package com.gabriel.orders.adapter.driven.persistence;

import com.gabriel.adapter.api.exceptions.NotFound;
import com.gabriel.core.application.exception.ApplicationError;
import com.gabriel.core.application.exception.ApplicationException;
import com.gabriel.orders.adapter.container.MongoDBTestContainer;
import com.gabriel.orders.core.OrderMock;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.OrderStatus;
import com.gabriel.orders.core.domain.port.OrderSearchParameters;
import com.gabriel.orders.infra.mongodb.MongoDbConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest
@Import({OrderMongoRepository.class, MongoDbConfig.class})
@ContextConfiguration(classes = MongoDBTestContainer.class)
public class OrderMongoRepositoryTest {

    @Autowired
    private OrderMongoRepository orderRepository;
    private Order basicOrder;
    private Order fullOrder;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        MongoDBTestContainer.mongoProperties(registry);
    }

    @BeforeEach
    void setup() {
        basicOrder = OrderMock.generateBasic();
        fullOrder = OrderMock.generateFull();
    }

    @Test
    void testSaveBasicRequiredFieldsOrder() {
        Order savedOrder = orderRepository.saveOrder(basicOrder);
        assertThat(savedOrder).isNotNull();
    }

    @Test
    void testSaveFullOptionalFieldsOrder() {
        Order savedOrder = orderRepository.saveOrder(fullOrder);
        assertThat(savedOrder).isNotNull();
    }

    @Test
    void testSaveOrderRestrictionById() {
        orderRepository.saveOrder(basicOrder);

        ApplicationException thrown = assertThrows(ApplicationException.class, () -> {
            orderRepository.saveOrder(basicOrder);
        }, "Expected saveOrder to throw, but it didn't");

        assertEquals(ApplicationError.APP_OO1.getMessage(), thrown.getType(), "The exception error does not match the expected value");
    }

    @Test
    void testUpdateOrder() {
        Order savedOrder = orderRepository.saveOrder(basicOrder);
        savedOrder.promote(OrderStatus.PREPARATION);
        orderRepository.updateOrder(savedOrder);

        Order updatedOrder = orderRepository.getByTicket(savedOrder.getTicketId());
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.PREPARATION);
    }

    @Test
    void testGetByTicket() {
        orderRepository.saveOrder(basicOrder);
        Order foundOrder = orderRepository.getByTicket(basicOrder.getTicketId());
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getTicketId()).isEqualTo(basicOrder.getTicketId());
    }

    @Test
    void testGetByTicketNotFound() {
        assertThrows(NotFound.class, () -> {
            orderRepository.getByTicket("ticketInexistente");
        });
    }

    @Test
    void testSearchByStatus() {
        orderRepository.saveOrder(basicOrder);
        List<Order> foundOrders = orderRepository.searchBy(new OrderSearchParameters(OrderStatus.CREATED));

        assertThat(foundOrders).isNotEmpty();
        assertThat(foundOrders.get(0).getStatus()).isEqualTo(OrderStatus.CREATED);
    }
}
