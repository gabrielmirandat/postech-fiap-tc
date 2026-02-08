using Cassandra;
using System;

namespace Payments.Infrastructure.Configuration;

public static class CassandraConfiguration
{
    private const int MaxRetries = 12;
    private static readonly TimeSpan RetryDelay = TimeSpan.FromSeconds(5);

    public static ISession CreateSession(string contactPoints, string keyspace)
    {
        var cluster = Cluster.Builder()
            .AddContactPoints(contactPoints.Split(','))
            .Build();

        return ConnectWithRetry(cluster, keyspace);
    }

    private static ISession ConnectWithRetry(ICluster cluster, string keyspace)
    {
        for (int i = 0; i < MaxRetries; i++)
        {
            try
            {
                var session = cluster.Connect();
                session.Execute($"CREATE KEYSPACE IF NOT EXISTS {keyspace} WITH REPLICATION = {{ 'class' : 'SimpleStrategy', 'replication_factor' : 1 }}");
                session.Execute($"USE {keyspace}");
                session.Execute(@"
                    CREATE TABLE IF NOT EXISTS payments (
                        id UUID PRIMARY KEY,
                        amount DECIMAL,
                        currency TEXT,
                        order_id TEXT,
                        customer_id TEXT,
                        status TEXT,
                        stripe_payment_intent_id TEXT,
                        created_at TIMESTAMP,
                        processed_at TIMESTAMP
                    )");
                return session;
            }
            catch (Exception)
            {
                if (i == MaxRetries - 1) throw;
                System.Threading.Thread.Sleep(RetryDelay);
            }
        }
        throw new InvalidOperationException("Failed to connect to Cassandra after retries");
    }
}
