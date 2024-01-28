package com.gabriel.menu.adapter.driver.api;

import com.gabriel.menu.core.application.query.SearchMenuQuery;
import com.gabriel.menu.core.application.usecase.MenuUseCase;
import com.gabriel.menu.core.domain.model.Menu;
import com.gabriel.specs.menu.MenuGrpc;
import com.google.protobuf.Timestamp;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.List;

@GrpcService
public class MenuGrpcController extends MenuGrpc.MenuImplBase {

    @Inject
    MenuUseCase menuUseCase;

    @Override
    @Blocking
    public void retrieveMenu(com.gabriel.specs.menu.MenuRequest request,
                             io.grpc.stub.StreamObserver<com.gabriel.specs.menu.MenuResponse> responseObserver) {
        List<Menu> menu = !request.getCategory().isEmpty()
            ? menuUseCase.searchMenuByCategory(
            new SearchMenuQuery(request.getCategory()))
            : menuUseCase.searchMenu();

        com.gabriel.specs.menu.MenuResponse.Builder response =
            com.gabriel.specs.menu.MenuResponse.newBuilder();

        menu.forEach(menuItem -> {
            Instant instant = menuItem.getUpdateTimestamp();

            Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();

            response.addItems(
                com.gabriel.specs.menu.MenuItem.newBuilder()
                    .setId(menuItem.getMenuId())
                    .setName(menuItem.getName().getValue())
                    .setPrice(menuItem.getPrice().getValue())
                    .setCategory(menuItem.getCategory().toString())
                    .setLastUpdated(timestamp)
                    .build()
            );
        });

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }
}
