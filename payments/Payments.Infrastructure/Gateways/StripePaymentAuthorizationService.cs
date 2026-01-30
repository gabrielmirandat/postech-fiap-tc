using Stripe;
using Microsoft.Extensions.Logging;
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
                Currency = amount.Currency.ToLowerInvariant(),
                Customer = customerId,
                PaymentMethod = paymentMethodId,
                ConfirmationMethod = "manual",
                Confirm = true,
                Metadata = new Dictionary<string, string>
                {
                    { "order_id", orderId }
                }
            };

            var paymentIntent = await _paymentIntentService.CreateAsync(options, cancellationToken: cancellationToken);

            if (paymentIntent.Status == "succeeded" || paymentIntent.Status == "requires_capture")
            {
                _logger.LogInformation(
                    "Payment authorized successfully. PaymentIntent: {PaymentIntentId}, Order: {OrderId}",
                    paymentIntent.Id,
                    orderId);

                return new PaymentAuthorizationResult(paymentIntent.Id, true);
            }

            _logger.LogWarning(
                "Payment authorization incomplete. PaymentIntent: {PaymentIntentId}, Status: {Status}, Order: {OrderId}",
                paymentIntent.Id,
                paymentIntent.Status,
                orderId);

            return new PaymentAuthorizationResult(
                paymentIntent.Id,
                false,
                $"Payment status: {paymentIntent.Status}");
        }
        catch (StripeException ex)
        {
            _logger.LogError(ex, "Stripe error authorizing payment for order {OrderId}", orderId);
            return new PaymentAuthorizationResult(
                string.Empty,
                false,
                ex.Message);
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Unexpected error authorizing payment for order {OrderId}", orderId);
            return new PaymentAuthorizationResult(
                string.Empty,
                false,
                "An unexpected error occurred while authorizing the payment");
        }
    }
}
