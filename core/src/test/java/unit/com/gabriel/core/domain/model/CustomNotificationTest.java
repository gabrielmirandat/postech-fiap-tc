package unit.com.gabriel.core.domain.model;

import com.gabriel.core.domain.exception.DomainException;
import com.gabriel.core.domain.model.CustomNotification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomNotificationTest {

    @Test
    void shouldCreateCustomNotificationTypeSuccessfullyWhenPatternIsCorrect() {
        Assertions.assertThatCode(() -> new CustomNotification("vendor|value"))
            .doesNotThrowAnyException();
    }

    @Test
    void shouldThrowExceptionWhenNotificationIsBlank() {
        Assertions.assertThatThrownBy(() -> new CustomNotification(""))
            .isInstanceOf(DomainException.class)
            .hasMessage("Domain validation failed: notification Custom notification cannot be blank, " +
                "notification Custom notification must follow the pattern <vendor>|<value>");
    }

    @Test
    void shouldThrowExceptionWhenPatternIsIncorrect() {
        Assertions.assertThatThrownBy(() -> new CustomNotification("invalidPattern"))
            .isInstanceOf(DomainException.class)
            .hasMessage("Domain validation failed: notification Custom notification must follow the pattern <vendor>|<value>");
    }

    @Test
    void shouldThrowExceptionWhenNotificationIsNull() {
        Assertions.assertThatThrownBy(() -> new CustomNotification(null))
            .isInstanceOf(DomainException.class)
            .hasMessage("Domain validation failed: notification Custom notification cannot be blank");
    }
}

