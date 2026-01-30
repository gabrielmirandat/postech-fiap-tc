using Payments.Domain.ValueObjects;

namespace Payments.Application.UseCases.CreatePayment;

public record CreatePaymentCommand(
    decimal Amount,
    string Currency,
    string OrderId,
    string CustomerId,
    string PaymentMethodId
)
{
    public Money ToMoney() => new(Amount, Currency);
}
