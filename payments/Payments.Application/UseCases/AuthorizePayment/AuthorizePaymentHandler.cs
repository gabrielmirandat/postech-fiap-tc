using MediatR;
using Payments.Application.UseCases.AuthorizePayment;
using Payments.Domain.Repositories;
using Payments.Domain.Services;

namespace Payments.Application.UseCases.AuthorizePayment;

public class AuthorizePaymentHandler : IRequestHandler<AuthorizePaymentCommand, bool>
{
    private readonly IPaymentRepository _paymentRepository;
    private readonly IPaymentAuthorizationService _paymentAuthorizationService;

    public AuthorizePaymentHandler(
        IPaymentRepository paymentRepository,
        IPaymentAuthorizationService paymentAuthorizationService)
    {
        _paymentRepository = paymentRepository;
        _paymentAuthorizationService = paymentAuthorizationService;
    }

    public async Task<bool> Handle(AuthorizePaymentCommand request, CancellationToken cancellationToken)
    {
        var payment = await _paymentRepository.GetByIdAsync(request.PaymentId, cancellationToken);
        
        if (payment == null)
            throw new InvalidOperationException($"Payment {request.PaymentId} not found");

        var result = await _paymentAuthorizationService.AuthorizePaymentAsync(
            payment.Amount,
            payment.OrderId,
            payment.CustomerId,
            request.PaymentMethodId,
            cancellationToken);

        if (!result.Success)
        {
            payment.MarkAsFailed(result.ErrorMessage ?? "Payment authorization failed");
            await _paymentRepository.UpdateAsync(payment, cancellationToken);
            return false;
        }

        payment.MarkAsProcessing(result.PaymentIntentId);
        await _paymentRepository.UpdateAsync(payment, cancellationToken);

        return true;
    }
}
