package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for Main class
 */
public class MainUnitTest {

    @Test
    void testMainClassExists() {
        // Simple test to verify the test framework is working
        assertThat(Main.class).isNotNull();
    }

    @Test
    void testBasicAssertion() {
        String expected = "Hello Quarkus";
        String actual = "Hello Quarkus";
        assertThat(actual).isEqualTo(expected);
    }
}
