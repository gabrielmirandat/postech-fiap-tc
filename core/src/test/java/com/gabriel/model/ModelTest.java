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
            assertEquals("Validation error:\n - street: Street cannot be blank and must not exceed 255 characters [address.street]", exception.getMessage());
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
    
            assertEquals("Validation error:\n - street: Street cannot be blank and must not exceed 255 characters [address.street]", exception.getMessage());
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

            assertEquals("Validation error:\n - city: City cannot be blank and must not exceed 255 characters [address.city]", exception.getMessage());
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
    
            assertEquals("Validation error:\n - city: City cannot be blank and must not exceed 255 characters [address.city]", exception.getMessage());
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
            assertEquals("Validation error:\n - state: State must be exactly 2 characters [address.state]", exception1.getMessage());
    
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
            assertEquals("Validation error:\n - state: State must be exactly 2 characters [address.state]", exception2.getMessage());
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
            assertEquals("Validation error:\n - zip: Zip code must follow the pattern XXXXX-XXX [address.zip]", exception1.getMessage());
    
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
            assertEquals("Validation error:\n - zip: Zip code must follow the pattern XXXXX-XXX [address.zip]", exception2.getMessage());
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
    class ContactTest {

        @Test
        void shouldCreateContactSuccessfullyWhenTypeAndValueAreValid() {
            assertDoesNotThrow(() -> 
                Model.validate(
                    Contact.newBuilder()
                        .setCellphone(Cellphone.newBuilder().setValue("(11) 98765-4321").build())
                        .build()
                )
            );

            assertDoesNotThrow(() -> 
                Model.validate(
                    Contact.newBuilder()
                        .setEmail(Email.newBuilder().setValue("example@example.com").build())
                        .build()
                )
            );
        }

        @Test
        void shouldThrowExceptionWhenContactValueIsInvalid() {
            Model.Exception cellphoneException = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Contact.newBuilder()
                        .setCellphone(Cellphone.newBuilder().setValue("invalid").build())
                        .build()
                )
            );
            assertEquals("Validation error:\n - cellphone.value: Cellphone number cannot be blank and must follow the pattern (XX) XXXX-XXXX or (XX) XXXXX-XXXX [cellphone.value]", cellphoneException.getMessage());

            Model.Exception emailException = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Contact.newBuilder()
                        .setEmail(Email.newBuilder().setValue("invalid").build())
                        .build()
                )
            );
            assertEquals("Validation error:\n - email.value: Invalid email address format [email.value]", emailException.getMessage());
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
        void shouldThrowExceptionWhenIdIsBlank() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Cpf.newBuilder().setValue("   ").build()
                )
            );
            assertEquals("Validation error:\n - value: CPF must follow the pattern XXX.XXX.XXX-XX [cpf.value]", exception.getMessage());
        }
        
        @Test
        void shouldThrowExceptionWhenIdDoesNotFollowPattern() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Cpf.newBuilder().setValue("12345678909").build()
                )
            );
            assertEquals("Validation error:\n - value: CPF must follow the pattern XXX.XXX.XXX-XX [cpf.value]", exception.getMessage());
        }        
    }

    @Nested
    class DescriptionTest {

        @Test
        void shouldCreateDescriptionSuccessfully_whenValueIsValid() {
            Description description = (Description) Model.validate(Description.newBuilder().setValue("Goodies things").build());
            assertNotNull(description);
            assertEquals("Goodies things", description.getValue());
        }
    
        @Test
        void shouldThrowException_whenValueIsEmpty() {
            assertThrows(Model.Exception.class, () -> 
                Model.validate(Description.newBuilder().setValue("").build())
            );
        }
    
        @Test
        void shouldThrowException_whenValueIsBlank() {
            assertThrows(Model.Exception.class, () -> 
                Model.validate(Description.newBuilder().setValue("   ").build())
            );
        }
    
        @Test
        void shouldThrowException_whenValueExceeds255Characters() {
            assertThrows(Model.Exception.class, () -> 
                Model.validate(Description.newBuilder().setValue("a".repeat(256)).build())
            );
        }
    
        @Test
        void shouldCreateDescriptionSuccessfully_whenValueIsExactly255Characters() {
            Description description = (Description) Model.validate(Description.newBuilder().setValue("a".repeat(255)).build());
            assertNotNull(description);
            assertEquals("a".repeat(255), description.getValue());
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
            assertEquals("Validation error:\n - value: Invalid email address format [email.value]", exception1.getMessage());
        
            Model.Exception exception2 = assertThrows(Model.Exception.class, () ->
                Model.validate(
                    Email.newBuilder().setValue("invalid@.com").build()
                )
            );
            assertEquals("Validation error:\n - value: Invalid email address format [email.value]", exception2.getMessage());
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
    class IdTest {

        @Test
        void shouldCreateIdSuccessfullyWhenTypeAndValueAreValid() {
            assertDoesNotThrow(() -> 
                Model.validate(
                    Id.newBuilder()
                        .setOrderId(OrderId.newBuilder().setValue("12345678-ORDR-2023-04-18").build())
                        .build()
                )
            );

            assertDoesNotThrow(() -> 
                Model.validate(
                    Id.newBuilder()
                        .setOrderItemId(OrderItemId.newBuilder().setValue("12345678-ORDI-2023-04-18").build())
                        .build()
                )
            );

            assertDoesNotThrow(() -> 
                Model.validate(
                    Id.newBuilder()
                        .setProductId(ProductId.newBuilder().setValue("12345678-PRDC-2023-04-18").build())
                        .build()
                )
            );

            assertDoesNotThrow(() -> 
                Model.validate(
                    Id.newBuilder()
                        .setIngredientId(IngredientId.newBuilder().setValue("12345678-INGR-2023-04-18").build())
                        .build()
                )
            );

            assertDoesNotThrow(() -> 
                Model.validate(
                    Id.newBuilder()
                        .setCustomerId(CustomerId.newBuilder().setValue("12345678-CUST-2023-04-18").build())
                        .build()
                )
            );

            assertDoesNotThrow(() -> 
                Model.validate(
                    Id.newBuilder()
                        .setPermissionId(PermissionId.newBuilder().setValue("12345678-PERM-2023-04-18").build())
                        .build()
                )
            );
        }

        @Test
        void shouldThrowExceptionWhenIdValueIsInvalid() {
            Model.Exception orderIdException = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Id.newBuilder()
                        .setOrderId(OrderId.newBuilder().setValue("").build())
                        .build()
                )
            );
            assertEquals("Validation error:\n - order_id.value: Invalid Order ID format [order_id.value]", orderIdException.getMessage());

            Model.Exception orderItemIdException = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Id.newBuilder()
                        .setOrderItemId(OrderItemId.newBuilder().setValue(" ").build())
                        .build()
                )
            );
            assertEquals("Validation error:\n - order_item_id.value: Invalid Order Item ID format [order_item_id.value]", orderItemIdException.getMessage());

            Model.Exception productIdException = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Id.newBuilder()
                        .setProductId(ProductId.newBuilder().setValue("invalid").build())
                        .build()
                )
            );
            assertEquals("Validation error:\n - product_id.value: Invalid Product ID format [product_id.value]", productIdException.getMessage());

            Model.Exception ingredientIdException = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Id.newBuilder()
                        .setIngredientId(IngredientId.newBuilder().setValue("").build())
                        .build()
                )
            );
            assertEquals("Validation error:\n - ingredient_id.value: Invalid Ingredient ID format [ingredient_id.value]", ingredientIdException.getMessage());

            Model.Exception customerIdException = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Id.newBuilder()
                        .setCustomerId(CustomerId.newBuilder().setValue(" ").build())
                        .build()
                )
            );
            assertEquals("Validation error:\n - customer_id.value: Invalid Customer ID format [customer_id.value]", customerIdException.getMessage());

            Model.Exception permissionIdException = assertThrows(Model.Exception.class, () -> 
                Model.validate(
                    Id.newBuilder()
                        .setPermissionId(PermissionId.newBuilder().setValue("invalid").build())
                        .build()
                )
            );
            assertEquals("Validation error:\n - permission_id.value: Invalid Permission ID format [permission_id.value]", permissionIdException.getMessage());
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
    class CustomerIdTest {

        @Test
        void shouldThrowExceptionWhenIdDoesNotFollowCustomerIdPattern() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(CustomerId.newBuilder().setValue("invalid-id").build())
            );
            assertEquals("Validation error:\n - value: Invalid Customer ID format [customer_id.value]", exception.getMessage());
        }
    
        @Test
        void shouldCreateCustomerIDWhenIdIsValid() {
            String validId = "12345678-CUST-2023-04-18";
            assertDoesNotThrow(() -> 
                Model.validate(CustomerId.newBuilder().setValue(validId).build())
            );
        }
    
        @Test
        void shouldCompareCustomerIdsBasedOnId() {
            CustomerId customerID1 = (CustomerId) Model.validate(CustomerId.newBuilder().setValue("12345678-CUST-2023-04-18").build());
            CustomerId customerID2 = (CustomerId) Model.validate(CustomerId.newBuilder().setValue("12345678-CUST-2023-04-18").build());
            CustomerId customerID3 = (CustomerId) Model.validate(CustomerId.newBuilder().setValue("87654321-CUST-2023-04-18").build());
    
            assertEquals(customerID1, customerID2);
            assertNotEquals(customerID1, customerID3);
        }
    }

    @Nested
    class IngredientIdTest {

        @Test
        void shouldThrowExceptionWhenIdDoesNotFollowIngredientIdPattern() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(IngredientId.newBuilder().setValue("invalid-id").build())
            );
            assertEquals("Validation error:\n - value: Invalid Ingredient ID format [ingredient_id.value]", exception.getMessage());
        }
    
        @Test
        void shouldCreateIngredientIdWhenIdIsValid() {
            String validId = "12345678-INGR-2023-04-18";
            assertDoesNotThrow(() -> 
                Model.validate(IngredientId.newBuilder().setValue(validId).build())
            );
        }
    
        @Test
        void shouldCompareIngredientIdsBasedOnId() {
            IngredientId ingredientID1 = (IngredientId) Model.validate(IngredientId.newBuilder().setValue("12345678-INGR-2023-04-18").build());
            IngredientId ingredientID2 = (IngredientId) Model.validate(IngredientId.newBuilder().setValue("12345678-INGR-2023-04-18").build());
            IngredientId ingredientID3 = (IngredientId) Model.validate(IngredientId.newBuilder().setValue("87654321-INGR-2023-04-18").build());
    
            assertEquals(ingredientID1, ingredientID2);
            assertNotEquals(ingredientID1, ingredientID3);
        }
    }

    @Nested
    class OrderItemIdTest {

        @Test
        void shouldThrowExceptionWhenIdDoesNotFollowOrderItemIdPattern() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(OrderItemId.newBuilder().setValue("invalid-id").build())
            );
            assertEquals("Validation error:\n - value: Invalid Order Item ID format [order_item_id.value]", exception.getMessage());
        }
    
        @Test
        void shouldCreateOrderItemIdWhenIdIsValid() {
            String validId = "12345678-ORDI-2023-04-18";
            assertDoesNotThrow(() -> 
                Model.validate(OrderItemId.newBuilder().setValue(validId).build())
            );
        }
    
        @Test
        void shouldCompareOrderItemIdsBasedOnId() {
            OrderItemId orderItemID1 = (OrderItemId) Model.validate(OrderItemId.newBuilder().setValue("12345678-ORDI-2023-04-18").build());
            OrderItemId orderItemID2 = (OrderItemId) Model.validate(OrderItemId.newBuilder().setValue("12345678-ORDI-2023-04-18").build());
            OrderItemId orderItemID3 = (OrderItemId) Model.validate(OrderItemId.newBuilder().setValue("87654321-ORDI-2023-04-18").build());
    
            assertEquals(orderItemID1, orderItemID2);
            assertNotEquals(orderItemID1, orderItemID3);
        }
    }

    @Nested
    class OrderIdTest {

        @Test
        void shouldThrowExceptionWhenIdDoesNotFollowOrderIdPattern() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(OrderId.newBuilder().setValue("invalid-id").build())
            );
            assertEquals("Validation error:\n - value: Invalid Order ID format [order_id.value]", exception.getMessage());
        }
    
        @Test
        void shouldCreateOrderIdWhenIdIsValid() {
            String validId = "12345678-ORDR-2023-04-18";
            assertDoesNotThrow(() -> 
                Model.validate(OrderId.newBuilder().setValue(validId).build())
            );
        }
    
        @Test
        void shouldCompareOrderIdsBasedOnId() {
            OrderId orderID1 = (OrderId) Model.validate(OrderId.newBuilder().setValue("12345678-ORDR-2023-04-18").build());
            OrderId orderID2 = (OrderId) Model.validate(OrderId.newBuilder().setValue("12345678-ORDR-2023-04-18").build());
            OrderId orderID3 = (OrderId) Model.validate(OrderId.newBuilder().setValue("87654321-ORDR-2023-04-18").build());
    
            assertEquals(orderID1, orderID2);
            assertNotEquals(orderID1, orderID3);
        }
    }

    @Nested
    class PermissionIdTest {

        @Test
        void shouldThrowExceptionWhenIdDoesNotFollowPermissionIdPattern() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(PermissionId.newBuilder().setValue("invalid-id").build())
            );
            assertEquals("Validation error:\n - value: Invalid Permission ID format [permission_id.value]", exception.getMessage());
        }
    
        @Test
        void shouldCreatePermissionIdWhenIdIsValid() {
            String validId = "12345678-PERM-2023-04-18";
            assertDoesNotThrow(() -> 
                Model.validate(PermissionId.newBuilder().setValue(validId).build())
            );
        }
    
        @Test
        void shouldComparePermissionIdsBasedOnId() {
            PermissionId PermissionID1 = (PermissionId) Model.validate(PermissionId.newBuilder().setValue("12345678-PERM-2023-04-18").build());
            PermissionId PermissionID2 = (PermissionId) Model.validate(PermissionId.newBuilder().setValue("12345678-PERM-2023-04-18").build());
            PermissionId PermissionID3 = (PermissionId) Model.validate(PermissionId.newBuilder().setValue("87654321-PERM-2023-04-18").build());
    
            assertEquals(PermissionID1, PermissionID2);
            assertNotEquals(PermissionID1, PermissionID3);
        }
    }

    @Nested
    class ProductIdTest {

        @Test
        void shouldThrowExceptionWhenIdDoesNotFollowProductIdPattern() {
            Model.Exception exception = assertThrows(Model.Exception.class, () -> 
                Model.validate(ProductId.newBuilder().setValue("invalid-id").build())
            );
            assertEquals("Validation error:\n - value: Invalid Product ID format [product_id.value]", exception.getMessage());
        }
    
        @Test
        void shouldCreateProductIdWhenIdIsValid() {
            String validId = "12345678-PRDC-2023-04-18";
            assertDoesNotThrow(() -> 
                Model.validate(ProductId.newBuilder().setValue(validId).build())
            );
        }
    
        @Test
        void shouldCompareProductIdsBasedOnId() {
            ProductId productID1 = (ProductId) Model.validate(ProductId.newBuilder().setValue("12345678-PRDC-2023-04-18").build());
            ProductId productID2 = (ProductId) Model.validate(ProductId.newBuilder().setValue("12345678-PRDC-2023-04-18").build());
            ProductId productID3 = (ProductId) Model.validate(ProductId.newBuilder().setValue("87654321-PRDC-2023-04-18").build());
    
            assertEquals(productID1, productID2);
            assertNotEquals(productID1, productID3);
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
}