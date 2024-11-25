Feature: Process Order

  Scenario: Successfully process an order for pickup
    Given an already created order
    And a logged in squad orders user
    When process the order through statuses
      | PREPARATION |
      | PACKAGING   |
      | PICKUP      |
    And finishing the order
    Then the order should be updated to status "COMPLETED"

  Scenario: Successfully process an order for delivery
    Given an already created order
    And a logged in squad orders user
    When process the order through statuses
      | PREPARATION |
      | PACKAGING   |
      | PICKUP      |
      | DELIVERY    |
    And finishing the order
    Then the order should be updated to status "COMPLETED"

  Scenario: Fail to process order that is already completed
    Given an already created order
    And a logged in squad orders user
    When process the order through statuses
      | COMPLETED |
      | DELIVERY  |
    Then an error "422" - "ORD_001 - INVALID ORDER STATUS CHANGE" - "Order must be in balcony to be delivered" should be returned

  Scenario: Fail to process non-existing order
    Given a logged in squad orders user
    When process a non-existing order
    Then an error "404" - "Order not found" should be returned