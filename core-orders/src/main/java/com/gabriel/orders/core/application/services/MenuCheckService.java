package com.gabriel.orders.core.application.services;

import java.util.List;

public interface MenuCheckService {
    boolean areProductsValid(List<String> productIds);
}
