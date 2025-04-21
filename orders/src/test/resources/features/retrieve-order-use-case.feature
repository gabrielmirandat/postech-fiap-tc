Feature: Retrieve Order

  Scenario: Successfully retrieve an existing order
    Given an already created order
    And a logged in customer user
    When retrieving an existing order
    Then the order should be retrieved from the database

  Scenario: Retrieve order with non-existing ticket Id
    Given an already created order
    And a logged in customer user
    When retrieving an order with non-existing ticket Id
    Then an error "404" - "Order not found" should be returned