using Xunit;
using FluentAssertions;
using Payments.Domain.Entities;
using Payments.Domain.Enums;
using Payments.Domain.ValueObjects;
using Payments.Domain.Events;

namespace Payments.Tests.Domain.Entities;

public class PaymentTests
{
    [Fact]
    public void Create_ShouldCreatePaymentWithPendingStatus()
    {
        // Arrange
        var amount = new Money(100.50m, "USD");
        var orderId = "order-123";
        var customerId = "customer-456";

        // Act
        var payment = Payment.Create(amount, orderId, customerId);

        // Assert
        payment.Should().NotBeNull();
        payment.Status.Should().Be(PaymentStatus.Pending);
        payment.Amount.Should().Be(amount);
        payment.OrderId.Should().Be(orderId);
        payment.CustomerId.Should().Be(customerId);
        payment.CreatedAt.Should().BeCloseTo(DateTime.UtcNow, TimeSpan.FromSeconds(1));
        payment.DomainEvents.Should().ContainSingle()
            .Which.Should().BeOfType<PaymentCreatedEvent>();
    }

    [Fact]
    public void MarkAsProcessing_ShouldUpdateStatusAndStripeId()
    {
        // Arrange
        var payment = Payment.Create(new Money(100m, "USD"), "order-123", "customer-456");
        var stripePaymentIntentId = "pi_1234567890";

        // Act
        payment.MarkAsProcessing(stripePaymentIntentId);

        // Assert
        payment.Status.Should().Be(PaymentStatus.Processing);
        payment.StripePaymentIntentId.Should().Be(stripePaymentIntentId);
    }

    [Fact]
    public void MarkAsProcessing_WhenNotPending_ShouldThrowException()
    {
        // Arrange
        var payment = Payment.Create(new Money(100m, "USD"), "order-123", "customer-456");
        payment.MarkAsProcessing("pi_123");
        payment.MarkAsCompleted();

        // Act & Assert
        var act = () => payment.MarkAsProcessing("pi_456");
        act.Should().Throw<InvalidOperationException>()
            .WithMessage("*Completed*");
    }

    [Fact]
    public void MarkAsCompleted_ShouldUpdateStatusAndAddEvent()
    {
        // Arrange
        var payment = Payment.Create(new Money(100m, "USD"), "order-123", "customer-456");
        payment.MarkAsProcessing("pi_123");

        // Act
        payment.MarkAsCompleted();

        // Assert
        payment.Status.Should().Be(PaymentStatus.Completed);
        payment.ProcessedAt.Should().BeCloseTo(DateTime.UtcNow, TimeSpan.FromSeconds(1));
        payment.DomainEvents.Should().Contain(e => e is PaymentCompletedEvent);
    }

    [Fact]
    public void MarkAsCompleted_WhenNotProcessing_ShouldThrowException()
    {
        // Arrange
        var payment = Payment.Create(new Money(100m, "USD"), "order-123", "customer-456");

        // Act & Assert
        var act = () => payment.MarkAsCompleted();
        act.Should().Throw<InvalidOperationException>()
            .WithMessage("*Pending*");
    }

    [Fact]
    public void MarkAsFailed_ShouldUpdateStatusAndAddEvent()
    {
        // Arrange
        var payment = Payment.Create(new Money(100m, "USD"), "order-123", "customer-456");
        var reason = "Insufficient funds";

        // Act
        payment.MarkAsFailed(reason);

        // Assert
        payment.Status.Should().Be(PaymentStatus.Failed);
        payment.ProcessedAt.Should().BeCloseTo(DateTime.UtcNow, TimeSpan.FromSeconds(1));
        payment.DomainEvents.Should().Contain(e => e is PaymentFailedEvent);
    }

    [Fact]
    public void MarkAsFailed_WhenCompleted_ShouldThrowException()
    {
        // Arrange
        var payment = Payment.Create(new Money(100m, "USD"), "order-123", "customer-456");
        payment.MarkAsProcessing("pi_123");
        payment.MarkAsCompleted();

        // Act & Assert
        var act = () => payment.MarkAsFailed("Error");
        act.Should().Throw<InvalidOperationException>()
            .WithMessage("*completed*");
    }

    [Fact]
    public void ClearDomainEvents_ShouldRemoveAllEvents()
    {
        // Arrange
        var payment = Payment.Create(new Money(100m, "USD"), "order-123", "customer-456");
        payment.DomainEvents.Should().NotBeEmpty();

        // Act
        payment.ClearDomainEvents();

        // Assert
        payment.DomainEvents.Should().BeEmpty();
    }
}
