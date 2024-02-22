package com.gabriel.orders.adapter.driven.persistence;

import com.gabriel.adapter.api.exceptions.NotFound;
import com.gabriel.orders.adapter.driven.persistence.mapper.MongoMapper;
import com.gabriel.orders.core.OrderMock;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.model.OrderStatus;
import com.gabriel.orders.core.domain.port.OrderSearchParameters;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderMongoRepositoryTest {

    @Mock
    private MongoCollection<Document> mongoCollection;

    @Mock
    private UpdateResult updateResult;

    @Mock
    private FindIterable<Bson> iterableMock;

    @Captor
    private ArgumentCaptor<Document> documentCaptor;

    private OrderMongoRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new OrderMongoRepository(mongoCollection);
    }

    @Test
    public void saveOrder_Success() {
        Order order = OrderMock.generateBasic();
        repository.saveOrder(order);
        verify(mongoCollection).insertOne(documentCaptor.capture());
        Document captured = documentCaptor.getValue();

        assertNotNull(captured);
        assertEquals(order.getTicketId(), captured.getString("ticketId"));
    }

    @Test
    public void updateOrder_OrderNotFound_ThrowsNotFound() {
        when(updateResult.getMatchedCount()).thenReturn(0L);
        when(mongoCollection.replaceOne(any(), any())).thenReturn(updateResult);
        Order newOrder = OrderMock.generateBasic();

        assertThrows(NotFound.class, () -> repository.updateOrder(newOrder));
    }

    @Test
    public void updateOrder_Success() {
        when(updateResult.getMatchedCount()).thenReturn(1L);
        when(mongoCollection.replaceOne(any(), any())).thenReturn(updateResult);
        Order newOrder = OrderMock.generateBasic();

        Order updatedOrder = repository.updateOrder(newOrder);
        assertNotNull(updatedOrder);
        assertEquals(newOrder, updatedOrder);
    }

    @Test
    public void getByTicket_NotFound_ThrowsNotFound() {
        lenient().when(mongoCollection.find((Class<Bson>) any()))
            .thenReturn(iterableMock);
        when(iterableMock.first()).thenReturn(null);
        assertThrows(NotFound.class, () -> repository.getByTicket("ticket123"));
    }

    @Test
    public void getByTicket_Success() {
        Order order = OrderMock.generateBasic();
        Document document = MongoMapper.orderToDocument(order);

        when(mongoCollection.find((Class<Bson>) any())).thenReturn(iterableMock);
        when(iterableMock.first()).thenReturn(document);

        Order retrievedOrder = repository.getByTicket("ticket123");
        assertNotNull(retrievedOrder);
        assertEquals(order.getTicketId(), retrievedOrder.getTicketId());
    }

    @Test
    public void searchBy_WithStatus_FindsOrders() {
        // Mock the behavior of `mongoCollection.find()` to return a cursor-like structure that you iterate over
        // This could be a mock of `FindIterable<Document>` and `MongoCursor<Document>`
        // Use `when(...).thenReturn(...)` to simulate the database returning documents
        // Then, verify that `searchBy` correctly transforms these documents into `Order` objects

        List<Document> documents = List.of(new Document(), new Document()); // Populate these documents as needed
        // Mock the find and forEach behavior here

        OrderSearchParameters parameters = new OrderSearchParameters(OrderStatus.DELIVERY); // Set up parameters as needed
        List<Order> orders = repository.searchBy(parameters);
        assertFalse(orders.isEmpty());
        // Additional assertions as needed
    }
}
