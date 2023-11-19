package com.gabriel.orders.adapter.driven.persistence;

import com.gabriel.orders.core.domain.entities.Order;
import com.gabriel.orders.core.domain.ports.OrderRepository;
import com.gabriel.orders.core.domain.ports.models.OrderSearchParameters;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderJpaMongoRepository implements OrderRepository {

    private final MongoTemplate mongoTemplate;

    public OrderJpaMongoRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Order saveOrder(Order order) {
        mongoTemplate.save(order, "orders");
        return order;
    }

    @Override
    public Order getByTicket(String ticket) {
        Query query = new Query();
        query.addCriteria(Criteria.where("ticketId").is(ticket));
        return mongoTemplate.findOne(query, Order.class, "orders");
    }

    @Override
    public List<Order> searchBy(OrderSearchParameters parameters) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(parameters.status));
        return mongoTemplate.find(query, Order.class, "orders");
    }
}
