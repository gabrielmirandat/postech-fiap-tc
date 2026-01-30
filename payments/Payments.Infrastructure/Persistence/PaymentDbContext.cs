using Cassandra;

namespace Payments.Infrastructure.Persistence;

public class PaymentDbContext
{
    private readonly ISession _session;
    private readonly string _keyspace;

    public PaymentDbContext(ISession session, string keyspace)
    {
        _session = session;
        _keyspace = keyspace;
    }

    public ISession Session => _session;

    public void EnsureCreated()
    {
        // Keyspace and table creation is handled in CassandraConfiguration
        // This method can be used for additional setup if needed
    }
}
