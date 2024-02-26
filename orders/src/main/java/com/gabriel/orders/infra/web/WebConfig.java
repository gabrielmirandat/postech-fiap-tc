package com.gabriel.orders.infra.web;

import com.gabriel.orders.adapter.driver.api.mapper.StringToOrderStatusDTOConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StringToOrderStatusDTOConverter stringToOrderStatusDTOConverter;

    public WebConfig(StringToOrderStatusDTOConverter stringToOrderStatusDTOConverter) {
        this.stringToOrderStatusDTOConverter = stringToOrderStatusDTOConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToOrderStatusDTOConverter);
    }
}

