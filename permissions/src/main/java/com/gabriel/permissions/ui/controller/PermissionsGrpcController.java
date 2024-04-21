package com.gabriel.permissions.ui.controller;

import com.gabriel.permissions.application.service.PermissionService;
import com.gabriel.permissions.domain.model.Role;
import com.gabriel.specs.permissions.PermissionsGrpc;
import com.gabriel.specs.permissions.RoleAuthoritiesRequest;
import com.gabriel.specs.permissions.RoleAuthoritiesResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@GrpcService
public class PermissionsGrpcController extends PermissionsGrpc.PermissionsImplBase {

    private final PermissionService permissionService;

    public PermissionsGrpcController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    @Transactional(readOnly = true)
    public void retrieveRoleAuthorities(RoleAuthoritiesRequest request, StreamObserver<RoleAuthoritiesResponse> responseObserver) {
        try {
            List<Role> roles = !request.getRole().isEmpty() ?
                List.of(permissionService.retrieveRole(request.getRole())) :
                permissionService.retrieveAllRoles();

            RoleAuthoritiesResponse.Builder responseBuilder = RoleAuthoritiesResponse.newBuilder();

            for (Role role : roles) {
                responseBuilder.addRoleAuthorities(
                    com.gabriel.specs.permissions.RoleAuthority.newBuilder()
                        .setRole(role.getName())
                        .addAllAuthorities(role.getRoleAuthorities().stream().map(
                            roleAuthority -> roleAuthority.getAuthority().getName()).toList())
                        .build()
                );
            }
            
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}
