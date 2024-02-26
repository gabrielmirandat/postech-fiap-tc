package com.gabriel.orders.adapter.driver.api.mapper;

import com.gabriel.specs.orders.models.OrderStatusDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

@Component
public class StringToOrderStatusDTOConverter implements Converter<String, OrderStatusDTO> {

    Environment environment;

    public StringToOrderStatusDTOConverter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public OrderStatusDTO convert(String source) {

        if (environment.acceptsProfiles(Profiles.of("test")) &&
            source.equalsIgnoreCase("FAILURE")) {
            throw new RuntimeException("Invalid order status: " + source);
        }
        return OrderStatusDTO.valueOf(source.toUpperCase());
    }
}

