package com.gabriel.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ModelTests {

    @Nested
    class AddressTest {

        @Test
        void shouldThrowExceptionWhenStreetIsBlank() {
            Assertions.assertThatThrownBy(() ->
                Model.validate(
                    Address.newBuilder()
                    .setStreet(" ")
                    .setCity("City")
                    .setState("ST")
                    .setZip("12345-678")
                    .build()
                )
            )
            .isInstanceOf(Model.Exception.class)
            .hasMessageContaining("Street cannot be blank");
        }
    
        @Test
        void shouldThrowExceptionWhenStreetIsTooLong() {
            String longStreet = "S".repeat(256);
            Assertions.assertThatThrownBy(() -> 
                Model.validate(
                    Address.newBuilder()
                    .setStreet(longStreet)
                    .setCity("City")
                    .setState("ST")
                    .setZip("12345-678")
                    .build()
                )
            )
            .isInstanceOf(Model.Exception.class)
            .hasMessageContaining("Street name cannot exceed 255 characters");
        }
    
        @Test
        void shouldThrowExceptionWhenCityIsBlank() {
            Assertions.assertThatThrownBy(() -> 
                Model.validate(
                    Address.newBuilder()
                    .setStreet("Street")
                    .setCity(" ")
                    .setState("ST")
                    .setZip("12345-678")
                    .build()
                )
            )
            .isInstanceOf(Model.Exception.class)
            .hasMessageContaining("City cannot be blank");
        }
    
        @Test
        void shouldThrowExceptionWhenCityIsTooLong() {
            String longCity = "C".repeat(256);
            Assertions.assertThatThrownBy(() -> 
                Model.validate(
                    Address.newBuilder()
                    .setStreet("Street")
                    .setCity(longCity)
                    .setState("ST")
                    .setZip("12345-678")
                    .build()
                )
            )
            .isInstanceOf(Model.Exception.class)
            .hasMessageContaining("City name cannot exceed 255 characters");
        }
    
        @Test
        void shouldThrowExceptionWhenStateIsNotTwoCharacters() {
            Assertions.assertThatThrownBy(() -> 
                Model.validate(
                    Address.newBuilder()
                    .setStreet("Street")
                    .setCity("City")
                    .setState("S")
                    .setZip("12345-678")
                    .build()
                )
            )
            .isInstanceOf(Model.Exception.class)
            .hasMessageContaining("State must be exactly 2 characters");
    
            Assertions.assertThatThrownBy(() -> 
                Model.validate(
                    Address.newBuilder()
                    .setStreet("Street")
                    .setCity("City")
                    .setState("STT")
                    .setZip("12345-678")
                    .build()
                )
            )
            .isInstanceOf(Model.Exception.class)
            .hasMessageContaining("State must be exactly 2 characters");
        }
    
        @Test
        void shouldThrowExceptionWhenZipCodeDoesNotFollowPattern() {
            Assertions.assertThatThrownBy(() -> 
                Model.validate(
                    Address.newBuilder()
                    .setStreet("Street")
                    .setCity("City")
                    .setState("ST")
                    .setZip("12345678")
                    .build()
                )
            )
            .isInstanceOf(Model.Exception.class)
            .hasMessageContaining("Zip code must follow the pattern XXXXX-XXX");
    
            Assertions.assertThatThrownBy(() -> 
                Model.validate(
                    Address.newBuilder()
                    .setStreet("Street")
                    .setCity("City")
                    .setState("ST")
                    .setZip("1234-678")
                    .build()
                )
            )
            .isInstanceOf(Model.Exception.class)
            .hasMessageContaining("Zip code must follow the pattern XXXXX-XXX");
        }
    
        @Test
        void shouldCreateAddressWhenAllFieldsAreValid() {
            Assertions.assertThatCode(() -> 
                Model.validate(
                    Address.newBuilder()
                    .setStreet("Street")
                    .setCity("City")
                    .setState("ST")
                    .setZip("12345-678")
                    .build()
                )
            )
            .doesNotThrowAnyException();
        }
    }

    @Nested
    class CellphoneTest {

        @Test
        void shouldThrowExceptionWhenNumberIsBlank() {
            Assertions.assertThatThrownBy(() -> 
                Model.validate(
                    Cellphone.newBuilder().setValue(" ").build()
                )
            )
                .isInstanceOf(Model.Exception.class)
                .hasMessageContaining("Cellphone number cannot be blank");
        }
    
        @Test
        void shouldThrowExceptionWhenNumberDoesNotFollowPattern() {
            Assertions.assertThatThrownBy(() -> Model.validate(
                Cellphone.newBuilder().setValue("(12) 1234-567").build()
            ))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Cellphone number must follow the pattern");
    
            Assertions.assertThatThrownBy(() -> Model.validate(
                Cellphone.newBuilder().setValue("12345678901").build()
            ))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Cellphone number must follow the pattern");
        }
    
        @Test
        void shouldCreateCellphoneWhenNumberIsValid() {
            Assertions.assertThatCode(() -> Model.validate(
                Cellphone.newBuilder().setValue("(12) 1234-5678").build()
            ))
                .doesNotThrowAnyException();
    
            Assertions.assertThatCode(() -> Model.validate(
                Cellphone.newBuilder().setValue("(12) 12345-6789").build()
            ))
                .doesNotThrowAnyException();
        }
    }

    @Nested
    class CPFTest {

        @Test
        void shouldCreateCPFWhenIdIsValid() {
            Assertions.assertThatCode(() -> 
                Model.validate(
                    Cpf.newBuilder().setValue("123.456.789-09").build()
                )
            )
            .doesNotThrowAnyException();
        }
    
        @Test
        void shouldThrowExceptionWhenIdIsNull() {
            Assertions.assertThatThrownBy(() -> 
                Model.validate(
                    Cpf.newBuilder().setValue(null).build()
                )
            )
            .isInstanceOf(Model.Exception.class)
            .hasMessage("Domain validation failed: id CPF cannot be blank");
        }
    
        @Test
        void shouldThrowExceptionWhenIdIsBlank() {
            Assertions.assertThatThrownBy(() -> 
                Model.validate(
                    Cpf.newBuilder().setValue("   ").build()
                )
            )
            .isInstanceOf(Model.Exception.class)
            .hasMessage("Domain validation failed: id CPF cannot be blank, " +
                "id CPF must follow the pattern XXX.XXX.XXX-XX");
        }
    
        @Test
        void shouldThrowExceptionWhenIdDoesNotFollowPattern() {
            Assertions.assertThatThrownBy(() -> 
                Model.validate(
                    Cpf.newBuilder().setValue("12345678909").build()
                )
            )
            .isInstanceOf(Model.Exception.class)
            .hasMessage("Domain validation failed: id CPF must follow the pattern XXX.XXX.XXX-XX");
        }
    }

    @Nested
    class EmailTest {

        @Test
        void shouldThrowExceptionWhenAddressIsBlank() {
            Assertions.assertThatThrownBy(() -> 
                Model.validate(
                    Email.newBuilder().setValue(" ").build()
                )
            )
                .isInstanceOf(Model.Exception.class)
                .hasMessageContaining("Email address cannot be blank");
        }
    
        @Test
        void shouldThrowExceptionWhenAddressDoesNotFollowEmailPattern() {
            Assertions.assertThatThrownBy(() -> 
                Model.validate(
                    Email.newBuilder().setValue("invalid-email").build()
                )
            )
                .isInstanceOf(Model.Exception.class)
                .hasMessageContaining("Invalid email address format");
    
            Assertions.assertThatThrownBy(() -> 
                Model.validate(
                    Email.newBuilder().setValue("invalid@.com").build()
                )
            )
                .isInstanceOf(Model.Exception.class)
                .hasMessageContaining("Invalid email address format");
        }
    
        @Test
        void shouldCreateEmailAddressWhenAddressIsValid() {
            Assertions.assertThatCode(() -> 
                Model.validate(
                    Email.newBuilder().setValue("valid@example.com").build()
                )
            )
                .doesNotThrowAnyException();
        }
    }

    @Nested
    class NameTest {

        @Test
        public void shouldCreateNameSuccessfully_whenValueIsValid() {
            Assertions.assertNotNull(Model.validate(Name.newBuilder().setValue("John Doe").build()));
            Assertions.assertEquals("John Doe", Model.validate(Name.newBuilder().setValue("John Doe").build()).getValue());
        }
    
        @Test
        public void shouldThrowException_whenValueIsNull() {
            Assertions.assertThrows(Model.Exception.class, () -> Model.validate(Name.newBuilder().setValue(null).build()));
        }
    
        @Test
        public void shouldThrowException_whenValueIsEmpty() {
            Assertions.assertThrows(Model.Exception.class, () -> Model.validate(Name.newBuilder().setValue("").build()));
        }
    
        @Test
        public void shouldThrowException_whenValueIsBlank() {
            Assertions.assertThrows(Model.Exception.class, () -> Model.validate(Name.newBuilder().setValue("   ").build()));
        }
    
        @Test
        public void shouldThrowException_whenValueExceeds255Characters() {
            Assertions.assertThrows(Model.Exception.class, () -> Model.validate(Name.newBuilder().setValue("a".repeat(256)).build()));
        }
    
        @Test
        public void shouldCreateNameSuccessfully_whenValueIsExactly255Characters() {
            Assertions.assertNotNull(Model.validate(Name.newBuilder().setValue("a".repeat(255)).build()));
            Assertions.assertEquals("a".repeat(255), Model.validate(Name.newBuilder().setValue("a".repeat(255)).build()).getValue());
        }
    }

    @Nested
    class NotificationTest {

        @Test
        void shouldCreateNotificationSuccessfullyWhenTypeAndValueAreValid() {
            Assertions.assertThatCode(() -> Model.validate(
                Notification.newBuilder().setCellphone(
                    Cellphone.newBuilder().setValue("(11) 98765-4321").build()
                ).build())
            ).doesNotThrowAnyException();
    
            Assertions.assertThatCode(() -> Model.validate(
                Notification.newBuilder().setEmail(
                    Email.newBuilder().setValue("example@example.com").build()
                ).build())
            ).doesNotThrowAnyException();
        }
    
        @Test
        void shouldThrowExceptionWhenValueIsInvalid() {
            Assertions.assertThatThrownBy(() -> Model.validate(
                Notification.newBuilder().setCellphone(
                    Cellphone.newBuilder().setValue("invalid").build()
                ).build())
            ).isInstanceOf(Model.Exception.class)
            .hasMessageContaining("Cellphone number must follow the pattern (XX) XXXX-XXXX or (XX) XXXXX-XXXX");
    
            Assertions.assertThatThrownBy(() -> Model.validate(
                Notification.newBuilder().setEmail(
                    Email.newBuilder().setValue("invalid").build()
                ).build())
            ).isInstanceOf(Model.Exception.class)
            .hasMessageContaining("Invalid email address format");
        }
    }  

    @Nested
    class PriceTest {

        @Test
        void shouldCreatePriceSuccessfully() {
            Assertions.assertThatCode(() -> Model.validate(Price.newBuilder().setValue(5.0).build()))
                .doesNotThrowAnyException();
        }
    
        @Test
        void shouldNotCreatePriceWithNegativeValue() {
            Assertions.assertThatThrownBy(() -> Model.validate(Price.newBuilder().setValue(-1.0).build()))
                .isInstanceOf(Model.Exception.class)
                .hasMessageContaining("Price must be at least 0.1");
        }
    
        @Test
        void shouldNotCreatePriceWithZeroValue() {
            Assertions.assertThatThrownBy(() -> Model.validate(Price.newBuilder().setValue(0.0).build()))
                .isInstanceOf(Model.Exception.class)
                .hasMessageContaining("Price must be at least 0.1");
        }
    
        @Test
        void shouldNotCreatePriceWithTooLowValue() {
            Assertions.assertThatThrownBy(() -> Model.validate(Price.newBuilder().setValue(0.05).build()))
                .isInstanceOf(Model.Exception.class)
                .hasMessageContaining("Price must be at least 0.1");
        }
    
        @Test
        void shouldNotCreatePriceWithTooHighValue() {
            Assertions.assertThatThrownBy(() -> Model.validate(Price.newBuilder().setValue(10000.1).build()))
                .isInstanceOf(Model.Exception.class)
                .hasMessageContaining("Price must be less than 10000.0");
        }
    }

    @Nested
    class QuantityTest {

        @Test
        void shouldCreateQuantitySuccessfully_whenSizeIsValid() {
            Assertions.assertThatCode(() -> Model.validate(Quantity.newBuilder().setValue(5).build()))
                .doesNotThrowAnyException();
        }
    
        @Test
        void shouldThrowException_whenSizeIsLessThanOne() {
            Assertions.assertThatThrownBy(() -> Model.validate(Quantity.newBuilder().setValue(0).build()))
                .isInstanceOf(Model.Exception.class)
                .hasMessageContaining("Quantity size must be between 1 and 10");
        }
    
        @Test
        void shouldThrowException_whenSizeIsMoreThanTen() {
            Assertions.assertThatThrownBy(() -> Model.validate(Quantity.newBuilder().setValue(11).build()))
                .isInstanceOf(Model.Exception.class)
                .hasMessageContaining("Quantity size must be between 1 and 10");
        }
    
        @Test
        void shouldCreateQuantitySuccessfully_whenSizeIsExactlyOne() {
            Assertions.assertThatCode(() -> Model.validate(Quantity.newBuilder().setValue(1).build()))
                .doesNotThrowAnyException();
        }
    
        @Test
        void shouldCreateQuantitySuccessfully_whenSizeIsExactlyTen() {
            Assertions.assertThatCode(() -> Model.validate(Quantity.newBuilder().setValue(10).build()))
                .doesNotThrowAnyException();
        }
    }

    @Nested
    class OrderIDTest {

        @Test
        void shouldThrowExceptionWhenIdDoesNotFollowOrderIDPattern() {
            Assertions.assertThatThrownBy(() -> Model.validate(
                OrderId.newBuilder().setValue("invalid-id").build()
            ))
            .isInstanceOf(Model.Exception.class)
            .hasMessageContaining("Invalid Order ID format");
        }
    
        @Test
        void shouldCreateOrderIDWhenIdIsValid() {
            String validId = "12345678-ORDR-2023-04-18";
            Assertions.assertThatCode(() -> Model.validate(
                OrderId.newBuilder().setValue(validId).build()
            )).doesNotThrowAnyException();
        }
    
        @Test
        void shouldGenerateValidOrderIDWhenNoIdProvided() {
            OrderId orderID = (OrderId) Model.validate(OrderId.newBuilder().build());
            String[] parts = orderID.getValue().split("-");
            Assertions.assertThat(parts).hasSize(5);
            Assertions.assertThat(parts[0]).matches("[0-9a-f]{8}");
            Assertions.assertThat(parts[1]).isEqualTo("ORDR");
            Assertions.assertThat(parts[2]).matches("\\d{4}");
            Assertions.assertThat(parts[3]).matches("\\d{2}");
            Assertions.assertThat(parts[4]).matches("\\d{2}");
        }
    
        @Test
        void shouldCompareOrderIDsBasedOnId() {
            OrderId orderID1 = (OrderId) Model.validate(OrderId.newBuilder().setValue("12345678-ORDR-2023-04-18").build());
            OrderId orderID2 = (OrderId) Model.validate(OrderId.newBuilder().setValue("12345678-ORDR-2023-04-18").build());
            OrderId orderID3 = (OrderId) Model.validate(OrderId.newBuilder().setValue("87654321-ORDR-2023-04-18").build());
            Assertions.assertThat(orderID1).isEqualTo(orderID2);
            Assertions.assertThat(orderID1).isNotEqualTo(orderID3);
        }
    }

    @Nested
    class ProductIDTest {

        @Test
        void shouldThrowExceptionWhenIdDoesNotFollowProductIDPattern() {
            Assertions.assertThatThrownBy(() -> Model.validate(
                ProductId.newBuilder().setValue("invalid-id").build()
            ))
            .isInstanceOf(Model.Exception.class)
            .hasMessageContaining("Invalid Product ID format");
        }
    
        @Test
        void shouldCreateProductIDWhenIdIsValid() {
            String validId = "12345678-PRDC-2023-04-18";
            Assertions.assertThatCode(() -> Model.validate(
                ProductId.newBuilder().setValue(validId).build()
            )).doesNotThrowAnyException();
        }
    
        @Test
        void shouldGenerateValidProductIDWhenNoIdProvided() {
            ProductId productID = (ProductId) Model.validate(ProductId.newBuilder().build();
            String[] parts = productID.getValue().split("-");
            Assertions.assertThat(parts).hasSize(5);
            Assertions.assertThat(parts[0]).matches("[0-9a-f]{8}");
            Assertions.assertThat(parts[1]).isEqualTo("PRDC");
            Assertions.assertThat(parts[2]).matches("\\d{4}");
            Assertions.assertThat(parts[3]).matches("\\d{2}");
            Assertions.assertThat(parts[4]).matches("\\d{2}");
        }
    
        @Test
        void shouldCompareProductIDsBasedOnId() {
            ProductId productID1 = (ProductId) Model.validate(ProductId.newBuilder().setValue("12345678-PRDC-2023-04-18").build());
            ProductId productID2 = (ProductId) Model.validate(ProductId.newBuilder().setValue("12345678-PRDC-2023-04-18").build());
            ProductId productID3 = (ProductId) Model.validate(ProductId.newBuilder().setValue("87654321-PRDC-2023-04-18").build());
            Assertions.assertThat(productID1).isEqualTo(productID2);
            Assertions.assertThat(productID1).isNotEqualTo(productID3);
        }
    }
}