using System;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using MediatR;
using Payments.Application.DTOs;
using Payments.Application.UseCases.CreatePayment;
using Payments.Domain.Entities;
using Payments.Domain.Repositories;
using Payments.Domain.Services;
using Payments.Domain.Events;

namespace Payments.Application.UseCases.CreatePayment;

public class CreatePaymentHandler : IRequestHandler<CreatePaymentCommand, PaymentDto>
{
    private readonly IPaymentRepository _paymentRepository;
    private readonly IPaymentAuthorizationService _paymentAuthorizationService;
    private readonly IEventPublisher _eventPublisher;

    public CreatePaymentHandler(
        IPaymentRepository paymentRepository,
        IPaymentAuthorizationService paymentAuthorizationService,
        IEventPublisher eventPublisher)
    {
        _paymentRepository = paymentRepository;
        _paymentAuthorizationService = paymentAuthorizationService;
        _eventPublisher = eventPublisher;
    }

    public async Task<PaymentDto> Handle(CreatePaymentCommand request, CancellationToken cancellationToken)
    {
        // Check if payment already exists for this order
        var existingPayment = await _paymentRepository.GetByOrderIdAsync(request.OrderId, cancellationToken);
        if (existingPayment != null && existingPayment.Status == Domain.Enums.PaymentStatus.Completed)
        {
            throw new InvalidOperationException($"Payment already completed for order {request.OrderId}");
        }

        // Create payment entity
        var payment = Payment.Create(request.ToMoney(), request.OrderId, request.CustomerId);

        // Authorize payment through service
        var authorizationResult = await _paymentAuthorizationService.AuthorizePaymentAsync(
            payment.Amount,
            payment.OrderId,
            payment.CustomerId,
            request.PaymentMethodId,
            cancellationToken);

        if (!authorizationResult.Success)
        {
            payment.MarkAsFailed(authorizationResult.ErrorMessage ?? "Payment authorization failed");
            await _paymentRepository.SaveAsync(payment, cancellationToken);
            throw new InvalidOperationException(authorizationResult.ErrorMessage ?? "Payment authorization failed");
        }

        payment.MarkAsProcessing(authorizationResult.PaymentIntentId);
        await _paymentRepository.SaveAsync(payment, cancellationToken);

        // Process payment completion
        payment.MarkAsCompleted();
        await _paymentRepository.UpdateAsync(payment, cancellationToken);

        // Publish domain events
        foreach (var domainEvent in payment.DomainEvents)
        {
            await _eventPublisher.PublishAsync(domainEvent, cancellationToken);
        }

        payment.ClearDomainEvents();

        return new PaymentDto(
            payment.Id.Value,
            payment.Amount.Amount,
            payment.Amount.Currency,
            payment.OrderId,
            payment.CustomerId,
            payment.Status.ToString(),
            payment.StripePaymentIntentId,
            payment.CreatedAt,
            payment.ProcessedAt
        );
    }
}
