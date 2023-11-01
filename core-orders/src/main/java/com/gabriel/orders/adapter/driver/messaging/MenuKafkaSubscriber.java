package com.gabriel.orders.adapter.driver.messaging;

import com.gabriel.orders.core.domain.events.MenuAddedEvent;
import com.gabriel.orders.core.domain.events.MenuDeletedEvent;
import com.gabriel.orders.core.domain.events.MenuUpdatedEvent;
import com.gabriel.orders.core.domain.ports.MenuSubscriber;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MenuKafkaSubscriber implements MenuSubscriber {

    @Override
    @KafkaListener(id = "menuProductAdded", topics = "menu")
    public void addProduct(MenuAddedEvent event) {
        // Implementação para adicionar produto
        System.out.println("Produto adicionado: " + event);
    }

    @Override
    @KafkaListener(id = "menuProductUpdated", topics = "menu")
    public void updateProduct(MenuUpdatedEvent event) {
        // Implementação para atualizar produto
        System.out.println("Produto atualizado: " + event);
    }

    @Override
    @KafkaListener(id = "menuProductDeleted", topics = "menu")
    public void deleteProduct(MenuDeletedEvent event) {
        // Implementação para deletar produto
        System.out.println("Produto deletado: " + event);
    }
}
