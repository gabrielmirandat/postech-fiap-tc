Feature: Create Order

  Scenario: Successfully create a new order
    Given a valid create order command
    When I create a new order
    Then the order should be saved in the database
    And an order created event should be published

  Scenario: Fail to create an order due to invalid menu product item
    Given a create order command with invalid product id
    When I create a new order
    Then an exception should be thrown with message "Product not found"

  Scenario: Fail to create an order due to invalid menu extra item
    Given a create order command with invalid extra id
    When I create a new order
    Then an exception should be thrown with message "Extra not found"
