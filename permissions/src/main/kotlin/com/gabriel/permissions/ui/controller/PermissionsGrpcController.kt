package com.gabriel.permissions.ui.controller

import com.gabriel.permissions.application.service.PermissionService
import com.gabriel.permissions.domain.model.Role
import com.gabriel.service.permissions.PermissionGrpc
import com.gabriel.service.permissions.PermissionRequest
import com.gabriel.service.permissions.PermissionResponse
import com.google.protobuf.Timestamp
import io.grpc.Status
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.transaction.annotation.Transactional

@GrpcService
class PermissionsGrpcController(
    private val permissionService: PermissionService
) : PermissionGrpc.PermissionImplBase() {

    @Transactional(readOnly = true)
    override fun retrievePermissions(
        request: PermissionRequest,
        responseObserver: StreamObserver<PermissionResponse>
    ) {
        try {
            val roles: List<Role> = if (request.role.isNotEmpty() && request.role != "all") {
                listOf(permissionService.retrieveRoleByName(request.role))
            } else {
                permissionService.retrieveAllRoles()
            }

            val responseBuilder = PermissionResponse.newBuilder()

            roles.forEach { role ->
                role.roleAuthorities.forEach { authority ->
                    val instant = authority.updatedAt

                    val timestamp = Timestamp.newBuilder()
                        .setSeconds(instant!!.epochSecond)
                        .setNanos(instant!!.nano)
                        .build()

                    responseBuilder.addItems(
                        com.gabriel.service.permissions.PermissionItem.newBuilder()
                            .setId(authority.permissionID!!.value)
                            .setRole(authority.role!!.name)
                            .setAuthority(authority.authority!!.name)
                            .setLastUpdated(timestamp)
                            .build()
                    )
                }
            }

            responseObserver.onNext(responseBuilder.build())
            responseObserver.onCompleted()

        } catch (e: Exception) {
            responseObserver.onError(
                Status.INTERNAL
                    .withDescription(e.message)
                    .asRuntimeException()
            )
        }
    }
}
