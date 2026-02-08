using Cassandra;
using Microsoft.Extensions.Logging;
using System;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using Payments.Domain.Entities;
using Payments.Domain.Repositories;
using Payments.Domain.ValueObjects;
using Payments.Domain.Enums;

namespace Payments.Infrastructure.Persistence;

public class PaymentRepository : IPaymentRepository
{
    private readonly ISession _session;
    private readonly ILogger<PaymentRepository> _logger;

    public PaymentRepository(ISession session, ILogger<PaymentRepository> logger)
    {
        _session = session;
        _logger = logger;
    }

    public async Task<Payment?> GetByIdAsync(PaymentId id, CancellationToken cancellationToken = default)
    {
        var statement = _session.Prepare("SELECT * FROM payments WHERE id = ?");
        var boundStatement = statement.Bind(id.Value);
        var result = await _session.ExecuteAsync(boundStatement);
        var row = result.FirstOrDefault();
        
        return row != null ? MapToPayment(row) : null;
    }

    public async Task<Payment?> GetByOrderIdAsync(string orderId, CancellationToken cancellationToken = default)
    {
        var statement = _session.Prepare("SELECT * FROM payments WHERE order_id = ? ALLOW FILTERING");
        var boundStatement = statement.Bind(orderId);
        var result = await _session.ExecuteAsync(boundStatement);
        var row = result.FirstOrDefault();
        
        return row != null ? MapToPayment(row) : null;
    }

    public async Task SaveAsync(Payment payment, CancellationToken cancellationToken = default)
    {
        var statement = _session.Prepare(
            "INSERT INTO payments (id, amount, currency, order_id, customer_id, status, stripe_payment_intent_id, created_at, processed_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        var boundStatement = statement.Bind(
            payment.Id.Value,
            payment.Amount.Amount,
            payment.Amount.Currency,
            payment.OrderId,
            payment.CustomerId,
            payment.Status.ToString(),
            payment.StripePaymentIntentId,
            payment.CreatedAt,
            payment.ProcessedAt);
        
        await _session.ExecuteAsync(boundStatement);
    }

    public async Task UpdateAsync(Payment payment, CancellationToken cancellationToken = default)
    {
        var statement = _session.Prepare(
            "UPDATE payments SET amount = ?, currency = ?, status = ?, stripe_payment_intent_id = ?, processed_at = ? WHERE id = ?");
        var boundStatement = statement.Bind(
            payment.Amount.Amount,
            payment.Amount.Currency,
            payment.Status.ToString(),
            payment.StripePaymentIntentId,
            payment.ProcessedAt,
            payment.Id.Value);
        
        await _session.ExecuteAsync(boundStatement);
    }

    private Payment MapToPayment(Row row)
    {
        var paymentId = PaymentId.From(row.GetValue<Guid>("id"));
        var amount = new Money(
            row.GetValue<decimal>("amount"),
            row.GetValue<string>("currency"));
        var status = Enum.Parse<PaymentStatus>(row.GetValue<string>("status"));
        var stripePaymentIntentId = row.GetValue<string>("stripe_payment_intent_id");
        var createdAt = row.GetValue<DateTimeOffset>("created_at").DateTime;
        var processedAt = row.GetValue<DateTimeOffset?>("processed_at")?.DateTime;
        
        return Payment.Reconstruct(
            paymentId,
            amount,
            row.GetValue<string>("order_id"),
            row.GetValue<string>("customer_id"),
            status,
            stripePaymentIntentId,
            createdAt,
            processedAt);
    }
}
