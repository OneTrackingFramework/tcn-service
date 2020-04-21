/**
 *
 */
package one.tracking.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class TcnApplication {

  public static void main(final String[] args) {
    SpringApplication.run(TcnApplication.class, args);
  }

}
