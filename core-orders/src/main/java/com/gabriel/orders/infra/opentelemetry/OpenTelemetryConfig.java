package com.gabriel.orders.infra.opentelemetry;

import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenTelemetryConfig {

    @Value("${newrelic.otlp.url}")
    private String newRelicOtlpUrl;

    @Value("${newrelic.app.name}")
    private String newRelicAppName;

    @Value("$newrelic.service.key")
    private String newRelicServiceKey;

    @Bean
    public Tracer tracer() {
        // Configure the exporter
        OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder()
            .setEndpoint(newRelicOtlpUrl) // Use the appropriate endpoint
            .addHeader("api-key", newRelicServiceKey)
            .build();

        // Configure the SDK with the exporter
        OpenTelemetrySdk openTelemetrySdk = OpenTelemetrySdk.builder()
            .setTracerProvider(
                SdkTracerProvider.builder()
                    .addSpanProcessor(SimpleSpanProcessor.create(spanExporter))
                    .build()
            ).build();

        return openTelemetrySdk.getTracer(newRelicAppName); // Replace with your application's name
    }
}
