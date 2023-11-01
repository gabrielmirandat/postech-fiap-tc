package com.gabriel.orders.core.domain.entities;

import com.gabriel.orders.core.domain.valueobjects.CPF;
import com.gabriel.orders.core.domain.valueobjects.EmailData;
import lombok.Getter;

@Getter
public class Customer {

    private final CPF cpf;

    private final String name;

    private final EmailData email;

    public Customer(String cpf, String name, String email) {
        this.cpf = new CPF(cpf);
        this.name = name;
        this.email = new EmailData(email);
    }
}
