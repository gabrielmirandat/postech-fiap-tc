using System.Threading;
using System.Threading.Tasks;
using Payments.Domain.ValueObjects;

namespace Payments.Domain.Services;

public interface IPaymentAuthorizationService
{
    Task<PaymentAuthorizationResult> AuthorizePaymentAsync(
        Money amount,
        string orderId,
        string customerId,
        string paymentMethodId,
        CancellationToken cancellationToken = default);
}

public record PaymentAuthorizationResult(
    string? PaymentIntentId,
    bool Success,
    string? ErrorMessage = null
);
