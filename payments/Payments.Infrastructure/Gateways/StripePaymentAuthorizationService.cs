using Stripe;
using Microsoft.Extensions.Logging;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using Payments.Domain.Services;
using Payments.Domain.ValueObjects;

namespace Payments.Infrastructure.Gateways;

public class StripePaymentAuthorizationService : IPaymentAuthorizationService
{
    private readonly PaymentIntentService _paymentIntentService;
    private readonly ILogger<StripePaymentAuthorizationService> _logger;

    public StripePaymentAuthorizationService(
        PaymentIntentService paymentIntentService,
        ILogger<StripePaymentAuthorizationService> logger)
    {
        _paymentIntentService = paymentIntentService;
        _logger = logger;
    }

    public async Task<PaymentAuthorizationResult> AuthorizePaymentAsync(
        Money amount,
        string orderId,
        string customerId,
        string paymentMethodId,
        CancellationToken cancellationToken = default)
    {
        try
        {
            var options = new PaymentIntentCreateOptions
            {
                Amount = (long)(amount.Amount * 100), // Convert to cents
                Currency = amount.Currency.ToLower(),
                Customer = customerId,
                PaymentMethod = paymentMethodId,
                ConfirmationMethod = "manual",
                Confirm = true,
                Metadata = new Dictionary<string, string> { { "order_id", orderId } }
            };

            var paymentIntent = await _paymentIntentService.CreateAsync(options, cancellationToken: cancellationToken);
            
            _logger.LogInformation("Payment intent created: {PaymentIntentId} for order {OrderId}", 
                paymentIntent.Id, orderId);

            return new PaymentAuthorizationResult(paymentIntent.Id, paymentIntent.Status == "succeeded");
        }
        catch (StripeException ex)
        {
            _logger.LogError(ex, "Stripe error authorizing payment for order {OrderId}", orderId);
            return new PaymentAuthorizationResult(null, false, ex.Message);
        }
    }
}
