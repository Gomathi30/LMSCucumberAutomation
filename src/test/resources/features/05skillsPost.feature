Feature: As a tester I want to test and validate Skills API POST method

  Background: user selected basic auth and entered valid username and password

  Scenario: To test the post method in Skills API with valid skill name
    Given User is in POST method
    When User sends POST request with Input Skill_name
    Then The response status 201 with the message successfully created

  Scenario: To test the post method in skills api without authorization
    Given User is in POST method
    When User sends POST request without authorization
    Then The response status 401 unauthorized is returned

   Scenario: To test the post method in skills api with same skill name
    Given User is in POST method
    When User sends POST request with existing skill name
    Then The response status 400 Bad request is returned