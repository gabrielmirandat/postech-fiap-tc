namespace Payments.Application.DTOs;

public record PaymentDto(
    Guid Id,
    decimal Amount,
    string Currency,
    string OrderId,
    string CustomerId,
    string Status,
    string? StripePaymentIntentId,
    DateTime CreatedAt,
    DateTime? ProcessedAt
);
