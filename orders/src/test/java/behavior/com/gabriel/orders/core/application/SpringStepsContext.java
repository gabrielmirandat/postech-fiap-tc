package behavior.com.gabriel.orders.core.application;

import behavior.com.gabriel.orders.core.application.StateConfiguration.StateManager;
import com.gabriel.orders.OrdersApplication;
import com.gabriel.orders.core.domain.port.MenuRepository;
import com.gabriel.orders.core.domain.port.MenuSubscriber;
import com.gabriel.orders.core.domain.port.OrderPublisher;
import com.gabriel.orders.core.domain.port.OrderRepository;
import com.gabriel.orders.infra.grpc.MenuGrpcClientConfiguration;
import com.gabriel.orders.infra.kafka.KafkaConfiguration;
import com.gabriel.orders.infra.mongodb.MongoConfiguration;
import com.gabriel.orders.infra.redis.RedisConfiguration;
import com.gabriel.orders.infra.serializer.SerializerConfiguration;
import io.cloudevents.CloudEvent;
import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import utils.com.gabriel.orders.adapter.container.GrpcServerTestContainer;
import utils.com.gabriel.orders.adapter.container.KafkaTestContainer;
import utils.com.gabriel.orders.adapter.container.MongoDBTestContainer;
import utils.com.gabriel.orders.adapter.container.RedisTestContainer;
import utils.com.gabriel.orders.infra.TestSecurityConfiguration;

@Import({
    MongoConfiguration.class,
    MenuGrpcClientConfiguration.class,
    RedisConfiguration.class,
    KafkaConfiguration.class,
    SerializerConfiguration.class,
    TestSecurityConfiguration.class,
    StateConfiguration.class
})
@ContextConfiguration(classes = {
    OrdersApplication.class,
    MongoDBTestContainer.class,
    GrpcServerTestContainer.class,
    RedisTestContainer.class,
    KafkaTestContainer.class
})
@ActiveProfiles("test")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringStepsContext {

    @Autowired
    protected Consumer<String, CloudEvent> consumer;
    @Autowired
    protected ConsumerFactory<String, CloudEvent> consumerFactory;
    @Autowired
    protected OrderPublisher orderPublisher;
    @Autowired
    protected MenuSubscriber menuSubscriber;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected MenuRepository menuRepository;
    @Autowired
    protected StateManager stateManager;

    @LocalServerPort
    protected int port;

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        MongoDBTestContainer.mongoProperties(registry);
        GrpcServerTestContainer.grpcProperties(registry);
        RedisTestContainer.redisProperties(registry);
        KafkaTestContainer.kafkaProperties(registry);
    }
}
