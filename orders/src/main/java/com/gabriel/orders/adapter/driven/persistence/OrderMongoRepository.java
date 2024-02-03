package com.gabriel.orders.adapter.driven.persistence;

import com.gabriel.core.domain.model.*;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.OrderID;
import com.gabriel.core.domain.model.id.OrderItemID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.core.domain.model.*;
import com.gabriel.orders.core.domain.port.OrderRepository;
import com.gabriel.orders.core.domain.port.OrderSearchParameters;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderMongoRepository implements OrderRepository {

    private final MongoCollection<Document> orderCollection;

    public OrderMongoRepository(MongoCollection<Document> orderCollection) {
        this.orderCollection = orderCollection;
    }

    @Override
    public Order saveOrder(Order order) {
        Document document = OrderConverter.orderToDocument(order);
        orderCollection.insertOne(document);
        return order;
    }

    @Override
    public Order getByTicket(String ticket) {
        Document doc = orderCollection.find(Filters.eq("ticketId", ticket)).first();
        return doc != null ? OrderConverter.documentToOrder(doc) : null;
    }

    @Override
    public List<Order> searchBy(OrderSearchParameters parameters) {
        List<Order> orders = new ArrayList<>();
        if (parameters.status() != null) {
            orderCollection.find(Filters.eq("status", parameters.status().toString()))
                .forEach(doc -> orders.add(OrderConverter.documentToOrder(doc)));
        }
        return orders;
    }

    public class OrderConverter {

        public static Document orderToDocument(Order order) {
            Document doc = new Document();
            doc.append("_id", order.getOrderId().getId())
                .append("items", order.getItems().stream()
                    .map(OrderConverter::orderItemToDocument)
                    .collect(Collectors.toList()))
                .append("shippingAddress", addressToDocument(order.getShippingAddress()))
                .append("notification", notificationToDocument(order.getNotification()))
                .append("price", order.getPrice().getValue())
                .append("ticketId", order.getTicketId())
                .append("status", order.getStatus().toString())
                .append("customer", order.getCustomer().getId())
                .append("creationTimestamp", order.getCreationTimestamp().toString())
                .append("updateTimestamp", order.getUpdateTimestamp().toString());
            return doc;
        }

        public static Order documentToOrder(Document doc) {
            OrderID orderId = new OrderID(doc.getString("_id"));
            List<OrderItem> items = ((List<Document>) doc.get("items")).stream()
                .map(OrderConverter::documentToOrderItem)
                .collect(Collectors.toList());
            CPF customer = new CPF(doc.getString("customer"));
            Address shippingAddress = documentToAddress((Document) doc.get("shippingAddress"));
            Notification notification = documentToNotification((Document) doc.get("notification"));
            Price price = new Price(doc.getDouble("price"));
            String ticketId = doc.getString("ticketId");
            OrderStatus status = OrderStatus.valueOf(doc.getString("status").toUpperCase());
            Instant createdAt = Instant.parse(doc.getString("creationTimestamp"));
            Instant updatedAt = Instant.parse(doc.getString("updateTimestamp"));

            return Order.copy(orderId, items, customer, shippingAddress, notification, price,
                ticketId, status, createdAt, updatedAt);
        }

        private static Document orderItemToDocument(OrderItem orderItem) {
            Document itemDoc = new Document();
            itemDoc.append("itemID", orderItem.getItemID().getId())
                .append("product", productToDocument(orderItem.getProduct()))
                .append("extras", orderItem.getExtras().stream()
                    .map(OrderConverter::extraToDocument)
                    .collect(Collectors.toList()));
            return itemDoc;
        }

        private static OrderItem documentToOrderItem(Document doc) {
            OrderItemID itemID = new OrderItemID(doc.getString("itemID"));
            Product product = documentToProduct((Document) doc.get("product"));
            List<Extra> extras = ((List<Document>) doc.get("extras")).stream()
                .map(OrderConverter::documentToExtra)
                .collect(Collectors.toList());
            return OrderItem.copy(itemID, product, extras);
        }

        private static Document addressToDocument(Address address) {
            if (address == null) {
                return null;
            }
            return new Document()
                .append("street", address.getStreet())
                .append("city", address.getCity())
                .append("state", address.getState())
                .append("zip", address.getZip());
        }

        private static Address documentToAddress(Document doc) {
            if (doc == null) {
                return null;
            }
            return new Address(
                doc.getString("street"),
                doc.getString("city"),
                doc.getString("state"),
                doc.getString("zip")
            );
        }

        private static Document notificationToDocument(Notification notification) {
            if (notification == null) {
                return null;
            }
            Document notifiableDoc = new Document();
            notifiableDoc.append("type", notification.getType().toString())
                .append("value", notification.getRepr().getValue()); // Assuming the toString method gives the required representation

            return notifiableDoc;
        }

        private static Notification documentToNotification(Document doc) {
            if (doc == null) {
                return null;
            }
            NotificationType type = NotificationType.valueOf(doc.getString("type").toUpperCase());
            String value = doc.getString("value");

            return new Notification(type, value); // Assuming this constructor exists
        }

        private static Document productToDocument(Product product) {
            Document productDoc = new Document();
            productDoc.append("productID", product.getProductID().getId())
                .append("name", product.getName().getValue())
                .append("price", product.getPrice().getValue());
            return productDoc;
        }

        private static Product documentToProduct(Document doc) {
            ProductID productID = new ProductID(doc.getString("productID"));
            Name name = new Name(doc.getString("name"));
            Price price = new Price(doc.getDouble("price"));
            return new Product(productID, name, price);
        }

        private static Document extraToDocument(Extra extra) {
            Document extraDoc = new Document();
            extraDoc.append("ingredientID", extra.getIngredientID().getId())
                .append("name", extra.getName().getValue())
                .append("price", extra.getPrice().getValue());
            return extraDoc;
        }

        private static Extra documentToExtra(Document doc) {
            IngredientID ingredientID = new IngredientID(doc.getString("ingredientID"));
            Name name = new Name(doc.getString("name"));
            Price price = new Price(doc.getDouble("price"));
            return new Extra(ingredientID, name, price);
        }
    }
}

