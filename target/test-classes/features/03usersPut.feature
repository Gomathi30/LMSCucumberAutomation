Feature: As a tester I want to test User API so that I can validate PUT method

  Background: The Authorization is set as basic auth with valid username and password

  Scenario: Test the Users API for put request with valid id
    Given User is in the endpoint url with valid id
    When User sends PUT request with endpoint url/users/id with updated fields and values
    Then The response should show status code 201 Created

  Scenario: Test the Users API for put request with invalid id
    Given User is in the endpoint url with invalid id
    When User sends PUT request with endpoint url/users/id with updated fields and values
    Then The response should show status code 404 Not Found
