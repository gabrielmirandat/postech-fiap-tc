package com.gabriel.orders.infra.kafka;

import io.cloudevents.CloudEvent;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class KafkaProducerInterceptor<K, V> implements ProducerInterceptor<K, V> {

    private final JsonSerializer<CloudEvent> cloudEventSerializer = new JsonSerializer<>();

    @Override
    public ProducerRecord<K, V> onSend(ProducerRecord<K, V> record) {
        if (record.value() instanceof CloudEvent) {
            CloudEvent event = (CloudEvent) record.value();
            byte[] eventTypeHeader = event.getType().getBytes(StandardCharsets.UTF_8);
            record.headers().add("eventType", eventTypeHeader);
        }
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        // do nothing
    }

    @Override
    public void close() {
        // do nothing
    }


    @Override
    public void configure(Map<String, ?> map) {
        // do nothing
    }
}
