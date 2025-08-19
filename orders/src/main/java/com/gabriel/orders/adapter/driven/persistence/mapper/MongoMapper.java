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
        doc.append("_id", order.getOrderId().getValue())
            .append("items", order.getItems().stream()
                .map(MongoMapper::orderItemToDocument)
                .collect(Collectors.toList()))
            .append("shippingAddress", addressToDocument(order.getShippingAddress()))
            .append("notification", contactToDocument(order.getContact()))
            .append("price", order.getPrice().getValue())
            .append("ticketId", order.getTicketId())
            .append("status", order.getStatus().toString())
            .append("customer", Objects.nonNull(order.getCustomer()) ? order.getCustomer().getValue() : null)
            .append("creationTimestamp", order.getCreationTimestamp().toString())
            .append("updateTimestamp", order.getUpdateTimestamp().toString());
        return doc;
    }

    public static Order documentToOrder(Document doc) {
        OrderId orderId = OrderId.newBuilder().setValue(doc.getString("_id")).build();

        List<Document> itemsList = doc.getList("items", Document.class);
        List<OrderItem> items = itemsList.stream()
            .map(MongoMapper::documentToOrderItem)
            .collect(Collectors.toList());
        Cpf customer = Objects.nonNull(doc.getString("customer")) ? Cpf.newBuilder().setValue(doc.getString("customer")).build() : null;
        Address shippingAddress = documentToAddress((Document) doc.get("shippingAddress"));
        Contact contact = documentToContact((Document) doc.get("contact"));
        Price price = Price.newBuilder().setValue(doc.getDouble("price")).build();
        String ticketId = doc.getString("ticketId");
        OrderStatus status = OrderStatus.valueOf(doc.getString("status").toUpperCase());
        Instant createdAt = Instant.parse(doc.getString("creationTimestamp"));
        Instant updatedAt = Instant.parse(doc.getString("updateTimestamp"));

        return Order.copy(orderId, items, customer, shippingAddress, contact, price,
            ticketId, status, createdAt, updatedAt);
    }

    private static Document orderItemToDocument(OrderItem orderItem) {
        Document itemDoc = new Document();
        itemDoc.append("itemID", orderItem.getItemID().getValue())
            .append("product", productToDocument(orderItem.getProduct()))
            .append("extras", orderItem.getExtras().stream()
                .map(MongoMapper::extraToDocument)
                .collect(Collectors.toList()));
        return itemDoc;
    }

    private static OrderItem documentToOrderItem(Document doc) {
        OrderItemId itemId = OrderItemId.newBuilder().setValue(doc.getString("itemId")).build();
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
        return Address.newBuilder()
            .setStreet(doc.getString("street"))
            .setCity(doc.getString("city"))
            .setState(doc.getString("state"))
            .setZip(doc.getString("zip"))
            .build();
    }

    private static Document contactToDocument(Contact contact) {
        if (contact == null) {
            return null;
        }
        Document notifiableDoc = new Document();
        String value;
        String type;
        
        if (contact.hasCellphone()) {
            value = contact.getCellphone().getValue();
            type = "CELLPHONE";
        } else if (contact.hasEmail()) {
            value = contact.getEmail().getValue();
            type = "EMAIL";
        } else if (contact.hasCustomValue()) {
            value = contact.getCustomValue();
            type = "CUSTOM";
        } else {
            value = "";
            type = "UNKNOWN";
        }
        
        notifiableDoc.append("type", type);
        notifiableDoc.append("value", value);
        return notifiableDoc;
    }

    private static Contact documentToContact(Document doc) {
        if (doc == null) {
            return null;
        }
        String type = doc.getString("type");
        String value = doc.getString("value");
        
        Contact.Builder contactBuilder = Contact.newBuilder();
        
        if ("EMAIL".equalsIgnoreCase(type)) {
            contactBuilder.setType(ContactType.EMAIL)
                .setEmail(Email.newBuilder().setValue(value).build());
        } else if ("CUSTOM".equalsIgnoreCase(type)) {
            contactBuilder.setType(ContactType.CUSTOM)
                .setCustomValue(value);
        } else {
            // Default to CELLPHONE
            contactBuilder.setType(ContactType.CELLPHONE)
                .setCellphone(Cellphone.newBuilder().setValue(value).build());
        }
        
        return contactBuilder.build();
    }

    private static Document productToDocument(Product product) {
        Document productDoc = new Document();
        productDoc.append("productId", product.getProductId().getValue())
            .append("name", product.getName().getValue())
            .append("price", product.getPrice().getValue());
        return productDoc;
    }

    private static Product documentToProduct(Document doc) {
        ProductId productId = ProductId.newBuilder().setValue(doc.getString("productId")).build();
        Name name = Name.newBuilder().setValue(doc.getString("name")).build();
        Price price = Price.newBuilder().setValue(doc.getDouble("price")).build();
        return new Product(productId, name, price);
    }

    private static Document extraToDocument(Extra extra) {
        Document extraDoc = new Document();
        extraDoc.append("ingredientID", extra.getIngredientId().getValue())
            .append("name", extra.getName().getValue())
            .append("price", extra.getPrice().getValue());
        return extraDoc;
    }

    private static Extra documentToExtra(Document doc) {
        IngredientId ingredientID = IngredientId.newBuilder().setValue(doc.getString("ingredientID")).build();
        Name name = Name.newBuilder().setValue(doc.getString("name")).build();
        Price price = Price.newBuilder().setValue(doc.getDouble("price")).build();
        return new Extra(ingredientID, name, price);
    }
}
