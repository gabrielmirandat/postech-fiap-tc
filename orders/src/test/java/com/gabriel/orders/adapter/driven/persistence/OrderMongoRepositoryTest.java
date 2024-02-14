package com.gabriel.orders.adapter.driven.persistence;

import com.gabriel.adapter.api.exceptions.NotFound;
import com.gabriel.orders.adapter.TestContainersBase;
import com.gabriel.orders.core.OrderMock;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.OrderStatus;
import com.gabriel.orders.core.domain.port.OrderSearchParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataMongoTest
@Import(OrderMongoRepository.class)
public class OrderMongoRepositoryTest extends TestContainersBase {

    @Autowired
    private OrderMongoRepository orderRepository;

    private Order sampleOrder;

    private Order otherSampleOrder;

    @BeforeEach
    void setup() {
        sampleOrder = OrderMock.generateBasic();
        otherSampleOrder = OrderMock.generateFull();
    }

    @AfterEach
    void cleanUp() {
    }

    @Test
    void testSaveOrder() {
        Order savedOrder = orderRepository.saveOrder(sampleOrder);
        assertThat(savedOrder).isNotNull();
    }

    @Test
    void testUpdateOrder() {
        Order savedOrder = orderRepository.saveOrder(sampleOrder);
        savedOrder.promote(OrderStatus.PREPARATION);
        orderRepository.updateOrder(savedOrder);

        Order updatedOrder = orderRepository.getByTicket(savedOrder.getTicketId());
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.PREPARATION);
    }

    @Test
    void testGetByTicket() {
        orderRepository.saveOrder(sampleOrder);
        Order foundOrder = orderRepository.getByTicket(sampleOrder.getTicketId());
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getTicketId()).isEqualTo(sampleOrder.getTicketId());
    }

    @Test
    void testGetByTicketNotFound() {
        assertThrows(NotFound.class, () -> {
            orderRepository.getByTicket("ticketInexistente");
        });
    }

    @Test
    void testSearchByStatus() {
        orderRepository.saveOrder(sampleOrder);
        List<Order> foundOrders = orderRepository.searchBy(new OrderSearchParameters(OrderStatus.CREATED));

        assertThat(foundOrders).isNotEmpty();
        assertThat(foundOrders.get(0).getStatus()).isEqualTo(OrderStatus.CREATED);
    }
}
