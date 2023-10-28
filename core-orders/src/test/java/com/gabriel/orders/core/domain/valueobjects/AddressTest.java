package com.gabriel.orders.core.domain.valueobjects;

import com.gabriel.orders.core.domain.base.DomainException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddressTest {

    @Test
    void shouldThrowExceptionWhenStreetIsBlank() {
        assertThatThrownBy(() -> new Address(" ", "City", "ST", "12345-678"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Street cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenStreetIsTooLong() {
        String longStreet = "S".repeat(256);
        assertThatThrownBy(() -> new Address(longStreet, "City", "ST", "12345-678"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Street name cannot exceed 255 characters");
    }

    @Test
    void shouldThrowExceptionWhenCityIsBlank() {
        assertThatThrownBy(() -> new Address("Street", " ", "ST", "12345-678"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("City cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenCityIsTooLong() {
        String longCity = "C".repeat(256);
        assertThatThrownBy(() -> new Address("Street", longCity, "ST", "12345-678"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("City name cannot exceed 255 characters");
    }

    @Test
    void shouldThrowExceptionWhenStateIsNotTwoCharacters() {
        assertThatThrownBy(() -> new Address("Street", "City", "S", "12345-678"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("State must be exactly 2 characters");

        assertThatThrownBy(() -> new Address("Street", "City", "STT", "12345-678"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("State must be exactly 2 characters");
    }

    @Test
    void shouldThrowExceptionWhenZipCodeDoesNotFollowPattern() {
        assertThatThrownBy(() -> new Address("Street", "City", "ST", "12345678"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Zip code must follow the pattern XXXXX-XXX");

        assertThatThrownBy(() -> new Address("Street", "City", "ST", "1234-678"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Zip code must follow the pattern XXXXX-XXX");
    }

    @Test
    void shouldCreateAddressWhenAllFieldsAreValid() {
        assertThatCode(() -> new Address("Street", "City", "ST", "12345-678"))
                .doesNotThrowAnyException();
    }
}

