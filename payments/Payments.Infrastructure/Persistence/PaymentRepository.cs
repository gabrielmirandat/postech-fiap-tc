using Cassandra;
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
        var statement = new SimpleStatement(
            "SELECT id, amount, currency, order_id, customer_id, status, stripe_payment_intent_id, created_at, processed_at " +
            "FROM payments WHERE id = ?",
            id.Value
        );

        var row = await _session.ExecuteAsync(statement);
        var result = row.FirstOrDefault();

        if (result == null)
            return null;

        return MapToPayment(result);
    }

    public async Task<Payment?> GetByOrderIdAsync(string orderId, CancellationToken cancellationToken = default)
    {
        var statement = new SimpleStatement(
            "SELECT id, amount, currency, order_id, customer_id, status, stripe_payment_intent_id, created_at, processed_at " +
            "FROM payments WHERE order_id = ? LIMIT 1",
            orderId
        );

        var row = await _session.ExecuteAsync(statement);
        var result = row.FirstOrDefault();

        if (result == null)
            return null;

        return MapToPayment(result);
    }

    public async Task SaveAsync(Payment payment, CancellationToken cancellationToken = default)
    {
        var statement = new SimpleStatement(
            "INSERT INTO payments (id, amount, currency, order_id, customer_id, status, stripe_payment_intent_id, created_at, processed_at) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
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

        await _session.ExecuteAsync(statement);
        _logger.LogInformation("Payment {PaymentId} saved to Cassandra", payment.Id);
    }

    public async Task UpdateAsync(Payment payment, CancellationToken cancellationToken = default)
    {
        var statement = new SimpleStatement(
            "UPDATE payments SET status = ?, stripe_payment_intent_id = ?, processed_at = ? WHERE id = ?",
            payment.Status.ToString(),
            payment.StripePaymentIntentId,
            payment.ProcessedAt,
            payment.Id.Value
        );

        await _session.ExecuteAsync(statement);
        _logger.LogInformation("Payment {PaymentId} updated in Cassandra", payment.Id);
    }

    private Payment MapToPayment(IRow row)
    {
        var amount = new Money(
            row.GetValue<decimal>("amount"),
            row.GetValue<string>("currency")
        );

        var paymentId = PaymentId.From(row.GetValue<Guid>("id"));
        var orderId = row.GetValue<string>("order_id");
        var customerId = row.GetValue<string>("customer_id");
        var statusStr = row.GetValue<string>("status");
        var stripeId = row.GetValue<string?>("stripe_payment_intent_id");
        var createdAt = row.GetValue<DateTime>("created_at");
        var processedAt = row.GetValue<DateTime?>("processed_at");

        var status = Enum.Parse<PaymentStatus>(statusStr);

        return Payment.Reconstruct(
            paymentId,
            amount,
            orderId,
            customerId,
            status,
            stripeId,
            createdAt,
            processedAt
        );
    }
}
