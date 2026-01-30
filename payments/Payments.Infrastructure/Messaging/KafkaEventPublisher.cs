using Confluent.Kafka;
using System.Text.Json;
using Microsoft.Extensions.Logging;
using Payments.Domain.Events;
using Payments.Infrastructure.Messaging.Mappers;

namespace Payments.Infrastructure.Messaging;

public class KafkaEventPublisher : IEventPublisher
{
    private readonly IProducer<string, string> _producer;
    private readonly ILogger<KafkaEventPublisher> _logger;
    private const string TopicPrefix = "payment-events";

    public KafkaEventPublisher(
        IProducer<string, string> producer,
        ILogger<KafkaEventPublisher> logger)
    {
        _producer = producer;
        _logger = logger;
    }

    public async Task PublishAsync<T>(T domainEvent, CancellationToken cancellationToken = default) where T : IDomainEvent
    {
        try
        {
            var eventType = typeof(T).Name;
            var topic = $"{TopicPrefix}-{eventType.ToLowerInvariant()}";
            
            var message = CloudEventMapper.ToCloudEvent(domainEvent);
            var json = JsonSerializer.Serialize(message);

            var kafkaMessage = new Message<string, string>
            {
                Key = GetEventKey(domainEvent),
                Value = json,
                Headers = new Headers
                {
                    { "event-type", System.Text.Encoding.UTF8.GetBytes(eventType) },
                    { "content-type", System.Text.Encoding.UTF8.GetBytes("application/json") }
                }
            };

            await _producer.ProduceAsync(topic, kafkaMessage, cancellationToken);
            
            _logger.LogInformation(
                "Published event {EventType} to topic {Topic}",
                eventType,
                topic);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error publishing event {EventType}", typeof(T).Name);
            throw;
        }
    }

    private string GetEventKey(IDomainEvent domainEvent)
    {
        return domainEvent switch
        {
            PaymentCreatedEvent e => e.OrderId,
            PaymentCompletedEvent e => e.OrderId,
            PaymentFailedEvent e => e.OrderId,
            _ => Guid.NewGuid().ToString()
        };
    }
}
