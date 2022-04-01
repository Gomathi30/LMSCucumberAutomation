package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertTrue;

import org.testng.Assert;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ExcelHandler;
import utils.PropertiesFileReader;

public class SkillsGet {
	RequestSpecification request;
	Response response;

	//read from propertiesfile
	PropertiesFileReader prop= new PropertiesFileReader();
	String username=prop.getUsername();
	String password=prop.getPassword();
	String baseURI=prop.getBaseURI();
	String endPoint=prop.getSkillsEndpoint();
	//String excelPath=prop.getSkillsExcelPath();
	String sheetName=prop.getSheetNameGet();

	Object id;

	String projectPath=System.getProperty("user.dir");
	String filepath = "/src/test/resources/data/SkillsTestData.xlsx";

	ExcelHandler excel= new ExcelHandler(projectPath+filepath, sheetName);

	@Given("user is on the Get method of SkillsApi")
	public void user_is_on_the_get_method_of_skills_api() {
		request=given();

	}

	@When("user sends get request for all skills with end")
	public void user_sends_get_request_for_all_skills_with_end() {
		response = request.auth().preemptive().basic(username,password).
				get(baseURI+endPoint);
		// System.out.println("User Get Response"+response.asPrettyString());

	}

	@Then("Skills details are displayed with {int} OK message")
	public void skills_details_are_displayed_with_ok_message(Integer statusCode) {
		//Schema validation
		response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/skillsgetall.json"));
		//response body validation
		Assert.assertEquals(response.getStatusCode(), statusCode);
		Assert.assertNotNull("skill_id");
		assertTrue(response.asString().contains("skill_id"));
		assertTrue(response.asString().contains("skill_name"));

	}

	@When("user sends get skills request for valid id")
	public void user_sends_get_skills_request_for_valid_id() {

		id = excel.getCellData(1, 0);
		System.out.println("ID:"+id);
		response = request.auth().preemptive().basic(username,password).
				get(baseURI+endPoint+"/"+id);
		/*response = request.auth().preemptive().basic(username,password).
	    		get(baseURI+endPoint+"/U777");*/

		System.out.println("User Get Response"+response.asPrettyString());

	}

	@Then("particular skill details are displayed with {int} OK message")
	public void particular_skill_details_are_displayed_with_ok_message(Integer statusCode) {
		response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/skillsget.json")).extract().response();

		//response body validation
		Assert.assertEquals(response.getStatusCode(), statusCode);
		Assert.assertNotNull("skill_id");
		assertTrue(response.asString().contains("skill_id"));
		assertTrue(response.asString().contains("skill_name"));

	}

	@When("user sends get request for invalid skill id")
	public void user_sends_get_request_for_invalid_skill_id() {
		id = excel.getCellData(2, 0);
		System.out.println(" invalid ID:"+id);
		response = request.auth().preemptive().basic(username,password).
				get(baseURI+endPoint+"/"+id);

	}

	@Then("single skill details are displayed with {int} Not found message")
	public void single_skill_details_are_displayed_with_not_found_message(Integer statusCode) {
		System.out.println("Inside Then Status code:"+response.getStatusCode());
		Assert.assertEquals(response.getStatusCode(), statusCode);

	}

	@When("user sends get request with no authorization for skills")
	public void user_sends_get_request_with_no_authorization_for_skills() {
		response = request.get(baseURI+endPoint);
		System.out.println("User Get Response"+response.asPrettyString());
		System.out.println("Status code:"+response.getStatusCode());


	}

	@Then("{int} unathorized status error code should be returned")
	public void unathorized_status_error_code_should_be_returned(Integer statusCode) {
		System.out.println("Inside Then Status code:"+response.getStatusCode());
		Assert.assertEquals(response.getStatusCode(), statusCode);


	}
}
