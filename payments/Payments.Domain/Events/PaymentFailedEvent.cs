using Payments.Domain.ValueObjects;

namespace Payments.Domain.Events;

public record PaymentFailedEvent(
    PaymentId PaymentId,
    string OrderId,
    string Reason
) : IDomainEvent
{
    public DateTime OccurredOn { get; } = DateTime.UtcNow;
}
