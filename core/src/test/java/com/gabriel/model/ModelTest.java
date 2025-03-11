package com.gabriel.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ModelTest {

    @Nested
    class AddressTest {

        @Test
        void shouldThrowExceptionWhenStreetIsBlank() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Address.newBuilder()
                        .setStreet(" ")
                        .setCity("City")
                        .setState("ST")
                        .setZip("12345-678")
                        .build()
                )
            );
            assertEquals("Street cannot be blank", exception.getMessage());
        }
    
        @Test
        void shouldThrowExceptionWhenStreetIsTooLong() {
            String longStreet = "S".repeat(256);
            
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Address.newBuilder()
                        .setStreet(longStreet)
                        .setCity("City")
                        .setState("ST")
                        .setZip("12345-678")
                        .build()
                )
            );
    
            assertEquals("Street name cannot exceed 255 characters", exception.getMessage());
        }
    
        @Test
        void shouldThrowExceptionWhenCityIsBlank() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Address.newBuilder()
                        .setStreet("Street")
                        .setCity(" ")
                        .setState("ST")
                        .setZip("12345-678")
                        .build()
                )
            );

            assertEquals("City cannot be blank", exception.getMessage());
        }
    
        @Test
        void shouldThrowExceptionWhenCityIsTooLong() {
            String longCity = "C".repeat(256);
    
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Address.newBuilder()
                        .setStreet("Street")
                        .setCity(longCity)
                        .setState("ST")
                        .setZip("12345-678")
                        .build()
                )
            );
    
            assertEquals("City name cannot exceed 255 characters", exception.getMessage());
        }
    

        @Test
        void shouldThrowExceptionWhenStateIsNotTwoCharacters() {
            // Test for state with 1 character
            Model.Exception exception1 = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Address.newBuilder()
                        .setStreet("Street")
                        .setCity("City")
                        .setState("S")
                        .setZip("12345-678")
                        .build()
                )
            );
            assertEquals(exception1.getMessage(), "State must be exactly 2 characters");
    
            // Test for state with 3 characters
            Model.Exception exception2 = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Address.newBuilder()
                        .setStreet("Street")
                        .setCity("City")
                        .setState("STT")
                        .setZip("12345-678")
                        .build()
                )
            );
            assertEquals(exception2.getMessage(), "State must be exactly 2 characters");
        }
    
        @Test
        void shouldThrowExceptionWhenZipCodeDoesNotFollowPattern() {
            // Test for zip code without hyphen
            Model.Exception exception1 = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Address.newBuilder()
                        .setStreet("Street")
                        .setCity("City")
                        .setState("ST")
                        .setZip("12345678")
                        .build()
                )
            );
            assertEquals(exception1.getMessage(), "Zip code must follow the pattern XXXXX-XXX");
    
            // Test for zip code with incorrect format
            Model.Exception exception2 = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Address.newBuilder()
                        .setStreet("Street")
                        .setCity("City")
                        .setState("ST")
                        .setZip("1234-678")
                        .build()
                )
            );
            assertEquals(exception2.getMessage(), "Zip code must follow the pattern XXXXX-XXX");
        }
    
        @Test
        void shouldCreateAddressWhenAllFieldsAreValid() {
            assertDoesNotThrow(() -> 
                Model.validate(
                    Address.newBuilder()
                        .setStreet("Street")
                        .setCity("City")
                        .setState("ST")
                        .setZip("12345-678")
                        .build()
                )
            );
        }
    }

    @Nested
    class CellphoneTest {

        @Test
        void shouldThrowExceptionWhenNumberIsBlank() {
            assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Cellphone.newBuilder().setValue(" ").build()
                )
            );
        }
    
        @Test
        void shouldThrowExceptionWhenNumberDoesNotFollowPattern() {
            assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Cellphone.newBuilder().setValue("(12) 1234-567").build()
                )
            );
    
            assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Cellphone.newBuilder().setValue("12345678901").build()
                )
            );
        }
    
        @Test
        void shouldCreateCellphoneWhenNumberIsValid() {
            assertDoesNotThrow(() -> 
                Model.validate(
                    Cellphone.newBuilder().setValue("(12) 1234-5678").build()
                )
            );
    
            assertDoesNotThrow(() -> 
                Model.validate(
                    Cellphone.newBuilder().setValue("(12) 12345-6789").build()
                )
            );
        }
    }

    @Nested
    class CPFTest {

        @Test
        void shouldCreateCPFWhenIdIsValid() {
            assertDoesNotThrow(() -> 
                Model.validate(
                    Cpf.newBuilder().setValue("123.456.789-09").build()
                )
            );
        }
        
        @Test
        void shouldThrowExceptionWhenIdIsNull() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Cpf.newBuilder().setValue(null).build()
                )
            );
            assertEquals("Domain validation failed: id CPF cannot be blank", exception.getMessage());
        }
        
        @Test
        void shouldThrowExceptionWhenIdIsBlank() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Cpf.newBuilder().setValue("   ").build()
                )
            );
            assertEquals("Domain validation failed: id CPF cannot be blank, " +
                "id CPF must follow the pattern XXX.XXX.XXX-XX", exception.getMessage());
        }
        
        @Test
        void shouldThrowExceptionWhenIdDoesNotFollowPattern() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Cpf.newBuilder().setValue("12345678909").build()
                )
            );
            assertEquals("Domain validation failed: id CPF must follow the pattern XXX.XXX.XXX-XX", exception.getMessage());
        }        
    }

    @Nested
    class EmailTest {

        @Test
        void shouldThrowExceptionWhenAddressIsBlank() {
            assertThrows(Model.Exception.class, () ->
                Model.validate(
                    Email.newBuilder().setValue(" ").build()
                )
            );
        }
        
        @Test
        void shouldThrowExceptionWhenAddressDoesNotFollowEmailPattern() {
            Model.Exception exception1 = assertThrows(Model.Exception.class, () ->
                Model.validate(
                    Email.newBuilder().setValue("invalid-email").build()
                )
            );
            assertEquals(exception1.getMessage(), "Invalid email address format");
        
            Model.Exception exception2 = assertThrows(Model.Exception.class, () ->
                Model.validate(
                    Email.newBuilder().setValue("invalid@.com").build()
                )
            );
            assertEquals(exception2.getMessage(), "Invalid email address format");
        }
        
        @Test
        void shouldCreateEmailAddressWhenAddressIsValid() {
            assertDoesNotThrow(() ->
                Model.validate(
                    Email.newBuilder().setValue("valid@example.com").build()
                )
            );
        }        
    }

    @Nested
    class NameTest {

        @Test
        void shouldCreateNameSuccessfully_whenValueIsValid() {
            Name name = (Name) Model.validate(Name.newBuilder().setValue("John Doe").build());
            assertNotNull(name);
            assertEquals("John Doe", name.getValue());
        }
    
        @Test
        void shouldThrowException_whenValueIsNull() {
            assertThrows(Model.Exception.class, () -> 
                Model.validate(Name.newBuilder().setValue(null).build())
            );
        }
    
        @Test
        void shouldThrowException_whenValueIsEmpty() {
            assertThrows(Model.Exception.class, () -> 
                Model.validate(Name.newBuilder().setValue("").build())
            );
        }
    
        @Test
        void shouldThrowException_whenValueIsBlank() {
            assertThrows(Model.Exception.class, () -> 
                Model.validate(Name.newBuilder().setValue("   ").build())
            );
        }
    
        @Test
        void shouldThrowException_whenValueExceeds255Characters() {
            assertThrows(Model.Exception.class, () -> 
                Model.validate(Name.newBuilder().setValue("a".repeat(256)).build())
            );
        }
    
        @Test
        void shouldCreateNameSuccessfully_whenValueIsExactly255Characters() {
            Name name = (Name) Model.validate(Name.newBuilder().setValue("a".repeat(255)).build());
            assertNotNull(name);
            assertEquals("a".repeat(255), name.getValue());
        }
    }

    @Nested
    class NotificationTest {

        @Test
        void shouldCreateNotificationSuccessfullyWhenTypeAndValueAreValid() {
            assertDoesNotThrow(() -> 
                Model.validate(
                    Notification.newBuilder()
                        .setCellphone(Cellphone.newBuilder().setValue("(11) 98765-4321").build())
                        .build()
                )
            );

            assertDoesNotThrow(() -> 
                Model.validate(
                    Notification.newBuilder()
                        .setEmail(Email.newBuilder().setValue("example@example.com").build())
                        .build()
                )
            );
        }

        @Test
        void shouldThrowExceptionWhenValueIsInvalid() {
            Model.Exception cellphoneException = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Notification.newBuilder()
                        .setCellphone(Cellphone.newBuilder().setValue("invalid").build())
                        .build()
                )
            );
            assertEquals("Cellphone number must follow the pattern (XX) XXXX-XXXX or (XX) XXXXX-XXXX", cellphoneException.getMessage());

            Model.Exception emailException = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Notification.newBuilder()
                        .setEmail(Email.newBuilder().setValue("invalid").build())
                        .build()
                )
            );
            assertEquals("Invalid email address format", emailException.getMessage());
        }
    }  

    @Nested
    class PriceTest {

        @Test
        void shouldCreatePriceSuccessfully() {
            assertDoesNotThrow(() -> 
                Model.validate(Price.newBuilder().setValue(5.0).build())
            );
        }
    
        @Test
        void shouldNotCreatePriceWithNegativeValue() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(Price.newBuilder().setValue(-1.0).build())
            );
            assertEquals("Validation error:\n - value: Price must be between 0.1 and 10000.0 [price.value]", exception.getMessage());
        }
    
        @Test
        void shouldNotCreatePriceWithZeroValue() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(Price.newBuilder().setValue(0.0).build())
            );
            assertEquals("Validation error:\n - value: Price must be between 0.1 and 10000.0 [price.value]", exception.getMessage());
        }
    
        @Test
        void shouldNotCreatePriceWithTooLowValue() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(Price.newBuilder().setValue(0.05).build())
            );
            assertEquals("Validation error:\n - value: Price must be between 0.1 and 10000.0 [price.value]", exception.getMessage());
        }
    
        @Test
        void shouldNotCreatePriceWithTooHighValue() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(Price.newBuilder().setValue(10000.1).build())
            );
            assertEquals("Validation error:\n - value: Price must be between 0.1 and 10000.0 [price.value]", exception.getMessage());
        }
    }

    @Nested
    class QuantityTest {

        @Test
        void shouldCreateQuantitySuccessfully_whenSizeIsValid() {
            assertDoesNotThrow(() -> 
                Model.validate(Quantity.newBuilder().setValue(5).build())
            );
        }
    
        @Test
        void shouldThrowException_whenSizeIsLessThanOne() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(Quantity.newBuilder().setValue(0).build())
            );
            assertEquals("Validation error:\n - value: Quantity must be between 1 and 10 [quantity.value]", exception.getMessage());
        }
    
        @Test
        void shouldThrowException_whenSizeIsMoreThanTen() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(Quantity.newBuilder().setValue(11).build())
            );
            assertEquals("Validation error:\n - value: Quantity must be between 1 and 10 [quantity.value]", exception.getMessage());
        }
    
        @Test
        void shouldCreateQuantitySuccessfully_whenSizeIsExactlyOne() {
            assertDoesNotThrow(() -> 
                Model.validate(Quantity.newBuilder().setValue(1).build())
            );
        }
    
        @Test
        void shouldCreateQuantitySuccessfully_whenSizeIsExactlyTen() {
            assertDoesNotThrow(() -> 
                Model.validate(Quantity.newBuilder().setValue(10).build())
            );
        }
    }

    @Nested
    class OrderIDTest {

        @Test
        void shouldThrowExceptionWhenIdDoesNotFollowOrderIDPattern() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(OrderId.newBuilder().setValue("invalid-id").build())
            );
            assertEquals("Validation error:\n - value: Invalid Order ID format [order_id.value]", exception.getMessage());
        }
    
        @Test
        void shouldCreateOrderIDWhenIdIsValid() {
            String validId = "12345678-ORDR-2023-04-18";
            assertDoesNotThrow(() -> 
                Model.validate(OrderId.newBuilder().setValue(validId).build())
            );
        }
    
        @Test
        void shouldCompareOrderIDsBasedOnId() {
            OrderId orderID1 = (OrderId) Model.validate(OrderId.newBuilder().setValue("12345678-ORDR-2023-04-18").build());
            OrderId orderID2 = (OrderId) Model.validate(OrderId.newBuilder().setValue("12345678-ORDR-2023-04-18").build());
            OrderId orderID3 = (OrderId) Model.validate(OrderId.newBuilder().setValue("87654321-ORDR-2023-04-18").build());
    
            assertEquals(orderID1, orderID2);
            assertNotEquals(orderID1, orderID3);
        }
    }

    @Nested
    class ProductIDTest {

        @Test
        void shouldThrowExceptionWhenIdDoesNotFollowProductIDPattern() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(ProductId.newBuilder().setValue("invalid-id").build())
            );
            assertEquals("Validation error:\n - value: Invalid Product ID format [product_id.value]", exception.getMessage());
        }
    
        @Test
        void shouldCreateProductIDWhenIdIsValid() {
            String validId = "12345678-PRDC-2023-04-18";
            assertDoesNotThrow(() -> 
                Model.validate(ProductId.newBuilder().setValue(validId).build())
            );
        }
    
        @Test
        void shouldCompareProductIDsBasedOnId() {
            ProductId productID1 = (ProductId) Model.validate(ProductId.newBuilder().setValue("12345678-PRDC-2023-04-18").build());
            ProductId productID2 = (ProductId) Model.validate(ProductId.newBuilder().setValue("12345678-PRDC-2023-04-18").build());
            ProductId productID3 = (ProductId) Model.validate(ProductId.newBuilder().setValue("87654321-PRDC-2023-04-18").build());
    
            assertEquals(productID1, productID2);
            assertNotEquals(productID1, productID3);
        }
    }
}