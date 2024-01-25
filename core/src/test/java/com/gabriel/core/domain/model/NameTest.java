package com.gabriel.core.domain.model;

import com.gabriel.core.domain.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void shouldCreateNameSuccessfully_whenValueIsValid() {
        // Arrange
        String validName = "John Doe";

        // Act
        Name name = new Name(validName);

        // Assert
        Assertions.assertNotNull(name);
        Assertions.assertEquals(validName, name.getValue());
    }

    @Test
    public void shouldThrowException_whenValueIsNull() {
        // Arrange
        String nullName = null;

        // Act & Assert
        Assertions.assertThrows(DomainException.class, () -> new Name(nullName));
    }

    @Test
    public void shouldThrowException_whenValueIsEmpty() {
        // Arrange
        String emptyName = "";

        // Act & Assert
        Assertions.assertThrows(DomainException.class, () -> new Name(emptyName));
    }

    @Test
    public void shouldThrowException_whenValueIsBlank() {
        // Arrange
        String blankName = "   ";

        // Act & Assert
        Assertions.assertThrows(DomainException.class, () -> new Name(blankName));
    }

    @Test
    public void shouldThrowException_whenValueExceeds255Characters() {
        // Arrange
        String longName = "a".repeat(256);

        // Act & Assert
        Assertions.assertThrows(DomainException.class, () -> new Name(longName));
    }

    @Test
    public void shouldCreateNameSuccessfully_whenValueIsExactly255Characters() {
        // Arrange
        String validName = "a".repeat(255);

        // Act
        Name name = new Name(validName);

        // Assert
        Assertions.assertNotNull(name);
        Assertions.assertEquals(validName, name.getValue());
    }
}

