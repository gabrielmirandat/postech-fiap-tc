package com.gabriel.permissions

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(basePackages = ["com.gabriel.permissions.domain.repository"])
@EntityScan("com.gabriel.permissions.domain.model")
@SpringBootApplication
class PermissionsApplication

fun main(args: Array<String>) {
    SpringApplication.run(PermissionsApplication::class.java, *args)
}
