package com.gabriel.customers.adapter.driver.api.controllers;

import com.gabriel.customers.adapter.driver.api.controller.CustomersApi;
import com.gabriel.customers.adapter.driver.api.models.CustomerDTO;
import org.springframework.http.ResponseEntity;

public class CustomersController implements CustomersApi {


    @Override
    public ResponseEntity<Void> createCustomer(CustomerDTO customerDTO) throws Exception {
        return null;
    }

    @Override
    public ResponseEntity<CustomerDTO> getCustomerById(String govId) throws Exception {
        return null;
    }
}
