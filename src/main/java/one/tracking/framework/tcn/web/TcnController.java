/**
 *
 */
package one.tracking.framework.tcn.web;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import one.tracking.framework.tcn.dto.KeyDto;
import one.tracking.framework.tcn.dto.PayloadDto;
import one.tracking.framework.tcn.service.TcnService;

@RestController
@RequestMapping
public class TcnController {

  @Autowired
  private TcnService service;

  @RequestMapping(method = RequestMethod.POST)
  public void handlePush(
      @RequestBody
      final PayloadDto payload) {

    this.service.handlePush(payload);
  }

  @RequestMapping(method = RequestMethod.GET)
  public Page<KeyDto> getKeys(
      @RequestParam(value = "ts", required = false)
      final Instant timestamp,
      @PageableDefault(page = 0, size = 20)
      // @SortDefault.SortDefaults({
      // @SortDefault(sort = "timestampCreate", direction = Sort.Direction.ASC),
      // })
      final Pageable pageable) {

    return this.service.getKeys(timestamp, pageable);
  }
}
