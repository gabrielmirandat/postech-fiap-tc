Feature: Create Order

  Scenario: Successfully create a new order
    Given a valid create order command
    When I create a new order
    Then the order should be saved in the database
    And an order created event should be published

#  Scenario: Fail to create an order due to invalid menu
#    Given an invalid create order command
#    When I create a new order
#    Then an exception should be thrown
