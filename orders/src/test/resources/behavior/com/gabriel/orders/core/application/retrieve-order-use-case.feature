Feature: Retrieve Order

  Scenario: Successfully retrieve an existing order
    Given a logged in customer user
    When retrieve an existing order
    Then the order should be retrieved from the database

  Scenario: Retrieve order with non-existing ticket ID
    Given a logged in customer user
    When retrieve an order with non-existing ticket ID
    Then an error "ORD_404 - ORDER NOT FOUND" - "Order not found" should be returned