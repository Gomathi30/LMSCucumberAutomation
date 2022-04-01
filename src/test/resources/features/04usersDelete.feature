Feature: As a user, I want to test the delete request of Users API

  Background: Select Authorization Type as Basic Auth and enter valid credentials

  Scenario: Test the delete method for valid user id
    Given user is on delete method
    When user sends delete request with valid user id
    Then the user id is deleted with response code 200

  Scenario: Test the delete method for nonexistant user id
    Given user is on delete method
    When user sends delete request with non existing user id
    Then 404 not found response code is displayed

  Scenario: Test the delete method by not giving userid in the endpoint
    Given user is on delete method
    When user sends delete request without userid
    Then 405 method not found error is displayed
