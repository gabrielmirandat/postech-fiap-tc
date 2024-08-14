Feature: Search Order

  Scenario: Search orders by existing status
    Given an already created order
    And a logged in squad orders user
    When search for orders by status "CREATED"
    Then the order should be in the search results

  Scenario: Search orders with non-existing status
    Given an already created order
    And a logged in squad orders user
    When search for orders by status "COMPLETED"
    Then the order should not be in the search results

  Scenario: Search orders with invalid status
    Given an already created order
    And a logged in squad orders user
    When search for orders by status "INVALID"
    Then an error "400" - "No enum constant com.gabriel.orders.core.domain.model.OrderStatus.INVALID" should be returned