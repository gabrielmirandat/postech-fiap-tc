package com.gabriel.customers.adapter.driver.api.controllers;


import com.gabriel.specs.customers.CustomersApi;
import com.gabriel.specs.customers.models.CustomerDTO;
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
