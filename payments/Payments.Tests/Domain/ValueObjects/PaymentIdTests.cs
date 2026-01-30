using Xunit;
using FluentAssertions;
using Payments.Domain.ValueObjects;

namespace Payments.Tests.Domain.ValueObjects;

public class PaymentIdTests
{
    [Fact]
    public void Create_ShouldGenerateNewGuid()
    {
        // Act
        var paymentId1 = PaymentId.Create();
        var paymentId2 = PaymentId.Create();

        // Assert
        paymentId1.Value.Should().NotBeEmpty();
        paymentId2.Value.Should().NotBeEmpty();
        paymentId1.Value.Should().NotBe(paymentId2.Value);
    }

    [Fact]
    public void From_WithValidGuid_ShouldCreatePaymentId()
    {
        // Arrange
        var guid = Guid.NewGuid();

        // Act
        var paymentId = PaymentId.From(guid);

        // Assert
        paymentId.Value.Should().Be(guid);
    }

    [Fact]
    public void From_WithEmptyGuid_ShouldThrowException()
    {
        // Act & Assert
        var act = () => PaymentId.From(Guid.Empty);
        act.Should().Throw<ArgumentException>()
            .WithMessage("*empty*");
    }

    [Fact]
    public void From_WithValidString_ShouldCreatePaymentId()
    {
        // Arrange
        var guid = Guid.NewGuid();
        var guidString = guid.ToString();

        // Act
        var paymentId = PaymentId.From(guidString);

        // Assert
        paymentId.Value.Should().Be(guid);
    }

    [Fact]
    public void From_WithInvalidString_ShouldThrowException()
    {
        // Act & Assert
        var act = () => PaymentId.From("invalid-guid");
        act.Should().Throw<ArgumentException>()
            .WithMessage("*format*");
    }

    [Fact]
    public void From_WithEmptyString_ShouldThrowException()
    {
        // Act & Assert
        var act = () => PaymentId.From("");
        act.Should().Throw<ArgumentException>()
            .WithMessage("*empty*");
    }

    [Fact]
    public void Equals_WithSameValue_ShouldReturnTrue()
    {
        // Arrange
        var guid = Guid.NewGuid();
        var paymentId1 = PaymentId.From(guid);
        var paymentId2 = PaymentId.From(guid);

        // Act & Assert
        paymentId1.Equals(paymentId2).Should().BeTrue();
    }

    [Fact]
    public void ImplicitConversion_ToGuid_ShouldWork()
    {
        // Arrange
        var guid = Guid.NewGuid();
        var paymentId = PaymentId.From(guid);

        // Act
        Guid result = paymentId;

        // Assert
        result.Should().Be(guid);
    }

    [Fact]
    public void ImplicitConversion_FromGuid_ShouldWork()
    {
        // Arrange
        var guid = Guid.NewGuid();

        // Act
        PaymentId paymentId = guid;

        // Assert
        paymentId.Value.Should().Be(guid);
    }

    [Fact]
    public void ToString_ShouldReturnGuidString()
    {
        // Arrange
        var guid = Guid.NewGuid();
        var paymentId = PaymentId.From(guid);

        // Act
        var result = paymentId.ToString();

        // Assert
        result.Should().Be(guid.ToString());
    }
}
