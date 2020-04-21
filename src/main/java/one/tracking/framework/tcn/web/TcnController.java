/**
 *
 */
package one.tracking.framework.tcn.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import one.tracking.framework.tcn.dto.PayloadDto;
import one.tracking.framework.tcn.service.TcnService;

/**
 * @author Marko Voß
 *
 */
@RestController
@RequestMapping
public class TcnController {

  @Autowired
  private TcnService service;

  @RequestMapping
  public void handlePush(
      @RequestBody
      final PayloadDto payload) {

    this.service.handlePush(payload);
  }
}
