package stepDefinitions;

import io.cucumber.java.AfterStep;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.specification.*;

import io.restassured.response.*;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import org.testng.Assert;

import io.restassured.module.jsv.JsonSchemaValidator;
//import  io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import utils.*;



public class UsersGet {

	RequestSpecification request;
	Response response;

	//load from properties file
	PropertiesFileReader prop= new PropertiesFileReader();
	String username=prop.getUsername();
	String password=prop.getPassword();
	String baseURI=prop.getBaseURI();
	String endPoint=prop.getUsersEndPoint();
	String sheetName= prop.getSheetNameGet();
	Object id;
	int row=1; //excel row

	String projectPath=System.getProperty("user.dir");
	String filepath = "/src/test/resources/data/UsersTestData.xlsx";

	ExcelHandler excel= new ExcelHandler(projectPath+filepath, sheetName);

	
	@Given("user is on the Get method")
	public void user_is_on_the_get_method() {
		request =  given();
	}

	@When("user sends get request with valid authorization for all users")
	public void user_sends_get_request_with_valid_authorization_for_all_users() {

		//  response = request.auth().preemptive().basic("APIPROCESSING", "2xx@Success").
		//		get("https://springboot-lms-userskill.herokuapp.com/Users");
		response = request.auth().preemptive().basic(username,password).
				get(baseURI+endPoint);
		//System.out.println("User Get Response"+response.asPrettyString());

	}

	@Then("user details are displayed with {int} OK message")
	public void user_details_are_displayed_with_ok_message(Integer statusCode) {
		//Schema validation
		response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/usersSchema.json")).extract().response();
		//response body validation
		Assert.assertEquals(response.getStatusCode(), statusCode);
		Assert.assertNotNull("user_id");
		assertTrue(response.asString().contains("name"));
		assertTrue(response.asString().contains("phone_number"));
		assertTrue(response.asString().contains("location"));
		assertTrue(response.asString().contains("time_zone"));
		assertTrue(response.asString().contains("linkedin_url"));
	}

	@When("user sends get request for valid id with valid authorization")
	public void user_sends_get_request_for_valid_id_with_valid_authorization() {

		//reading user id data from excel

		id = excel.getCellData(1, 0);
		System.out.println("ID:"+id);
		response = request.auth().preemptive().basic(username,password).
				get(baseURI+endPoint+"/"+id);
		/*response = request.auth().preemptive().basic(username,password).
	    		get(baseURI+endPoint+"/U777");*/
		
		System.out.println("User Get Response"+response.asPrettyString());

	}

	@Then("single user details are displayed with {int} OK message")
	public void single_user_details_are_displayed_with_ok_message(Integer statusCode) {
		//json schema validation
		response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/userSchemaSingleUser.json")).extract().response();

		//response body validation
		Assert.assertEquals(response.getStatusCode(), statusCode);
		Assert.assertNotNull("user_id");
		assertTrue(response.asString().contains("name"));
		assertTrue(response.asString().contains("phone_number"));
		assertTrue(response.asString().contains("location"));
		assertTrue(response.asString().contains("time_zone"));
		assertTrue(response.asString().contains("linkedin_url"));
	}


	@When("user sends get request with no authorization")
	public void user_sends_get_request_with_no_authorization() {

		response = request.get(baseURI+endPoint);
		System.out.println("User Get Response"+response.asPrettyString());
		System.out.println("Status code:"+response.getStatusCode());

	}

	@Then("{int} unathorized status code should be returned")
	public void unathorized_status_code_should_be_returned(Integer statusCode) {
		System.out.println("Inside Then Status code:"+response.getStatusCode());
		Assert.assertEquals(response.getStatusCode(), statusCode);

	}

	@When("user sends get request for invalid id")
	public void user_sends_get_request_for_invalid_id() {

		id = excel.getCellData(2, 0);
		System.out.println(" invalid ID:"+id);
		response = request.auth().preemptive().basic(username,password).
				get(baseURI+endPoint+"/"+id);
	}

	@Then("single user details are displayed with {int} Not found message")
	public void single_user_details_are_displayed_with_not_found_message(Integer statusCode) {
		System.out.println("Inside Then Status code:"+response.getStatusCode());
		Assert.assertEquals(response.getStatusCode(), statusCode);
	}

}
