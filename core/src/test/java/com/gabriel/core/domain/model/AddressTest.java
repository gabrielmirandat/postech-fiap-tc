package com.gabriel.core.domain.model;

import com.gabriel.core.domain.exception.DomainException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AddressTest {

    @Test
    void shouldThrowExceptionWhenStreetIsBlank() {
        Assertions.assertThatThrownBy(() -> new Address(" ", "City", "ST", "12345-678"))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Street cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenStreetIsTooLong() {
        String longStreet = "S".repeat(256);
        Assertions.assertThatThrownBy(() -> new Address(longStreet, "City", "ST", "12345-678"))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Street name cannot exceed 255 characters");
    }

    @Test
    void shouldThrowExceptionWhenCityIsBlank() {
        Assertions.assertThatThrownBy(() -> new Address("Street", " ", "ST", "12345-678"))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("City cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenCityIsTooLong() {
        String longCity = "C".repeat(256);
        Assertions.assertThatThrownBy(() -> new Address("Street", longCity, "ST", "12345-678"))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("City name cannot exceed 255 characters");
    }

    @Test
    void shouldThrowExceptionWhenStateIsNotTwoCharacters() {
        Assertions.assertThatThrownBy(() -> new Address("Street", "City", "S", "12345-678"))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("State must be exactly 2 characters");

        Assertions.assertThatThrownBy(() -> new Address("Street", "City", "STT", "12345-678"))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("State must be exactly 2 characters");
    }

    @Test
    void shouldThrowExceptionWhenZipCodeDoesNotFollowPattern() {
        Assertions.assertThatThrownBy(() -> new Address("Street", "City", "ST", "12345678"))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Zip code must follow the pattern XXXXX-XXX");

        Assertions.assertThatThrownBy(() -> new Address("Street", "City", "ST", "1234-678"))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Zip code must follow the pattern XXXXX-XXX");
    }

    @Test
    void shouldCreateAddressWhenAllFieldsAreValid() {
        Assertions.assertThatCode(() -> new Address("Street", "City", "ST", "12345-678"))
            .doesNotThrowAnyException();
    }
}

