using System.Threading;
using System.Threading.Tasks;
using Payments.Domain.Entities;
using Payments.Domain.ValueObjects;

namespace Payments.Domain.Repositories;

public interface IPaymentRepository
{
    Task<Payment?> GetByIdAsync(PaymentId id, CancellationToken cancellationToken = default);
    Task<Payment?> GetByOrderIdAsync(string orderId, CancellationToken cancellationToken = default);
    Task SaveAsync(Payment payment, CancellationToken cancellationToken = default);
    Task UpdateAsync(Payment payment, CancellationToken cancellationToken = default);
}
