package com.gabriel.orders.adapter;

import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@Testcontainers
public class TestContainersBase {

    @Container
    public static DockerComposeContainer environment;

    static {
        environment = new DockerComposeContainer(new File("../compose-test.yaml"))
            .withExposedService("mongo", 27017)
            .withExposedService("redis", 6379)
            .withExposedService("kafka", 9092)
            .withLocalCompose(true);

        environment.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

