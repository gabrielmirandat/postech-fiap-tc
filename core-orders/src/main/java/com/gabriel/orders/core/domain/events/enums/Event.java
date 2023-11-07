package com.gabriel.orders.core.domain.events.enums;

public enum Event {

    CONTEXT("context"),

    AUDIENCE("audience"),

    TAGS("tags"),

    HEADER_TYPE("eventType");

    private final String name;

    Event(String name) {
        this.name = name;
    }

    public String info() {
        return name;
    }

    public enum Context {

        DOMAIN("domain"),

        SYSTEM("system");

        private final String name;

        Context(String name) {
            this.name = name;
        }

        public String eventContextName() {
            return name;
        }
    }

    public enum Audience {
        INTERNAL_BOUNDED_CONTEXT("internal-bounded-context"),

        EXTERNAL_BOUNDED_CONTEXT("external-bounded-context");

        private final String name;

        Audience(String name) {
            this.name = name;
        }

        public String audienceName() {
            return name;
        }
    }
}

