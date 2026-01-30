using Cassandra;
using Confluent.Kafka;
using MediatR;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Payments.Application.UseCases.CreatePayment;
using Payments.Domain.Repositories;
using Payments.Domain.Services;
using Payments.Domain.Events;
using Payments.Infrastructure.Gateways;
using Payments.Infrastructure.Messaging;
using Payments.Infrastructure.Persistence;
using Stripe;

namespace Payments.Infrastructure.Configuration;

public static class DependencyInjection
{
    public static IServiceCollection AddInfrastructure(
        this IServiceCollection services,
        IConfiguration configuration)
    {
        // Cassandra
        var cassandraContactPoints = configuration["Cassandra:ContactPoints"] ?? "localhost";
        var cassandraKeyspace = configuration["Cassandra:Keyspace"] ?? "payments";
        var session = CassandraConfiguration.CreateSession(cassandraContactPoints, cassandraKeyspace);
        services.AddSingleton<ISession>(session);
        services.AddScoped<IPaymentRepository, PaymentRepository>();
        services.AddScoped<PaymentDbContext>(sp => new PaymentDbContext(session, cassandraKeyspace));

        // Stripe
        var stripeApiKey = configuration["Stripe:ApiKey"];
        if (!string.IsNullOrEmpty(stripeApiKey))
        {
            StripeConfiguration.ApiKey = stripeApiKey;
        }
        services.AddScoped<PaymentIntentService>();
        services.AddScoped<IPaymentAuthorizationService, StripePaymentAuthorizationService>();

        // Kafka
        var kafkaConfig = new ProducerConfig
        {
            BootstrapServers = configuration["Kafka:BootstrapServers"] ?? "localhost:9092",
            Acks = Acks.All,
            EnableIdempotence = true
        };
        services.AddSingleton<IProducer<string, string>>(sp =>
            new ProducerBuilder<string, string>(kafkaConfig).Build());
        services.AddScoped<IEventPublisher, KafkaEventPublisher>();

        // MediatR
        services.AddMediatR(cfg => cfg.RegisterServicesFromAssembly(typeof(CreatePaymentCommandHandler).Assembly));

        return services;
    }
}
