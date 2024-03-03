package integration.com.gabriel.orders.adapter.driven.api;

import com.gabriel.core.domain.model.Name;
import com.gabriel.core.domain.model.Price;
import com.gabriel.core.domain.model.id.IngredientID;
import com.gabriel.core.domain.model.id.ProductID;
import com.gabriel.orders.adapter.driven.api.MenuGrpcClient;
import com.gabriel.orders.core.application.usecase.SetupMenuUseCase;
import com.gabriel.orders.core.domain.model.Extra;
import com.gabriel.orders.core.domain.model.Product;
import com.gabriel.orders.core.domain.port.MenuRepository;
import com.gabriel.orders.infra.grpc.GrpcClientConfig;
import com.gabriel.specs.menu.MenuResponse;
import integration.com.gabriel.orders.adapter.container.GrpcServerTestContainer;
import io.grpc.ManagedChannel;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Import({GrpcClientConfig.class})
@ContextConfiguration(classes = {MenuGrpcClient.class, GrpcServerTestContainer.class})
public class MenuGrpcClientIntegrationTest {

    @Autowired
    ManagedChannel managedMenuChannel;
    @Autowired
    MenuGrpcClient menuGrpcClient;
    @SpyBean
    SetupMenuUseCase setupMenuUseCase;
    @MockBean
    MenuRepository menuRepository;
    @Captor
    private ArgumentCaptor<MenuResponse> menuResponseCaptor;
    @Captor
    private ArgumentCaptor<Product> productCaptor;
    @Captor
    private ArgumentCaptor<Extra> extraCaptor;

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        GrpcServerTestContainer.grpcProperties(registry);
    }

    @Test
    void testDumpMenuData() {
        // Execute the method to test
        menuGrpcClient.dumpMenuData();

        // Capture and assert the response passed to setupMenuUseCase
        verify(setupMenuUseCase).setupData(menuResponseCaptor.capture());
        MenuResponse capturedResponse = menuResponseCaptor.getValue();

        // Assert the details of the response based on your stub
        assertThat(capturedResponse.getItemsList()).hasSize(2); // Assuming 2 items in stub
        assertThat(capturedResponse.getItemsList().get(0).getName()).isEqualTo("Costela");
        assertThat(capturedResponse.getItemsList().get(1).getName()).isEqualTo("Cheese Burger");

        // Assert channel is shutdown
        // assertThat(managedMenuChannel.isShutdown()).isTrue();
    }

    @Test
    void testDumpProductDataCached() {
        // Execute the method to test
        menuGrpcClient.dumpMenuData();

        Product dumpedProduct = new Product(
            new ProductID("5ed5dad3-PRDC-2024-02-12"),
            new Name("Cheese Burger"),
            new Price(10.99),
            Instant.parse("2022-01-02T12:00:00Z"));

        // Capture and assert the response passed to setupMenuUseCase
        verify(menuRepository).addProduct(productCaptor.capture());
        Product capturedProduct = productCaptor.getValue();

        // Assert the details of the response based on your stub
        assertEquals(dumpedProduct.getProductID().getId(), capturedProduct.getProductID().getId());
        assertEquals(dumpedProduct.getName().getValue(), capturedProduct.getName().getValue());
        assertEquals(dumpedProduct.getPrice().getValue(), capturedProduct.getPrice().getValue());
        assertEquals(dumpedProduct.getTimestamp(), capturedProduct.getTimestamp());
    }

    @Test
    void testDumpExtraDataCached() {
        // Execute the method to test
        menuGrpcClient.dumpMenuData();

        Extra dumpedExtra = new Extra(
            new IngredientID("fabe70b1-INGR-2024-02-12"),
            new Name("Costela"),
            new Price(1.99),
            Instant.parse("2022-01-01T12:00:00Z"));

        // Capture and assert the response passed to setupMenuUseCase
        verify(menuRepository).addExtra(extraCaptor.capture());
        Extra capturedExtra = extraCaptor.getValue();

        // Assert the details of the response based on your stub
        assertEquals(dumpedExtra.getName().getValue(), capturedExtra.getName().getValue());
        assertEquals(dumpedExtra.getPrice().getValue(), capturedExtra.getPrice().getValue());
        assertEquals(dumpedExtra.getTimestamp(), capturedExtra.getTimestamp());
    }
}

