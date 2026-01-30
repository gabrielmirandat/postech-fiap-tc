using Cassandra;

namespace Payments.Infrastructure.Configuration;

public static class CassandraConfiguration
{
    public static ISession CreateSession(string contactPoints, string keyspace)
    {
        var cluster = Cluster.Builder()
            .AddContactPoints(contactPoints.Split(','))
            .WithDefaultKeyspace(keyspace)
            .Build();

        var session = cluster.Connect();
        
        // Create keyspace if it doesn't exist
        session.Execute(@"
            CREATE KEYSPACE IF NOT EXISTS payments
            WITH REPLICATION = {
                'class': 'SimpleStrategy',
                'replication_factor': 1
            }");

        session = cluster.Connect("payments");
        
        // Create table if it doesn't exist
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
}
