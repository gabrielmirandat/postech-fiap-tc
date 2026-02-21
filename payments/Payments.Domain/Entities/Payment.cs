using System;
using System.Collections.Generic;
using System.Linq;
using Payments.Domain.ValueObjects;
using Payments.Domain.Events;
using Payments.Domain.Enums;

namespace Payments.Domain.Entities;

public class Payment
{
    public PaymentId Id { get; private set; } = null!;
    public Money Amount { get; private set; } = null!;
    public string OrderId { get; private set; } = null!;
    public string CustomerId { get; private set; } = null!;
    public PaymentStatus Status { get; private set; }
    public string? StripePaymentIntentId { get; private set; }
    public DateTime CreatedAt { get; private set; }
    public DateTime? ProcessedAt { get; private set; }
    
    private readonly List<IDomainEvent> _domainEvents = new();
    public IReadOnlyCollection<IDomainEvent> DomainEvents => _domainEvents.AsReadOnly();

    // Private parameterless constructor for reconstruction
    private Payment() { }

    private Payment(Money amount, string orderId, string customerId)
    {
        Id = PaymentId.Create();
        Amount = amount;
        OrderId = orderId;
        CustomerId = customerId;
        Status = PaymentStatus.Pending;
        CreatedAt = DateTime.UtcNow;
    }

    // Private constructor for reconstruction from persistence
    private Payment(
        PaymentId id,
        Money amount,
        string orderId,
        string customerId,
        PaymentStatus status,
        string? stripePaymentIntentId,
        DateTime createdAt,
        DateTime? processedAt)
    {
        Id = id;
        Amount = amount;
        OrderId = orderId;
        CustomerId = customerId;
        Status = status;
        StripePaymentIntentId = stripePaymentIntentId;
        CreatedAt = createdAt;
        ProcessedAt = processedAt;
    }

    public static Payment Create(Money amount, string orderId, string customerId)
    {
        var payment = new Payment(amount, orderId, customerId);
        payment._domainEvents.Add(new PaymentCreatedEvent(payment.Id, payment.OrderId, payment.Amount));
        return payment;
    }

    // Factory method for reconstruction from persistence
    public static Payment Reconstruct(
        PaymentId id,
        Money amount,
        string orderId,
        string customerId,
        PaymentStatus status,
        string? stripePaymentIntentId,
        DateTime createdAt,
        DateTime? processedAt)
    {
        return new Payment(id, amount, orderId, customerId, status, stripePaymentIntentId, createdAt, processedAt);
    }

    public void MarkAsProcessing(string? stripePaymentIntentId)
    {
        if (Status != PaymentStatus.Pending)
            throw new InvalidOperationException($"Cannot process payment in {Status} status");

        StripePaymentIntentId = stripePaymentIntentId;
        Status = PaymentStatus.Processing;
    }

    public void MarkAsCompleted()
    {
        if (Status != PaymentStatus.Processing)
            throw new InvalidOperationException($"Cannot complete payment in {Status} status");

        Status = PaymentStatus.Completed;
        ProcessedAt = DateTime.UtcNow;
        _domainEvents.Add(new PaymentCompletedEvent(Id, OrderId, Amount));
    }

    public void MarkAsFailed(string reason)
    {
        if (Status == PaymentStatus.Completed)
            throw new InvalidOperationException("Cannot fail a completed payment");

        Status = PaymentStatus.Failed;
        ProcessedAt = DateTime.UtcNow;
        _domainEvents.Add(new PaymentFailedEvent(Id, OrderId, reason));
    }

    public void ClearDomainEvents()
    {
        _domainEvents.Clear();
    }
}

