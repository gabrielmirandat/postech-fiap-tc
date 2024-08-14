Feature: Cancel Order

  Scenario: Successfully canceling an order
    Given an already created order
    And a logged in admin orders user
    When canceling the order
    Then the order should be updated to status "CANCELED"
    And an order canceled event should be published

  Scenario: Canceling non-existing order
    Given a logged in admin orders user
    When canceling a non-existing order
    Then an error "404" - "Order not found" should be returned