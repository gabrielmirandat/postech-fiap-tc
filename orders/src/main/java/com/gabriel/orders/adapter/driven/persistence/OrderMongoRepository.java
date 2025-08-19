package com.gabriel.orders.adapter.driven.persistence;

import com.gabriel.orders.adapter.driven.persistence.mapper.MongoMapper;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.port.OrderRepository;
import com.gabriel.orders.core.domain.port.OrderSearchParameters;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.UpdateResult;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderMongoRepository implements OrderRepository {

    private final MongoCollection<Document> orderCollection;

    public OrderMongoRepository(MongoCollection<Document> orderCollection) {
        this.orderCollection = orderCollection;
    }

    @Override
    public Order saveOrder(Order order) {
        Document document = MongoMapper.orderToDocument(order);

        try {
            orderCollection.insertOne(document);
        } catch (MongoWriteException ex) {
            throw new RuntimeException("Database error: " + ex.getError().getMessage(), ex);
        }
        return order;
    }

    @Override
    public Order updateOrder(Order newOrder) {
        Document document = MongoMapper.orderToDocument(newOrder);
        UpdateResult result = orderCollection.replaceOne(Filters.eq("_id", newOrder.getOrderId().getValue()), document);
        if (result.getMatchedCount() == 0) {
            throw new RuntimeException("Order not found");
        }
        return newOrder;
    }

    @Override
    public Order getByTicket(String ticketId) {
        Document doc = orderCollection.find(Filters.eq("ticketId", ticketId)).first();
        if (doc != null) {
            return MongoMapper.documentToOrder(doc);
        }
        throw new RuntimeException("Order not found");
    }

    @Override
    public List<Order> searchBy(OrderSearchParameters parameters) {
        List<Order> orders = new ArrayList<>();
        Bson filter = parameters.status() != null ? Filters.eq("status", parameters.status().toString()) : new BsonDocument();
        FindIterable<Document> iterable = orderCollection.find(filter).sort(Sorts.descending("creationTimestamp"));
        iterable.forEach(doc -> orders.add(MongoMapper.documentToOrder(doc)));
        return orders;
    }
}

