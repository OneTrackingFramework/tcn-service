/**
 *
 */
package one.tracking.framework.tcn.service;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import one.tracking.framework.tcn.dto.KeyDto;
import one.tracking.framework.tcn.dto.KeyResultDto;
import one.tracking.framework.tcn.dto.PayloadDto;
import one.tracking.framework.tcn.entity.Key;
import one.tracking.framework.tcn.entity.Memo;
import one.tracking.framework.tcn.event.TcnEvent;
import one.tracking.framework.tcn.repo.KeyRepository;
import one.tracking.framework.tcn.repo.MemoRepository;

@Service
public class TcnService {

  private static final Logger LOG = LoggerFactory.getLogger(TcnService.class);

  @Autowired
  private KeyRepository keyRepository;

  @Autowired
  private MemoRepository memoRepository;

  @Autowired
  private KafkaTemplate<String, TcnEvent> tcnEventProducer;

  public void publishTcns(final List<String> tcns, final String memo) {

    final TcnEvent te = TcnEvent.builder()
        .tcns(tcns)
        .memo(memo)
        .build();

    LOG.info("-> " + tcns.get(0) + " ...");
    this.tcnEventProducer.send("tcn", te);
  }

  @KafkaListener(topics = "tcn", containerFactory = "kafka.listener.TcnEvent")
  public void receiveTcns(final TcnEvent tcnEvent) {

    LOG.info("<- " + tcnEvent.getTcns().get(0) + " ...");

    Memo memo = null;
    if (tcnEvent.getMemo() != null && tcnEvent.getMemo().length() > 0) {
      memo = this.memoRepository.save(Memo.builder().value(tcnEvent.getMemo()).build());
    }

    for (final String tcn : tcnEvent.getTcns()) {
      this.keyRepository.save(Key.builder()
          .tcn(tcn)
          .memo(memo)
          .build());
    }
  }


  public void handlePush(final PayloadDto payload) {

    final List<String> tcns = calculateTCNs(payload);

    this.publishTcns(tcns, payload.getMemo());
  }

  private List<String> calculateTCNs(final PayloadDto payload) {

    final List<String> tcns = new LinkedList<>();

    byte[] lastTck = Base64.getDecoder().decode(payload.getTck());
    final byte[] rvk = Base64.getDecoder().decode(payload.getRvk());

    // Generate from j1 to j2
    for (int j = payload.getJ1(); j <= payload.getJ2(); j++) {
      // tck_{j1+1} ← H_tck(rvk || tck_{j1}) # Ratchet
      final byte[] tckj = htck(rvk, lastTck);

      // tcn_{j1+1} ← H_tcn(le_u16(j1+1) || tck_{j1+1}) # Generate
      final byte[] htcn = htcn(j, tckj);

      tcns.add(Base64.getEncoder().encodeToString(htcn));

      lastTck = tckj;
    }

    return tcns;
  }

  /**
   * H_tck(rvk || tck_{j1})
   *
   * @param rvk
   * @param tck
   * @return
   */
  private byte[] htck(final byte[] rvk, final byte[] tck) {

    try {
      final MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return digest.digest(merge("H_TCK".getBytes(StandardCharsets.UTF_8), merge(rvk, tck)));

    } catch (final NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * little endian unsigned 16 bits H_tcn(le_u16(j1+1) || tck_{j1+1})
   *
   * @param j
   * @param tck
   * @return
   */
  private byte[] htcn(final int j, final byte[] tck) {

    try {

      final ByteBuffer jByte = ByteBuffer.allocate(2);

      // cast int to unsigned short
      jByte.put(0, (byte) ((j & 0xFF00L) >> 8));
      jByte.put(1, (byte) ((j & 0x00FFL)));

      final MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return digest.digest(merge("H_TCN".getBytes(StandardCharsets.UTF_8), merge(jByte.array(), tck)));

    } catch (final NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  private byte[] merge(final byte[] a, final byte[] b) {
    final byte[] c = new byte[a.length + b.length];
    System.arraycopy(a, 0, c, 0, a.length);
    System.arraycopy(b, 0, c, a.length, b.length);
    return c;
  }


  public Page<KeyDto> getKeys(final Instant timestamp, final Pageable pageable) {

    final Page<Key> result = timestamp == null
        ? this.keyRepository.findAll(pageable)
        : this.keyRepository.findAllByCreatedAtAfter(timestamp, pageable);

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

  public void handlePush(final byte[] payload) {

    if (payload.length < 68) {
      return;
    }
    final String rvk = Base64.getEncoder().encodeToString(Arrays.copyOfRange(payload, 0, 32));
    final String tck = Base64.getEncoder().encodeToString(Arrays.copyOfRange(payload, 32, 64));
    final int j1 = bytesToInt(Arrays.copyOfRange(payload, 64, 66));
    final int j2 = bytesToInt(Arrays.copyOfRange(payload, 66, 68));

    String memo = null;
    if (payload.length > 69) {
      final int range = bytesToInt(Arrays.copyOfRange(payload, 69, 70));

      if (range + 70 == payload.length) {
        memo = Base64.getEncoder().encodeToString(Arrays.copyOfRange(payload, 70, range));
      }

    }

    final PayloadDto payloadDto = PayloadDto.builder()
        .rvk(rvk)
        .tck(tck)
        .j1(j1)
        .j2(j2)
        .memo(memo)
        .build();

    handlePush(payloadDto);
  }


  private int bytesToInt(final byte[] bytes) {
    final short shortVal = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    return shortVal >= 0 ? shortVal : 0x10000 + shortVal;

  }
}
