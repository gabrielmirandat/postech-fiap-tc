using System;

namespace Payments.Domain.Events;

public interface IDomainEvent
{
    DateTime OccurredOn { get; }
}
