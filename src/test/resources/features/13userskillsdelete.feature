Feature: As a tester I want to test UserSkills API so that I can validate DELETE method

  Background: The Authorization as basic auth with username : APIPROCESSING and password : 2xx@Success

  Scenario: Test the deletion of the Existing User_Skill_Id
    Given User selects DELETE method in UserSkills api
    When User sends delete request with valid user skill id
    Then The response with the status code 200 is returned

  Scenario: Test the deletion of the Non-Existing User_Skill_Id
    Given User selects DELETE method in UserSkills api
    When User sends delete request with in valid user skill id
    Then 404 not found error will be returned in userskills api
