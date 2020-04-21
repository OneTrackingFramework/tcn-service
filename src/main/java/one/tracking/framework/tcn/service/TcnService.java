/**
 *
 */
package one.tracking.framework.tcn.service;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import one.tracking.framework.tcn.dto.KeyDto;
import one.tracking.framework.tcn.dto.KeyResultDto;
import one.tracking.framework.tcn.dto.PayloadDto;
import one.tracking.framework.tcn.entity.Key;
import one.tracking.framework.tcn.entity.Memo;
import one.tracking.framework.tcn.repo.KeyRepository;
import one.tracking.framework.tcn.repo.MemoRepository;
@Service
public class TcnService {

  @Autowired
  private KeyRepository keyRepository;

  @Autowired
  private MemoRepository memoRepository;


  public void handlePush(final PayloadDto payload) {

    final List<String> tcns = calculateTCNs(payload);

    Memo memo = null;
    if (payload.getMemo() != null && payload.getMemo().length() > 0) {
      memo = this.memoRepository.save(Memo.builder().value(payload.getMemo()).build());
    }

    for (final String tcn : tcns) {
      this.keyRepository.save(Key.builder()
          .tcn(tcn)
          .memo(memo)
          .build());
    }
  }

  private List<String> calculateTCNs(final PayloadDto payload) {

    List<String> tcns = new LinkedList<>();

    byte[] lastTck =  Base64.getDecoder().decode(payload.getTck());
    byte[] rvk =  Base64.getDecoder().decode(payload.getRvk());

    // Generate from j1 to j2
    for (short j = payload.getJ1(); j <= payload.getJ2(); j++ ) {
      // tck_{j1+1} ← H_tck(rvk || tck_{j1})            # Ratchet
      byte[] tckj = tckj = htck(rvk, lastTck);

      // tcn_{j1+1} ← H_tcn(le_u16(j1+1) || tck_{j1+1}) # Generate
      byte[] htcn = htcn(j, tckj);

      tcns.add(Base64.getEncoder().encodeToString(htcn));

      lastTck = tckj;
    }

    return tcns;
  }

  private byte[] htck(final byte[] rvk, byte[] tck) {

    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return digest.digest(merge("H_TCK".getBytes(StandardCharsets.UTF_8),merge(rvk, tck)));
    } catch (NoSuchAlgorithmException e){
      // Rly, java?
      return null;
    }
  }

  private byte[] htcn(final short j, byte[] tck) {

    try {
      ByteBuffer jByte = ByteBuffer.allocate(2);
      jByte.putShort(j);

      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return digest.digest(merge("H_TCN".getBytes(StandardCharsets.UTF_8),merge(jByte.array(), tck)));
    } catch (NoSuchAlgorithmException e){
      // Rly, java?
      return null;
    }
  }

  private byte[] merge(final byte[] a, final byte[] b) {
    byte[] c = new byte[a.length + b.length];
    System.arraycopy(a, 0, c, 0, a.length);
    System.arraycopy(b, 0, c, a.length, b.length);
    return c;
  }


  public Page<KeyDto> getKeys(final Instant timestamp, final Pageable pageable) {

    final Page<Key> result = this.keyRepository.findAllByTimestampCreateAfter(timestamp, pageable);
    final Instant instant = Instant.now();

    final KeyResultDto keyResult = KeyResultDto.builder()
        .evalTimestamp(instant)
        .tcns(result.stream().map(KeyDto::fromEntity).collect(Collectors.toList()))
        .build();

    return new PageImpl<>(
        keyResult,
        pageable,
        result.getTotalElements());
  }
}
