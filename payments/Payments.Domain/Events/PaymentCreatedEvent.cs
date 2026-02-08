using System;
using Payments.Domain.ValueObjects;

namespace Payments.Domain.Events;

public record PaymentCreatedEvent(
    PaymentId PaymentId,
    string OrderId,
    Money Amount
) : IDomainEvent
{
    public DateTime OccurredOn { get; } = DateTime.UtcNow;
}
