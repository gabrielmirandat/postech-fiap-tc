Feature: Create Order

  Scenario: Successfully create a new order
    Given a logged in customer user
    When create a new order
    Then the order should be saved in the database
    And an order created event should be published

  Scenario: Create order with non-existing product
    Given a logged in customer user
    When create a new order with non-existing product
    Then an error "ORD_100 - INVALID PRODUCT" - "Product not found" should be returned

  Scenario: Create order with non-existing extra
    Given a logged in customer user
    When create a new order with non-existing extra
    Then an error "ORD_101 - INVALID EXTRA" - "Extra not found" should be returned