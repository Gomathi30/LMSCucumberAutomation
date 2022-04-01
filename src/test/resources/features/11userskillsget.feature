Feature: As a tester I want to test UserSkills API so that I can validate GET method
Background: The Authorization as basic auth with username : APIPROCESSING and password : 2xx@Success


  Scenario: Test the Get request for all users of UserSkills API
    Given user is on the Get method in UserSkills Api
    When user sends get request with valid auth for all users in UserSkill Api
    Then userskill details are displayed with 200 OK message

  Scenario: Test the Api for get request for single user in UserSkills API
    Given user is on the Get method in UserSkills Api
    When user sends get request with valid user skill id 
    Then single user skill details are displayed with 200 OK message
    
  Scenario: Test the Get request for User API without authorization in UserSkills API
    Given user is on the Get method in UserSkills Api
    When user sends get request with no authorization in UserSkills Api
    Then 401 unathorized status code should be returned in userskills 
    
 Scenario: Test the Api for get request with invalid id for userskills
    Given user is on the Get method in UserSkills Api
    When user sends get request for invalid id in userskills 
    Then 404 Not found message is returned
