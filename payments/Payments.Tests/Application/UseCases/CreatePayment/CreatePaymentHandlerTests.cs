using Xunit;
using FluentAssertions;
using Moq;
using Payments.Application.DTOs;
using Payments.Application.UseCases.CreatePayment;
using Payments.Domain.Entities;
using Payments.Domain.Enums;
using Payments.Domain.Events;
using Payments.Domain.Repositories;
using Payments.Domain.Services;
using Payments.Domain.ValueObjects;

namespace Payments.Tests.Application.UseCases.CreatePayment;

public class CreatePaymentHandlerTests
{
    private readonly Mock<IPaymentRepository> _repositoryMock;
    private readonly Mock<IPaymentAuthorizationService> _authorizationServiceMock;
    private readonly Mock<IEventPublisher> _eventPublisherMock;
    private readonly CreatePaymentHandler _handler;

    public CreatePaymentHandlerTests()
    {
        _repositoryMock = new Mock<IPaymentRepository>();
        _authorizationServiceMock = new Mock<IPaymentAuthorizationService>();
        _eventPublisherMock = new Mock<IEventPublisher>();
        
        _handler = new CreatePaymentHandler(
            _repositoryMock.Object,
            _authorizationServiceMock.Object,
            _eventPublisherMock.Object);
    }

    [Fact]
    public async Task Handle_WithValidCommand_ShouldCreateAndCompletePayment()
    {
        // Arrange
        var command = new CreatePaymentCommand(
            100.50m,
            "USD",
            "order-123",
            "customer-456",
            "pm_1234567890");

        _repositoryMock.Setup(r => r.GetByOrderIdAsync(command.OrderId, It.IsAny<CancellationToken>()))
            .ReturnsAsync((Payment?)null);

        _authorizationServiceMock.Setup(s => s.AuthorizePaymentAsync(
                It.IsAny<Money>(),
                command.OrderId,
                command.CustomerId,
                command.PaymentMethodId,
                It.IsAny<CancellationToken>()))
            .ReturnsAsync(new PaymentAuthorizationResult("pi_1234567890", true));

        _repositoryMock.Setup(r => r.SaveAsync(It.IsAny<Payment>(), It.IsAny<CancellationToken>()))
            .Returns(Task.CompletedTask);

        _repositoryMock.Setup(r => r.UpdateAsync(It.IsAny<Payment>(), It.IsAny<CancellationToken>()))
            .Returns(Task.CompletedTask);

        _eventPublisherMock.Setup(e => e.PublishAsync(It.IsAny<IDomainEvent>(), It.IsAny<CancellationToken>()))
            .Returns(Task.CompletedTask);

        // Act
        var result = await _handler.Handle(command, CancellationToken.None);

        // Assert
        result.Should().NotBeNull();
        result.Amount.Should().Be(100.50m);
        result.Currency.Should().Be("USD");
        result.OrderId.Should().Be("order-123");
        result.Status.Should().Be(PaymentStatus.Completed.ToString());
        result.StripePaymentIntentId.Should().Be("pi_1234567890");

        _repositoryMock.Verify(r => r.SaveAsync(It.IsAny<Payment>(), It.IsAny<CancellationToken>()), Times.Once);
        _repositoryMock.Verify(r => r.UpdateAsync(It.IsAny<Payment>(), It.IsAny<CancellationToken>()), Times.Once);
        _authorizationServiceMock.Verify(s => s.AuthorizePaymentAsync(
            It.IsAny<Money>(),
            command.OrderId,
            command.CustomerId,
            command.PaymentMethodId,
            It.IsAny<CancellationToken>()), Times.Once);
    }

    [Fact]
    public async Task Handle_WhenPaymentAlreadyExists_ShouldThrowException()
    {
        // Arrange
        var command = new CreatePaymentCommand(
            100m,
            "USD",
            "order-123",
            "customer-456",
            "pm_1234567890");

        var existingPayment = Payment.Create(new Money(100m, "USD"), "order-123", "customer-456");
        existingPayment.MarkAsProcessing("pi_existing");
        existingPayment.MarkAsCompleted();

        _repositoryMock.Setup(r => r.GetByOrderIdAsync(command.OrderId, It.IsAny<CancellationToken>()))
            .ReturnsAsync(existingPayment);

        // Act & Assert
        var act = async () => await _handler.Handle(command, CancellationToken.None);
        await act.Should().ThrowAsync<InvalidOperationException>()
            .WithMessage("*already completed*");
    }

    [Fact]
    public async Task Handle_WhenAuthorizationFails_ShouldMarkAsFailedAndThrow()
    {
        // Arrange
        var command = new CreatePaymentCommand(
            100m,
            "USD",
            "order-123",
            "customer-456",
            "pm_1234567890");

        _repositoryMock.Setup(r => r.GetByOrderIdAsync(command.OrderId, It.IsAny<CancellationToken>()))
            .ReturnsAsync((Payment?)null);

        _authorizationServiceMock.Setup(s => s.AuthorizePaymentAsync(
                It.IsAny<Money>(),
                command.OrderId,
                command.CustomerId,
                command.PaymentMethodId,
                It.IsAny<CancellationToken>()))
            .ReturnsAsync(new PaymentAuthorizationResult("", false, "Insufficient funds"));

        _repositoryMock.Setup(r => r.SaveAsync(It.IsAny<Payment>(), It.IsAny<CancellationToken>()))
            .Returns(Task.CompletedTask);

        // Act & Assert
        var act = async () => await _handler.Handle(command, CancellationToken.None);
        await act.Should().ThrowAsync<InvalidOperationException>()
            .WithMessage("*Insufficient funds*");

        _repositoryMock.Verify(r => r.SaveAsync(
            It.Is<Payment>(p => p.Status == PaymentStatus.Failed),
            It.IsAny<CancellationToken>()), Times.Once);
    }
}
