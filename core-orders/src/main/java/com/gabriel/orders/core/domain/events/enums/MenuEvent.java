package com.gabriel.orders.core.domain.events.enums;

public enum MenuEvent {

    TYPE("type"),

    SOURCE("source"),

    SUBJECT("subject");

    private final String name;

    MenuEvent(String name) {
        this.name = name;
    }

    public String infoName() {
        return name;
    }

    public enum Type {
        CREATED("postech.menu.v1.product.created"),
        UPDATED("postech.menu.v1.product.updated"),
        DELETED("postech.menu.v1.product.deleted");

        private final String type;

        Type(String type) {
            this.type = type;
        }

        public String eventType() {
            return type;
        }
    }

    public enum Source {
        CREATED("post/products"),
        UPDATED("post/products/%s"),
        DELETED("delete/products/%s");

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
