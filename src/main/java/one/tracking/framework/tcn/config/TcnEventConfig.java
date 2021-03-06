/**
 *
 */
package one.tracking.framework.tcn.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import one.tracking.framework.tcn.event.TcnEvent;

@Configuration
@EnableKafka
public class TcnEventConfig extends KafkaConfig {

  @Bean(name = "kafka.producer.TcnEvent")
  public ProducerFactory<String, TcnEvent> tcnEventProducerFactory() {
    return new DefaultKafkaProducerFactory<>(senderConfig());
  }

  @Bean(name = "kafka.template.TcnEvent")
  public KafkaTemplate<String, TcnEvent> tcnEventKafkaTemplate() {
    return new KafkaTemplate<>(tcnEventProducerFactory());
  }

  @Bean(name = "kafka.consumer.TcnEvent")
  public ConsumerFactory<String, TcnEvent> tcnEventConsumerFactory() {
    return new DefaultKafkaConsumerFactory<>(
        consumerConfig(),
        new StringDeserializer(),
        new JsonDeserializer<>(TcnEvent.class));
  }

  @Bean(name = "kafka.listener.TcnEvent")
  public ConcurrentKafkaListenerContainerFactory<String, TcnEvent> tcnEventListenerContainerFactory() {

    final ConcurrentKafkaListenerContainerFactory<String, TcnEvent> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(tcnEventConsumerFactory());
    return factory;
  }
}
