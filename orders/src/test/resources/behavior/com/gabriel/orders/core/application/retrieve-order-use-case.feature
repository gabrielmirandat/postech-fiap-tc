Feature: Retrieve Order

  Scenario: Successfully retrieve an existing order
    Given a logged in customer user
    And an order was created
    When retrieving an existing order
    Then the order should be retrieved from the database

  Scenario: Retrieve order with non-existing ticket ID
    Given a logged in customer user
    When retrieving an order with non-existing ticket ID
    Then an error message "Order not found" should be returned