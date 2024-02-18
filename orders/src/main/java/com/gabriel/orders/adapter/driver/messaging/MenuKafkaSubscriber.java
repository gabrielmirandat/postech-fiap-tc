package com.gabriel.orders.adapter.driver.messaging;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.orders.core.application.event.*;
import com.gabriel.orders.core.application.usecase.UpdateMenuUseCase;
import com.gabriel.orders.core.domain.port.MenuSubscriber;
import io.cloudevents.CloudEvent;
import lombok.Setter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class MenuKafkaSubscriber implements MenuSubscriber {

    private final ObjectMapper mapper;

    private final UpdateMenuUseCase updateMenuUseCase;

    @Setter
    private CountDownLatch countDownLatch;

    public MenuKafkaSubscriber(ObjectMapper mapper, UpdateMenuUseCase updateMenuUseCase) {
        this.mapper = mapper;
        this.updateMenuUseCase = updateMenuUseCase;

        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @KafkaListener(topics = "menu", groupId = "orders-group-id")
    public void listenMenuEvents(@Payload CloudEvent cloudEvent,
                                 @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp) {

        try {
            switch (cloudEvent.getType()) {
                case "postech.menu.v1.product.created":
                    listenProductAdded(new MenuProductAddedEvent(
                        CloudEventMapper.productFrom(mapper, cloudEvent),
                        timestamp));
                    break;
                case "postech.menu.v1.product.deleted":
                    listenProductDeleted(new MenuProductDeletedEvent(
                        CloudEventMapper.productFrom(mapper, cloudEvent)));
                    break;
                case "postech.menu.v1.ingredient.created":
                    listenExtraAdded(new MenuExtraAddedEvent(
                        CloudEventMapper.extraFrom(mapper, cloudEvent),
                        timestamp));
                    break;
                case "postech.menu.v1.ingredient.deleted":
                    listenExtraDeleted(new MenuExtraDeletedEvent(
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

        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    @Override
    public void listenProductAdded(MenuProductAddedEvent event) {
        updateMenuUseCase.handleProductAdded(event.productAdded());
    }

    @Override
    public void listenProductDeleted(MenuProductDeletedEvent event) {
        updateMenuUseCase.handleProductDeleted(event.productDeleted());
    }

    @Override
    public void listenExtraAdded(MenuExtraAddedEvent event) {
        updateMenuUseCase.handleExtraAdded(event.extraAdded());
    }

    @Override
    public void listenExtraDeleted(MenuExtraDeletedEvent event) {
        updateMenuUseCase.handleExtraDeleted(event.extraDeleted());
    }
}
