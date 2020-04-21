/**
 *
 */
package one.tracking.framework.tcn.service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    // TODO implement calculation of TCNs
    return Collections.emptyList();
  }

  @RequestMapping(method = RequestMethod.GET)
  public Page<KeyDto> getKeys(
      @RequestParam("ts")
      final Instant timestamp,
      @PageableDefault(page = 0, size = 20)
      // @SortDefault.SortDefaults({
      // @SortDefault(sort = "timestampCreate", direction = Sort.Direction.ASC),
      // })
      final Pageable pageable) {

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
