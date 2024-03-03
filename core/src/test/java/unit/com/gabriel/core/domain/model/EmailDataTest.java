package unit.com.gabriel.core.domain.model;

import com.gabriel.core.domain.exception.DomainException;
import com.gabriel.core.domain.model.EmailData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EmailDataTest {

    @Test
    void shouldThrowExceptionWhenAddressIsBlank() {
        Assertions.assertThatThrownBy(() -> new EmailData(" "))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Email address cannot be blank");
    }

    @Test
    void shouldThrowExceptionWhenAddressDoesNotFollowEmailPattern() {
        Assertions.assertThatThrownBy(() -> new EmailData("invalid-email"))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Invalid email address format");

        Assertions.assertThatThrownBy(() -> new EmailData("invalid@.com"))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("Invalid email address format");
    }

    @Test
    void shouldCreateEmailAddressWhenAddressIsValid() {
        Assertions.assertThatCode(() -> new EmailData("valid@example.com"))
            .doesNotThrowAnyException();
    }
}

