package behavior.com.gabriel.orders.core.application;

import com.gabriel.orders.OrdersApplication;
import com.gabriel.orders.core.application.usecase.CreateOrderUseCase;
import com.gabriel.orders.core.domain.port.MenuRepository;
import com.gabriel.orders.core.domain.port.OrderPublisher;
import com.gabriel.orders.core.domain.port.OrderRepository;
import com.gabriel.orders.infra.grpc.MenuGrpcClientConfig;
import com.gabriel.orders.infra.kafka.KafkaConfig;
import com.gabriel.orders.infra.mongodb.MongoDbConfig;
import com.gabriel.orders.infra.redis.RedisConfig;
import com.gabriel.orders.infra.serializer.SerializerConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import utils.com.gabriel.orders.adapter.container.GrpcServerTestContainer;
import utils.com.gabriel.orders.adapter.container.KafkaTestContainer;
import utils.com.gabriel.orders.adapter.container.MongoDBTestContainer;
import utils.com.gabriel.orders.adapter.container.RedisTestContainer;
import utils.com.gabriel.orders.infra.SecurityConfig;

@Import({MongoDbConfig.class, MenuGrpcClientConfig.class, RedisConfig.class, KafkaConfig.class, SerializerConfig.class, SecurityConfig.class})
@ContextConfiguration(classes = {OrdersApplication.class, MongoDBTestContainer.class, GrpcServerTestContainer.class, RedisTestContainer.class, KafkaTestContainer.class})
@CucumberContextConfiguration
@SpringBootTest
@ActiveProfiles("test")
public class CucumberSpringConfiguration {

    @Autowired
    protected CreateOrderUseCase createOrderUseCase;

    @Autowired
    protected OrderPublisher orderPublisher;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        MongoDBTestContainer.mongoProperties(registry);
        GrpcServerTestContainer.grpcProperties(registry);
        RedisTestContainer.redisProperties(registry);
        KafkaTestContainer.kafkaProperties(registry);
    }
}
