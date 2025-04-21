package com.gabriel.orders.adapter.driven.persistence.mapper;

import com.gabriel.model.*;
import com.gabriel.orders.core.domain.model.*;
import org.bson.Document;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MongoMapper {

    public static Document orderToDocument(Order order) {
        Document doc = new Document();
        doc.append("_id", order.getOrderId().getId())
            .append("items", order.getItems().stream()
                .map(MongoMapper::orderItemToDocument)
                .collect(Collectors.toList()))
            .append("shippingAddress", addressToDocument(order.getShippingAddress()))
            .append("notification", notificationToDocument(order.getContact()))
            .append("price", order.getPrice().getValue())
            .append("ticketId", order.getTicketId())
            .append("status", order.getStatus().toString())
            .append("customer", Objects.nonNull(order.getCustomer()) ? order.getCustomer().getId() : null)
            .append("creationTimestamp", order.getCreationTimestamp().toString())
            .append("updateTimestamp", order.getUpdateTimestamp().toString());
        return doc;
    }

    public static Order documentToOrder(Document doc) {
        OrderId orderId = new OrderId(doc.getString("_id"));

        List<Document> itemsList = doc.getList("items", Document.class);
        List<OrderItem> items = itemsList.stream()
            .map(MongoMapper::documentToOrderItem)
            .collect(Collectors.toList());
        Cpf customer = Objects.nonNull(doc.getString("customer")) ? new Cpf(doc.getString("customer")) : null;
        Address shippingAddress = documentToAddress((Document) doc.get("shippingAddress"));
        Contact contact = documentToContact((Document) doc.get("contact"));
        Price price = new Price(doc.getDouble("price"));
        String ticketId = doc.getString("ticketId");
        OrderStatus status = OrderStatus.valueOf(doc.getString("status").toUpperCase());
        Instant createdAt = Instant.parse(doc.getString("creationTimestamp"));
        Instant updatedAt = Instant.parse(doc.getString("updateTimestamp"));

        return Order.copy(orderId, items, customer, shippingAddress, contact, price,
            ticketId, status, createdAt, updatedAt);
    }

    private static Document orderItemToDocument(OrderItem orderItem) {
        Document itemDoc = new Document();
        itemDoc.append("itemID", orderItem.getItemID().getId())
            .append("product", productToDocument(orderItem.getProduct()))
            .append("extras", orderItem.getExtras().stream()
                .map(MongoMapper::extraToDocument)
                .collect(Collectors.toList()));
        return itemDoc;
    }

    private static OrderItem documentToOrderItem(Document doc) {
        OrderItemId itemId = new OrderItemId(doc.getString("itemId"));
        Product product = documentToProduct((Document) doc.get("product"));

        List<Document> extrasList = doc.getList("extras", Document.class);
        List<Extra> extras = extrasList.stream()
            .map(MongoMapper::documentToExtra)
            .collect(Collectors.toList());
        return OrderItem.copy(itemId, product, extras);
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

    private static Document contactToDocument(Contact contact) {
        if (contact == null) {
            return null;
        }
        Document notifiableDoc = new Document();
        notifiableDoc.append("type", contact.getType().toString())
            .append("value", contact.getRepr().getValue()); // Assuming the toString method gives the required representation

        return notifiableDoc;
    }

    private static Contact documentToContact(Document doc) {
        if (doc == null) {
            return null;
        }
        ContactType type = ContactType.valueOf(doc.getString("type").toUpperCase());
        String value = doc.getString("value");

        return new Contact(type, value); // Assuming this constructor exists
    }

    private static Document productToDocument(Product product) {
        Document productDoc = new Document();
        productDoc.append("productId", product.getProductId().getId())
            .append("name", product.getName().getValue())
            .append("price", product.getPrice().getValue());
        return productDoc;
    }

    private static Product documentToProduct(Document doc) {
        ProductId productId = new ProductId(doc.getString("productId"));
        Name name = new Name(doc.getString("name"));
        Price price = new Price(doc.getDouble("price"));
        return new Product(productId, name, price);
    }

    private static Document extraToDocument(Extra extra) {
        Document extraDoc = new Document();
        extraDoc.append("ingredientID", extra.getIngredientID().getId())
            .append("name", extra.getName().getValue())
            .append("price", extra.getPrice().getValue());
        return extraDoc;
    }

    private static Extra documentToExtra(Document doc) {
        IngredientId ingredientID = new IngredientId(doc.getString("ingredientID"));
        Name name = new Name(doc.getString("name"));
        Price price = new Price(doc.getDouble("price"));
        return new Extra(ingredientID, name, price);
    }
}
