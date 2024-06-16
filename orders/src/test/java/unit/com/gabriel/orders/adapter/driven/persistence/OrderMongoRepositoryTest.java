package unit.com.gabriel.orders.adapter.driven.persistence;

import com.gabriel.adapter.api.exceptions.NotFound;
import com.gabriel.orders.adapter.driven.persistence.OrderMongoRepository;
import com.gabriel.orders.adapter.driven.persistence.mapper.MongoMapper;
import com.gabriel.orders.core.domain.model.Order;
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
import utils.com.gabriel.orders.core.domain.OrderMock;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderMongoRepositoryTest {

    @Mock
    private MongoCollection<Document> mongoCollection;

    @Mock
    private UpdateResult updateResult;

    @Mock
    private FindIterable<Document> findIterable;

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
        Order order = OrderMock.validBasicOrder();
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
        Order newOrder = OrderMock.validBasicOrder();

        assertThrows(NotFound.class, () -> repository.updateOrder(newOrder));
    }

    @Test
    public void updateOrder_Success() {
        when(updateResult.getMatchedCount()).thenReturn(1L);
        when(mongoCollection.replaceOne(any(), any())).thenReturn(updateResult);
        Order newOrder = OrderMock.validBasicOrder();

        Order updatedOrder = repository.updateOrder(newOrder);
        assertNotNull(updatedOrder);
        assertEquals(newOrder, updatedOrder);
    }

    @Test
    public void getByTicket_NotFound_ThrowsNotFound() {
        when(mongoCollection.find(any(Bson.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(null);
        assertThrows(NotFound.class, () -> repository.getByTicket("ticket123"));
    }

    @Test
    public void getByTicket_Success() {
        Order order = OrderMock.validBasicOrder();
        Document document = MongoMapper.orderToDocument(order);

        when(mongoCollection.find(any(Bson.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(document);

        Order retrievedOrder = repository.getByTicket("ticket123");
        assertNotNull(retrievedOrder);
        assertEquals(order.getTicketId(), retrievedOrder.getTicketId());
    }

    @Test
    public void searchBy_WithStatus_FindsOrders() {
        // Mock orders
        Order order1 = OrderMock.validBasicOrder(); // Assume this sets a specific status
        Order order2 = OrderMock.validBasicOrder();
        // Assume MongoMapper can convert documents to orders correctly
        Document doc1 = MongoMapper.orderToDocument(order1);
        Document doc2 = MongoMapper.orderToDocument(order2);
        List<Document> documents = Arrays.asList(doc1, doc2);

        // Mock the behavior of the findIterable to simulate database operation
        when(mongoCollection.find(any(Bson.class))).thenReturn(findIterable);
        // Use thenAnswer to simulate forEach operation on findIterable
        doAnswer(invocation -> {
            Consumer<Document> consumer = invocation.getArgument(0);
            documents.forEach(consumer);
            return null;
        }).when(findIterable).forEach(any(Consumer.class));

        // Create search parameters with the specific status you're testing
        OrderSearchParameters parameters = new OrderSearchParameters(order1.getStatus());

        // Execute the search
        List<Order> foundOrders = repository.searchBy(parameters);

        // Assertions
        assertNotNull(foundOrders);
        assertEquals(2, foundOrders.size());
        assertTrue(foundOrders.stream().allMatch(order -> order.getStatus().equals(order1.getStatus())));

        // Optionally, verify interactions
        verify(findIterable).forEach(any(Consumer.class));
    }


}
