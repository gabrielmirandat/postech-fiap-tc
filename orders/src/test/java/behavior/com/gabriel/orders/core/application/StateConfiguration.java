package behavior.com.gabriel.orders.core.application;

import com.gabriel.orders.core.domain.model.Order;
import io.cloudevents.CloudEvent;
import io.restassured.response.Response;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

@Configuration
public class StateConfiguration {

    @Bean
    public StateManager stateManager() {
        return new StateManager();
    }

    public static class StateManager {

        private String AUTH_TOKEN;
        private Response RESPONSE;
        private String GENERATED_TICKET_ID;
        private Order GENERATED_ORDER;
        private ConsumerRecord<String, CloudEvent> RECORDED_EVENT;

        public Object get(String variableName) {
            try {
                Field field = this.getClass().getDeclaredField(variableName);
                field.setAccessible(true);
                return field.get(this);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Variable " + variableName + " does not exist or is not accessible.", e);
            }
        }

        public void set(String variableName, Object value) {
            try {
                Field field = this.getClass().getDeclaredField(variableName);
                field.setAccessible(true);
                field.set(this, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Variable " + variableName + " does not exist or is not accessible.", e);
            }
        }
    }
}
