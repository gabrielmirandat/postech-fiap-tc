package com.gabriel.orders.adapter.driver.messaging;

import com.gabriel.orders.core.domain.events.MenuAddedEvent;
import com.gabriel.orders.core.domain.events.MenuDeletedEvent;
import com.gabriel.orders.core.domain.events.MenuUpdatedEvent;
import com.gabriel.orders.core.domain.ports.MenuSubscriber;
import io.cloudevents.CloudEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MenuKafkaSubscriber implements MenuSubscriber {

    @KafkaListener(topics = "menub", groupId = "menu-group-id")
    public void listen(ConsumerRecord<String, CloudEvent> record) {
        CloudEvent event = record.value();
        // Aqui você pode manipular o evento CloudEvent
        System.out.println("Recebeu evento: " + event);
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
