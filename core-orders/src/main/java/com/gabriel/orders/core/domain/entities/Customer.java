package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.valueobjects.CPF;
import com.gabriel.orders.core.domain.valueobjects.EmailAddress;
import com.gabriel.orders.core.domain.valueobjects.EmailAddress;

public class Customer {

    private final CPF cpf;

    private final String name;

    private final EmailAddress email;

    public Customer(String cpf, String name, String email) {
        this.cpf = new CPF(cpf);
        this.name = name;
        this.email = new EmailAddress(email);
    }
}
