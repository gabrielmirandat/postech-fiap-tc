package unit.com.gabriel.orders.adapter.driven.api;

import com.gabriel.orders.adapter.driven.api.MenuGrpcClient;
import com.gabriel.orders.core.application.usecase.SetupMenuUseCase;
import com.gabriel.specs.menu.MenuGrpc;
import com.gabriel.specs.menu.MenuItem;
import com.gabriel.specs.menu.MenuRequest;
import com.gabriel.specs.menu.MenuResponse;
import io.grpc.ManagedChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class MenuGrpcClientTest {

    @Mock
    private ManagedChannel managedMenuChannel;

    @Mock
    private SetupMenuUseCase setupMenuUseCase;

    private MenuGrpcClient menuGrpcClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        menuGrpcClient = new MenuGrpcClient(managedMenuChannel, setupMenuUseCase);
    }

    @Test
    void dumpMenuData_success() {
        try (MockedStatic<MenuGrpc> mockedStatic = mockStatic(MenuGrpc.class)) {
            // Prepare
            MenuGrpc.MenuBlockingStub menuBlockingStub = mock(MenuGrpc.MenuBlockingStub.class);
            MenuRequest request = MenuRequest.newBuilder().setCategory("all").build();
            MenuResponse response = MenuResponse.newBuilder()
                .addItems(
                    MenuItem.newBuilder().build())
                .build();

            mockedStatic.when(() -> MenuGrpc.newBlockingStub(managedMenuChannel)).thenReturn(menuBlockingStub);
            when(menuBlockingStub.retrieveMenu(request)).thenReturn(response);

            // Act
            menuGrpcClient.dumpMenuData();

            // Assert
            verify(menuBlockingStub, times(1)).retrieveMenu(request);
            verify(setupMenuUseCase, times(1)).setupData(response);
        }
    }
}
