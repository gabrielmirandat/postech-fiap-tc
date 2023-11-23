package com.gabriel.orders.infra.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories
public class MongoDbConfig {

    @Value("${mongodb.conn.string}")
    private String mongodbConnString;

    @Value("${mongodb.conn.db}")
    private String mongodbConnDatabase;

    @Bean
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString(String.format("%s/admin", mongodbConnString));
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();

        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), mongodbConnDatabase);
    }
}
