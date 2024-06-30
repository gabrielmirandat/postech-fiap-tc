Feature: Create Order

  Scenario: Successfully create a new order
    Given a logged in customer user
    When create a new order
    Then the order should be saved in the database
    And an order created event should be published

  Scenario: Create order with invalid product id
    Given a logged in customer user
    When create a new order with invalid product id
    Then an error response with message "Invalid product ID" should be returned

  Scenario: Create order with invalid extra id
    Given a logged in customer user
    When create a new order with invalid extra id
    Then an error response with message "Invalid extra ID" should be returned