package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ExcelHandler;
import utils.PropertiesFileReader;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.*;

import org.json.simple.JSONObject;
import org.testng.Assert;

public class SkillsPut {
	RequestSpecification request;
	Response response;

	
	//creating Json for put request
	JSONObject json = new JSONObject();
	Object skill_name;
	Object skill_id;
	static int row =1; //  excel row
	
	
	//read from properties file
	PropertiesFileReader prop = new PropertiesFileReader(); 
	String username = prop.getUsername();
	String password= prop.getPassword();
	String baseURI = prop.getBaseURI();
	String endPoint = prop.getSkillsEndpoint();
	String sheetName= prop.getSheetNamePut();

	//excel path
	String projectPath=System.getProperty("user.dir");
	String filepath = "/src/test/resources/data/SkillsTestData.xlsx";
	
	ExcelHandler excel = new ExcelHandler(projectPath+filepath,sheetName);
	
	@Given("User is in put method of skills api")
	public void user_is_in_put_method_of_skills_api() {
		
		request=given();
	}

	@When("User sends PUT request with with skill id and name")
	public void user_sends_put_request_with_with_skill_id_and_name() {
		
		//read from excel
		skill_id = excel.getCellData(row, 0);
		skill_name = excel.getCellData(row, 1);
		
		//create request body
		json.put("skill_id", skill_id);
		json.put("skill_name", skill_name);
		
		response= request.auth().preemptive().basic(username, password).contentType("application/json")
		.when().body(json.toJSONString()).put(baseURI+endPoint+"/"+skill_id);
		System.out.println(response.asPrettyString());
	}

	@Then("The response should return status code {int} Created")
	public void the_response_should_return_status_code_created(Integer statusCode) {
		//Json schema validation
		response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/skillspostschema.json")).extract().response();

		//response body validation
		Assert.assertEquals(statusCode, response.getStatusCode());
		//Assert.assertEquals(Integer.parseInt(skill_id.toString()), response.jsonPath().getInt("skill_id"));
		Assert.assertEquals(skill_id, response.jsonPath().getString("skill_id"));

		Assert.assertEquals(skill_name, response.jsonPath().getString("skill_name"));
		//Assert.assertTrue(response.jsonPath().getString("message").contains("Successfully Updated !!"));
		
		//DB Validation
		int skillId = response.then().extract().path("skill_id");
		Response getReponseAfterPost = given().auth().basic(username, password)
				.get(baseURI + endPoint + "/" +skill_id); 
		
		
		getReponseAfterPost.then().assertThat().statusCode(200).and().body(("skill_id"), equalTo(skillId),"skill_name",equalTo(skill_name));
		
	}

	@When("User sends PUT request with invalid id")
	public void user_sends_put_request_with_invalid_id() {
		++row;
		skill_id = excel.getCellData(row, 0);
		skill_name = excel.getCellData(row, 1);
		json.put("skill_id", skill_id);
		json.put("skill_name", skill_name);
		response= request.auth().preemptive().basic(username, password).contentType("application/json").
				when().body(json.toJSONString()).put(baseURI+endPoint+"/"+skill_id);
		System.out.println(response.asPrettyString());
	}

	@Then("The response should return status code {int} Not Found")
	public void the_response_should_return_status_code_not_found(Integer statusCode) {
		Assert.assertEquals(statusCode, response.getStatusCode());
	}


}
