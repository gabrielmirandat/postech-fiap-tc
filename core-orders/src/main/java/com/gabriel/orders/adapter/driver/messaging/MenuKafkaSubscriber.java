package com.gabriel.orders.adapter.driver.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.orders.core.domain.events.MenuAddedEvent;
import com.gabriel.orders.core.domain.events.MenuDeletedEvent;
import com.gabriel.orders.core.domain.events.MenuUpdatedEvent;
import com.gabriel.orders.core.domain.ports.MenuSubscriber;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.CloudEventUtils;
import io.cloudevents.jackson.PojoCloudEventDataMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MenuKafkaSubscriber implements MenuSubscriber {

    private final ObjectMapper mapper;

    public MenuKafkaSubscriber(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @KafkaListener(topics = "menub", groupId = "orders-group-id")
    public void listen(@Payload CloudEvent cloudEvent,
                       @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) Optional<String> key,
                       @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                       @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) {

        System.out.println(cloudEvent);

        try {
            // Deserialize the data payload from the CloudEvent
            var deserializedData = CloudEventUtils
                    .mapData(cloudEvent, PojoCloudEventDataMapper.from(mapper, MenuAddedEvent.class));
            MenuAddedEvent menuAddedEvent = deserializedData.getValue();
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void addProduct(MenuAddedEvent event) {
        // Implementação para adicionar produto
        System.out.println("Produto adicionado: " + event);
    }

    @Override
    public void updateProduct(MenuUpdatedEvent event) {
        // Implementação para atualizar produto
        System.out.println("Produto atualizado: " + event);
    }

    @Override
    public void deleteProduct(MenuDeletedEvent event) {
        // Implementação para deletar produto
        System.out.println("Produto deletado: " + event);
    }
}
