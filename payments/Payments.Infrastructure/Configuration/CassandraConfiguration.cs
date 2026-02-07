using Cassandra;

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

        ISession session = ConnectWithRetry(cluster);

        // Create keyspace if it does not exist
        session.Execute($@"
            CREATE KEYSPACE IF NOT EXISTS {keyspace}
            WITH REPLICATION = {{
                'class': 'SimpleStrategy',
                'replication_factor': 1
            }}");

        // Reconnect using the created keyspace
        session = cluster.Connect(keyspace);

        // Create table if it does not exist
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

        // Create index for order_id lookups
        session.Execute(@"
            CREATE INDEX IF NOT EXISTS ON payments (order_id)");

        return session;
    }

    private static ISession ConnectWithRetry(ICluster cluster)
    {
        for (int attempt = 1; attempt <= MaxRetries; attempt++)
        {
            try
            {
                Console.WriteLine("Connecting to Cassandra...");
                return cluster.Connect();
            }
            catch (NoHostAvailableException)
            {
                Console.WriteLine(
                    $"Cassandra not ready yet (attempt {attempt}/{MaxRetries}). Retrying in {RetryDelay.TotalSeconds}s...");
                
                Thread.Sleep(RetryDelay);
            }
        }

        throw new Exception("Unable to connect to Cassandra after multiple retries.");
    }
}
