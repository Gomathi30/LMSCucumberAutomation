Feature: As a tester I want to test UserSkills API so that I can validate Put method

  Background: The Authorization as basic auth with username : APIPROCESSING and password : 2xx@Success

  Scenario: To test the put method of Userskills API
    Given User selects put method in UserSkills api
    When User sends put request with valid user skill id
    Then The 201 status code is returned in UserSkills api

  Scenario: To test the put method of Userskills API  for invalid id
    Given User selects put method in UserSkills api
    When User sends put request with invalid user skill id
    Then The response should show status code 404 Not Found in UserSkills api
