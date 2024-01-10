package com.gabriel.orders.adapter.driver.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.orders.core.application.events.*;
import com.gabriel.orders.core.domain.ports.MenuSubscriber;
import io.cloudevents.CloudEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class MenuKafkaSubscriber implements MenuSubscriber {

    private final ObjectMapper mapper;

    public MenuKafkaSubscriber(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @KafkaListener(topics = "menu", groupId = "orders-group-id")
    public void listenMenuEvents(@Payload CloudEvent cloudEvent) {

        try {
            switch (cloudEvent.getType()) {
                case "postech.menu.v1.product.created":
                    addProduct(new MenuProductAddedEvent(
                        CloudEventMapper.productFrom(cloudEvent, mapper)));
                    break;
                case "postech.menu.v1.product.deleted":
                    deleteProduct(new MenuProductDeletedEvent(
                        CloudEventMapper.productFrom(cloudEvent, mapper)));
                    break;
                case "postech.menu.v1.ingredient.created":
                    addIngredient(new MenuIngredientAddedEvent(
                        CloudEventMapper.ingredientFrom(cloudEvent, mapper)));
                    break;
                case "postech.menu.v1.ingredient.deleted":
                    deleteIngredient(new MenuIngredientDeletedEvent(
                        CloudEventMapper.ingredientFrom(cloudEvent, mapper)));
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            // TODO: deadletters
            System.out.println("Erro ao processar evento: " + ex.getMessage());
        }
    }

    @Override
    public void addProduct(MenuProductAddedEvent event) {
        System.out.println("Produto adicionado: " + event);
    }

    @Override
    public void deleteProduct(MenuProductDeletedEvent event) {
        System.out.println("Produto deletado: " + event);
    }

    @Override
    public void addIngredient(MenuIngredientAddedEvent event) {
        System.out.println("Ingrediente adicionado: " + event);
    }

    @Override
    public void deleteIngredient(MenuIngredientDeletedEvent event) {
        System.out.println("Ingrediente deletado: " + event);
    }
}
