package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.specification.*;

import io.restassured.response.*;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.Assert;

import io.restassured.module.jsv.JsonSchemaValidator;
//import  io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import utils.*;

public class UserSkillsGet {

	RequestSpecification request;
	Response response;

	PropertiesFileReader prop= new PropertiesFileReader();
	String username=prop.getUsername();
	String password=prop.getPassword();
	String baseURI=prop.getBaseURI();
	String endPoint=prop.getuserSkillsEndPoint();
	Object user_skill_id;

	String projectPath=System.getProperty("user.dir");
	String filepath = "/src/test/resources/data/UserSkillsTestData.xlsx";

	ExcelHandler excel= new ExcelHandler(projectPath+filepath, "Get");

	@Given("user is on the Get method in UserSkills Api")
	public void user_is_on_the_get_method_in_user_skills_api() {
		request =  given();
	}

	@When("user sends get request with valid auth for all users in UserSkill Api")
	public void user_sends_get_request_with_valid_auth_for_all_users_in_user_skill_api() {

		response = request.auth().preemptive().basic(username,password).
				get(baseURI+endPoint);
		//System.out.println("User Get Response"+response.asPrettyString());

	}

	@Then("userskill details are displayed with {int} OK message")
	public void userskill_details_are_displayed_with_ok_message(Integer statusCode) {
		//Schema validation
		response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/usserskillgetall.json")).extract().response();
		//response body validation
		Assert.assertEquals(response.getStatusCode(), statusCode);
		Assert.assertNotNull("user_skill_id");
		assertTrue(response.asString().contains("user_skill_id"));
		assertTrue(response.asString().contains("user_id"));
		assertTrue(response.asString().contains("skill_id"));
		assertTrue(response.asString().contains("months_of_exp"));

	}

	@When("user sends get request with valid user skill id")
	public void user_sends_get_request_with_valid_user_skill_id() {

		//reading user id data from excel

		user_skill_id = excel.getCellData(1, 0);
		System.out.println("ID:"+user_skill_id);
		response = request.auth().preemptive().basic(username,password).
				get(baseURI+endPoint+"/"+user_skill_id);
		/*response = request.auth().preemptive().basic(username,password).
		    		get(baseURI+endPoint+"/U777");*/

		System.out.println("User Get Response"+response.asPrettyString());

	}

	@Then("single user skill details are displayed with {int} OK message")
	public void single_user_skill_details_are_displayed_with_ok_message(Integer statusCode) {
		//json schema validation
		response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/userskillsget.json")).extract().response();

		//response body validation
		Assert.assertEquals(response.getStatusCode(), statusCode);
		Assert.assertNotNull("user_skill_id");
		assertTrue(response.asString().contains("user_skill_id"));
		assertTrue(response.asString().contains("user_id"));
		assertTrue(response.asString().contains("skill_id"));
		assertTrue(response.asString().contains("months_of_exp"));
	}


	@When("user sends get request with no authorization in UserSkills Api")
	public void user_sends_get_request_with_no_authorization_in_user_skills_api() {

		response = request.get(baseURI+endPoint);
		System.out.println("User Get Response"+response.asPrettyString());
		System.out.println("Status code:"+response.getStatusCode());

	}

	@Then("{int} unathorized status code should be returned in userskills")
	public void unathorized_status_code_should_be_returned_in_userskills(Integer statusCode) {
		System.out.println("Inside Then Status code:"+response.getStatusCode());
		Assert.assertEquals(response.getStatusCode(), statusCode);

	}

	@When("user sends get request for invalid id in userskills")
	public void user_sends_get_request_for_invalid_id_in_userskills() {

		user_skill_id = excel.getCellData(2, 0);
		System.out.println(" invalid ID:"+user_skill_id);
		response = request.auth().preemptive().basic(username,password).
				get(baseURI+endPoint+"/"+user_skill_id);
	}

	@Then("{int} Not found message is returned")
	public void not_found_message_is_returned(Integer statusCode) {
		System.out.println("Inside Then Status code:"+response.getStatusCode());
		Assert.assertEquals(response.getStatusCode(), statusCode);
	}

}

