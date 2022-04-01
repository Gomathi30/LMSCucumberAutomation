Feature: As a tester I want to test and validate the GET method in UserSkillsMap API
Background: The Authorization as basic auth with username : APIPROCESSING and password : 2xx@Success

Scenario: To get all the Users and their associated Skills  from UserSkillsMap API
Given  User selects GET method in UserSkillsMap api
When User sends GET request with endpoint UserSkillsMap
Then The response should be in JSON format with the status code 200

Scenario: To get a particular  User and their associated Skills  from UserSkillsMap API
Given User selects GET method and enter the endpoint 
When User sends GET request with userid endpoint
Then user id details wil be returned with the status code 200

Scenario: To get all users of a  particular  Skill in UserSkillsMap API
Given User selects GET method and enter the endpoint skills
When User sends GET request with skillid endpoint
Then skill id details will be displayed with the status code 200


 





