package com.gabriel.orders.core.domain.events.enums;

public enum OrderEvent {

    TYPE("type"),

    SOURCE("source"),

    SUBJECT("subject");

    private final String name;

    OrderEvent(String name) {
        this.name = name;
    }

    public String infoName() {
        return name;
    }

    public enum Type {
        CREATED("postech.orders.v1.order.created"),
        UPDATED("postech.orders.v1.order.updated"),
        DELETED("postech.orders.v1.order.deleted");

        private final String type;

        Type(String type) {
            this.type = type;
        }

        public String eventType() {
            return type;
        }
    }

    public enum Source {
        CREATED("post/orders"),
        UPDATED("post/orders/%s"),
        DELETED("delete/orders/%s");

        private final String source;

        Source(String source) {
            this.source = source;
        }

        public String eventSource() {
            return source;
        }
    }

    public enum Subject {
        ID("id/%s");

        private final String id;

        Subject(String id) {
            this.id = id;
        }

        public String eventSubject() {
            return id;
        }
    }
}
