Feature: As a user, I want to test and validate the delete request of Skills API

  Background: Select Authorization Type as Basic Auth and enter valid credentials

  Scenario: Test the delete method for valid skill id
    Given user is on delete method in skills Api
    When user sends delete request with valid skill id
    Then the skill id is deleted with response code 200

  Scenario: Test the delete method for nonexistant skill id
    Given user is on delete method in skills Api
    When user sends delete request with non existing skill id
    Then 404 not found error response code is displayed 

  Scenario: Test the delete method by not giving skill id in the endpoint
    Given user is on delete method in skills Api
    When user sends delete request without skill id
    Then 405 method not found error is displayed in skills
