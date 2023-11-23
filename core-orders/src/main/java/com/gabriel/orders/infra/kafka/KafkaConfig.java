package com.gabriel.orders.infra.kafka;

import com.gabriel.orders.core.domain.events.enums.MenuEvent;
import io.cloudevents.CloudEvent;
import io.cloudevents.kafka.CloudEventDeserializer;
import io.cloudevents.kafka.CloudEventSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gabriel.orders.core.domain.events.enums.Event.HEADER_TYPE;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${kafka.server.url}")
    private String kafkaServerUrl;

    @Value("${kafka.group.id}")
    private String kafkaGroupId;

    @Bean
    public RecordFilterStrategy<String, String> menuProductCreatedFilterStrategy() {
        return consumerRecord -> {
            if (consumerRecord.headers().lastHeader(HEADER_TYPE.info()) == null) {
                return true;
            }
            String eventType = new String(consumerRecord.headers()
                .lastHeader(HEADER_TYPE.info()).value());
            return !eventType.equals(MenuEvent.Type.CREATED.eventType());
        };
    }

    @Bean
    public ConsumerFactory<String, CloudEvent> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CloudEventDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
            new CloudEventDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CloudEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CloudEvent> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ProducerFactory<String, CloudEvent> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerUrl);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CloudEventSerializer.class);
        configProps.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,
            List.of(KafkaProducerInterceptor.class));

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, CloudEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
