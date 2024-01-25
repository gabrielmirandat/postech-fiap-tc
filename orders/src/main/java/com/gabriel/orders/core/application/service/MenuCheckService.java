package com.gabriel.orders.core.application.service;

import java.util.List;

public interface MenuCheckService {
    boolean areProductsValid(List<String> productIds);
}
