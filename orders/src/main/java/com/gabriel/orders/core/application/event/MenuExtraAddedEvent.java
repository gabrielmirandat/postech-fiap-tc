package com.gabriel.orders.core.application.event;

import com.gabriel.orders.core.domain.model.Extra;

public record MenuExtraAddedEvent(Extra extraAdded, long timestamp) {
}
