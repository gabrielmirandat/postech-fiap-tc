using System.Text.Json;
using Payments.Domain.Events;

using System;

namespace Payments.Infrastructure.Messaging.Mappers;

public static class CloudEventMapper
{
    public static object ToCloudEvent(IDomainEvent domainEvent)
    {
        return new
        {
            specversion = "1.0",
            type = domainEvent.GetType().Name,
            source = "payments-service",
            id = Guid.NewGuid().ToString(),
            time = domainEvent.OccurredOn.ToString("O"),
            datacontenttype = "application/json",
            data = domainEvent
        };
    }
}
