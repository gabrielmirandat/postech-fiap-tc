using Cassandra;
using Confluent.Kafka;
using MediatR;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Stripe;
using Payments.Application.UseCases.CreatePayment;
using Payments.Domain.Repositories;
using Payments.Domain.Services;
using Payments.Domain.Events;
using Payments.Infrastructure.Gateways;
using Payments.Infrastructure.Messaging;
using Payments.Infrastructure.Persistence;

namespace Payments.Infrastructure.Configuration;

public static class DependencyInjection
{
    public static IServiceCollection AddInfrastructure(
        this IServiceCollection services,
        IConfiguration configuration)
    {
        // Cassandra
        var cassandraContactPoints = configuration["Cassandra:ContactPoints"] ?? "localhost:9042";
        var cassandraKeyspace = configuration["Cassandra:Keyspace"] ?? "payments";
        var session = CassandraConfiguration.CreateSession(cassandraContactPoints, cassandraKeyspace);
        services.AddSingleton(session);
        services.AddScoped<PaymentDbContext>(sp => new PaymentDbContext(session, cassandraKeyspace));
        services.AddScoped<IPaymentRepository, PaymentRepository>();

        // Stripe
        var stripeApiKey = configuration["Stripe:ApiKey"];
        if (!string.IsNullOrEmpty(stripeApiKey))
        {
            StripeConfiguration.ApiKey = stripeApiKey;
        }
        services.AddScoped<PaymentIntentService>();
        services.AddScoped<IPaymentAuthorizationService, StripePaymentAuthorizationService>();

        // Kafka
        var kafkaBootstrapServers = configuration["Kafka:BootstrapServers"] ?? "localhost:9092";
        var producerConfig = new ProducerConfig
        {
            BootstrapServers = kafkaBootstrapServers
        };
        services.AddSingleton<IProducer<string, string>>(sp => new ProducerBuilder<string, string>(producerConfig).Build());
        services.AddScoped<IEventPublisher, KafkaEventPublisher>();

        // MediatR
        services.AddMediatR(cfg => cfg.RegisterServicesFromAssembly(typeof(CreatePaymentHandler).Assembly));

        return services;
    }
}
