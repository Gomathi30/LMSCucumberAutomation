package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ExcelHandler;
import utils.PropertiesFileReader;
import static io.restassured.RestAssured.* ;
import static org.hamcrest.Matchers.*;

import org.json.simple.JSONObject;
import org.testng.Assert;

import io.restassured.module.jsv.JsonSchemaValidator;


public class SkillsPost {

	RequestSpecification request ;
	Response response;

	static int row=1;
	int skill_id;
	Object skill_name;
	JSONObject json;

	String projectPath=System.getProperty("user.dir");
	String filepath = "/src/test/resources/data/SkillsTestData.xlsx";

	PropertiesFileReader prop= new PropertiesFileReader();
	String username=prop.getUsername();
	String password=prop.getPassword();
	String baseURI=prop.getBaseURI();
	String endPoint=prop.getSkillsEndpoint();
	//String excelPath=prop.getSkillsExcelPath();
	String sheetName=prop.getSheetNamePost();
	ExcelHandler excel= new ExcelHandler(projectPath+filepath, sheetName);


	@Given("User is in POST method")
	public void user_is_in_post_method() {
		System.out.println("Endpoint:"+endPoint );
		request=given();
	}

	@When("User sends POST request with Input Skill_name")
	public void user_sends_post_request_with_input_skill_name() {

		json = new JSONObject();
		skill_name= excel.getCellData(row,1);
		json.put("skill_name", skill_name);
		System.out.println("Skill name is :"+skill_name);
		response= request.auth().preemptive().basic(username,password).contentType("application/json").
				when().body(json.toJSONString()).post(baseURI+endPoint);
		System.out.println("Reponse:"+response.asPrettyString());

	}

	@Then("The response status {int} with the message successfully created")
	public void the_response_status_with_the_message_successfully_created(Integer status) {
		//json schema validation
		response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/skillspostschema.json")).extract().response();

		skill_id = response.jsonPath().getInt("skill_id");

		//response body validation
		Assert.assertEquals(status, response.getStatusCode());
		Assert.assertNotNull(skill_id );
		Assert.assertEquals(response.jsonPath().getString("skill_name"),skill_name);


		//updating user id in put/post/get/delete methods in excel
		excel.setCellData(1, 0, skill_id,"Post");
		excel.setCellData(1, 0, skill_id,"Get");
		excel.setCellData(1, 0, skill_id,"Put");
		excel.setCellData(1, 0, skill_id,"Delete");
		excel.close();
		
		//DB validation
		int skillId  = response.then().extract().path("skill_id");
		System.out.println("Auto generated UserId: " + skill_id );
		Response getReponseAfterPost = request.given().auth().basic(username, password)
				.get(baseURI + endPoint + "/" +skill_id);
		getReponseAfterPost.then().assertThat().statusCode(200).and().body("skill_id", equalTo(skillId));

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getMessage();
		}
	}

	@When("User sends POST request without authorization")
	public void user_sends_post_request_without_authorization() {
		row++;
		JSONObject json = new JSONObject();
		skill_name= excel.getCellData(row,1);
		json.put("skill_name", skill_name);
		System.out.println("Skill name is :"+skill_name);
		response= request.contentType("application/json").
				when().body(json.toJSONString()).post(baseURI+endPoint);
		System.out.println("Reponse:"+response.asPrettyString());

	}

	@Then("The response status {int} unauthorized is returned")
	public void the_response_status_unauthorized_is_returned(Integer statusCode) {
		Assert.assertEquals(response.getStatusCode(), statusCode);
	}

	//Skill already exists
	@When("User sends POST request with existing skill name")
	public void user_sends_post_request_with_existing_skill_name() {
		json = new JSONObject();
		skill_name= excel.getCellData(1,1);
		json.put("skill_name", skill_name);
		System.out.println("Skill name is :"+skill_name);
		response= request.auth().preemptive().basic(username,password).contentType("application/json").
				when().body(json.toJSONString()).post(baseURI+endPoint);
		System.out.println("Reponse:"+response.asPrettyString());
	}

	@Then("The response status {int} Bad request is returned")
	public void the_response_status_bad_request_is_returned(Integer statusCode) {
		Assert.assertEquals(response.getStatusCode(), statusCode);
		Assert.assertTrue(response.asString().contains("Skill already exists"));
	}


}
