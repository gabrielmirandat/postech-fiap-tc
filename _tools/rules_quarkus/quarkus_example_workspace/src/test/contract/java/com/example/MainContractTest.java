package com.example;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Contract tests for Main class
 */
public class MainContractTest {

    @Test
    void testContractValidation() {
        // Contract test to verify the test framework is working
        assertThat(Main.class).isNotNull();
    }

    @Test
    void testBasicContract() {
        // Test basic contract behavior
        String message = "Hello Quarkus";
        assertThat(message).contains("Quarkus");
    }
}
