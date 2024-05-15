package com.gabriel.permissions.ui.controller;

import com.gabriel.permissions.application.service.PermissionService;
import com.gabriel.permissions.domain.model.Role;
import com.gabriel.permissions.domain.model.RoleAuthority;
import com.gabriel.specs.permissions.PermissionGrpc;
import com.gabriel.specs.permissions.PermissionRequest;
import com.gabriel.specs.permissions.PermissionResponse;
import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@GrpcService
public class PermissionsGrpcController extends PermissionGrpc.PermissionImplBase {

    private final PermissionService permissionService;

    public PermissionsGrpcController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    @Transactional(readOnly = true)
    public void retrievePermissions(PermissionRequest request, StreamObserver<PermissionResponse> responseObserver) {
        try {
            List<Role> roles = !request.getRole().isEmpty() && !request.getRole().equals("all") ?
                List.of(permissionService.retrieveRoleByName(request.getRole())) :
                permissionService.retrieveAllRoles();

            PermissionResponse.Builder responseBuilder = PermissionResponse.newBuilder();

            for (Role role : roles) {
                for (RoleAuthority authority : role.getRoleAuthorities()) {

                    Instant instant = authority.getUpdateTimestamp();

                    Timestamp timestamp = Timestamp.newBuilder()
                        .setSeconds(instant.getEpochSecond())
                        .setNanos(instant.getNano())
                        .build();

                    responseBuilder.addItems(
                        com.gabriel.specs.permissions.PermissionItem.newBuilder()
                            .setId(authority.getPermissionID().getId())
                            .setRole(authority.getRole().getName())
                            .setAuthority(authority.getAuthority().getName())
                            .setLastUpdated(timestamp)
                            .build()
                    );
                }
            }

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}
