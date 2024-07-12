Feature: Cancel Order

  Scenario: Successfully canceling an order
    Given an already created order
    And a logged in admin orders user
    When canceling the order
    Then the order should be updated to status "CANCELED"
    And an order canceled event should be published