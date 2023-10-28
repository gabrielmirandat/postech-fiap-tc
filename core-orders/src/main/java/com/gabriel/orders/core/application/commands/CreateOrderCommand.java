package com.gabriel.orders.core.application.commands;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateOrderCommand {
    private final List<String> products;
    private final List<List<String>> extras;
    private final String customer;
    private final List<String> address;
    private final String notification;
}
