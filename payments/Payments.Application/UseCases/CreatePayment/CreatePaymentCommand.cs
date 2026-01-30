using MediatR;
using Payments.Application.DTOs;
using Payments.Domain.ValueObjects;

namespace Payments.Application.UseCases.CreatePayment;

public record CreatePaymentCommand(
    decimal Amount,
    string Currency,
    string OrderId,
    string CustomerId,
    string PaymentMethodId
) : IRequest<PaymentDto>
{
    public Money ToMoney() => new(Amount, Currency);
}
