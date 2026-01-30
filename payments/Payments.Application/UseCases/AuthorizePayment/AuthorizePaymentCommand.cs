using Payments.Domain.ValueObjects;

namespace Payments.Application.UseCases.AuthorizePayment;

public record AuthorizePaymentCommand(
    PaymentId PaymentId,
    string PaymentMethodId
);
