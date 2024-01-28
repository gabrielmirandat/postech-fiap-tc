package com.gabriel.orders.adapter.driver.messaging;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.orders.core.application.event.*;
import com.gabriel.orders.core.domain.port.MenuRepository;
import com.gabriel.orders.core.domain.port.MenuSubscriber;
import io.cloudevents.CloudEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class MenuKafkaSubscriber implements MenuSubscriber {

    private final ObjectMapper mapper;

    private final MenuRepository menuRepository;

    public MenuKafkaSubscriber(ObjectMapper mapper, MenuRepository menuRepository) {
        this.mapper = mapper;
        this.menuRepository = menuRepository;

        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @KafkaListener(topics = "menu", groupId = "orders-group-id")
    public void listenMenuEvents(@Payload CloudEvent cloudEvent,
                                 @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp) {

        try {
            switch (cloudEvent.getType()) {
                case "postech.menu.v1.product.created":
                    addProduct(new MenuProductAddedEvent(
                        CloudEventMapper.productFrom(mapper, cloudEvent),
                        timestamp));
                    break;
                case "postech.menu.v1.product.deleted":
                    deleteProduct(new MenuProductDeletedEvent(
                        CloudEventMapper.productFrom(mapper, cloudEvent)));
                    break;
                case "postech.menu.v1.ingredient.created":
                    addExtra(new MenuExtraAddedEvent(
                        CloudEventMapper.extraFrom(mapper, cloudEvent),
                        timestamp));
                    break;
                case "postech.menu.v1.ingredient.deleted":
                    deleteExtra(new MenuExtraDeletedEvent(
                        CloudEventMapper.extraFrom(mapper, cloudEvent)));
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            // TODO: deadletters
            System.out.println("Erro ao  " +
                "processar evento: " + ex.getMessage());
        }
    }

    @Override
    public void addProduct(MenuProductAddedEvent event) {
        System.out.println("Produto adicionado: " + event);
        menuRepository.addProduct(event.productAdded());
    }

    @Override
    public void deleteProduct(MenuProductDeletedEvent event) {
        System.out.println("Produto deletado: " + event);
        menuRepository.deleteProduct(event.productDeleted().getProductID());
    }

    @Override
    public void addExtra(MenuExtraAddedEvent event) {
        System.out.println("Ingrediente adicionado: " + event);
        menuRepository.addExtra(event.extraAdded());
    }

    @Override
    public void deleteExtra(MenuExtraDeletedEvent event) {
        System.out.println("Ingrediente deletado: " + event);
        menuRepository.deleteExtra(event.extraDeleted().getIngredientID());
    }
}
