package com.gabriel.orders.core.domain.model;

import com.gabriel.common.core.domain.base.ValueObject;
import com.gabriel.common.core.domain.model.CPF;
import com.gabriel.common.core.domain.model.EmailData;
import com.gabriel.common.core.domain.model.Name;
import lombok.Getter;

@Getter
public class Customer extends ValueObject {

    private final CPF cpf;

    private final Name name;

    private final EmailData email;

    public Customer(CPF cpf, Name name, EmailData email) {
        this.cpf = cpf;
        this.name = name;
        this.email = email;
    }

    public Customer(String cpf, String name, String email) {
        this.cpf = new CPF(cpf);
        this.name = new Name(name);
        this.email = new EmailData(email);
    }
}
