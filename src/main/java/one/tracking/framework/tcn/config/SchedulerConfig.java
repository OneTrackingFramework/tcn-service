/**
 *
 */
package one.tracking.framework.tcn.config;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import one.tracking.framework.tcn.repo.KeyRepository;

/**
 * @author Marko Voß
 *
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {

  @Autowired
  private KeyRepository keyRepository;

  @Scheduled(cron = "0 0 0 * * ?")
  public void scheduleTask() {

    this.keyRepository.deleteByCreatedAtBefore(Instant.now().minus(14, ChronoUnit.DAYS));
  }
}
