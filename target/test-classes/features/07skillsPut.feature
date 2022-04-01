Feature: As a tester I want to test and validate Skills API PUT method

 Background: Select Authorization Type as Basic Auth and enter valid credentials

 Scenario: Test the Skills API for put request with valid id
    Given User is in put method of skills api
    When User sends PUT request with with skill id and name
    Then The response should return status code 201 Created
    
  Scenario: Test the Skills API for put request with invalid id
    Given User is in put method of skills api
    When User sends PUT request with invalid id
    Then The response should return status code 404 Not Found
