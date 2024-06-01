package com.gabriel.orders.adapter.driven.persistence;

import com.gabriel.adapter.api.exceptions.NotFound;
import com.gabriel.core.application.exception.ApplicationError;
import com.gabriel.core.application.exception.ApplicationException;
import com.gabriel.orders.adapter.driven.persistence.mapper.MongoMapper;
import com.gabriel.orders.core.domain.model.Order;
import com.gabriel.orders.core.domain.port.OrderRepository;
import com.gabriel.orders.core.domain.port.OrderSearchParameters;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
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
            throw new ApplicationException(ex.getError().getMessage(), ApplicationError.APP_OO1);
        }
        return order;
    }

    @Override
    public Order updateOrder(Order newOrder) {
        Document document = MongoMapper.orderToDocument(newOrder);
        UpdateResult result = orderCollection.replaceOne(Filters.eq("_id", newOrder.getOrderId().getId()), document);
        if (result.getMatchedCount() == 0) {
            throw new NotFound("Order not found");
        }
        return newOrder;
    }

    @Override
    public Order getByTicket(String ticketId) {
        Document doc = orderCollection.find(Filters.eq("ticketId", ticketId)).first();
        if (doc != null) {
            return MongoMapper.documentToOrder(doc);
        }
        throw new NotFound("Order not found");
    }

    @Override
    public List<Order> searchBy(OrderSearchParameters parameters) {
        List<Order> orders = new ArrayList<>();
        if (parameters.status() != null) {
            orderCollection.find(Filters.eq("status", parameters.status().toString()))
                .forEach(doc -> orders.add(MongoMapper.documentToOrder(doc)));
        } else {
            orderCollection.find().forEach(doc -> orders.add(MongoMapper.documentToOrder(doc)));
        }
        return orders;
    }
}

