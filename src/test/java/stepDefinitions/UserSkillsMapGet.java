package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ExcelHandler;
import utils.PropertiesFileReader;
import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertTrue;

import org.testng.Assert;

public class UserSkillsMapGet {
	RequestSpecification request;
	Response response;

	Object userid;
	Object skillid;
	PropertiesFileReader prop= new PropertiesFileReader();
	String username=prop.getUsername();
	String password=prop.getPassword();
	String baseURI=prop.getBaseURI();
	String endPoint=prop.getuserSkillMapEndPoint();
	String sheetName = prop.getSheetNameGet();
	//UserSkillsMap
	
	//String projectPath=System.getProperty("user.dir");
	
	//String filepath = "src/test/resources/data/UserSkillsmapGet.xlsx";
	String filepath="/Users/suresh/eclipse-workspace/LMSCucumberAutomation/src/test/resources/data/UserSkillsmapGet.xlsx";
	ExcelHandler excel= new ExcelHandler(filepath, "Get");

	@Given("User selects GET method in UserSkillsMap api")
	public void user_selects_get_method_in_user_skills_map_api() {
		//System.out.println("sheetName:"+sheetName +"\n" +"projectPath:"+projectPath);
		request= given();
	}

	@When("User sends GET request with endpoint UserSkillsMap")
	public void user_sends_get_request_with_endpoint_user_skills_map() {
		response=request.auth().preemptive().basic(username, password).
				when().get(baseURI+endPoint);


	}

	@Then("The response should be in JSON format with the status code {int}")
	public void the_response_should_be_in_json_format_with_the_status_code(Integer statusCode) {
		response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/userskillmapget.json")).extract().response();
		System.out.println(response.asPrettyString());
		Assert.assertEquals(statusCode,response.getStatusCode());
		//System.out.println(response.asPrettyString());

		//response body validation
		Assert.assertEquals(statusCode,response.getStatusCode());

		assertTrue(response.asString().contains("users"));
		assertTrue(response.asString().contains("id"));
		assertTrue(response.asString().contains("firstName"));
		assertTrue(response.asString().contains("skillmap"));
		assertTrue(response.asString().contains("id"));
		assertTrue(response.asString().contains("skill"));

	}

	@Given("User selects GET method and enter the endpoint")
	public void user_selects_get_method_and_enter_the_endpoint() {
		request= given();

	}

	@When("User sends GET request with userid endpoint")
	public void user_sends_get_request_with_userid_endpoint() {
		userid = excel.getCellData(1, 0);
		// userid="U772";
		response=request.auth().preemptive().basic(username, password).when().get(baseURI+"/UserSkillsMap/"+userid);
				//when().get(baseURI+endPoint+"/"+userid);
		System.out.println(response.asPrettyString());
	}


	@Then("user id details wil be returned with the status code {int}")
	public void user_id_details_wil_be_returned_with_the_status_code(Integer statusCode) {
		response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/userskillsmapusersget.json")).extract().response();
		

		//response body validation
		Assert.assertEquals(statusCode,response.getStatusCode());
		//Assert.assertEquals(userid,response.jsonPath().getString("id");
		assertTrue(response.asString().contains("users"));
		assertTrue(response.asString().contains("id"));
		assertTrue(response.asString().contains("firstName"));
		assertTrue(response.asString().contains("skillmap"));
		assertTrue(response.asString().contains("id"));
		assertTrue(response.asString().contains("skill"));

	}

	@Given("User selects GET method and enter the endpoint skills")
	public void user_selects_get_method_and_enter_the_endpoint_skills() {
		request=given();

	}

	@When("User sends GET request with skillid endpoint")
	public void user_sends_get_request_with_skillid_endpoint() {
		skillid=excel.getCellData(2, 0);
		//skillid=2;
		response=request.auth().preemptive().basic(username, password).
				when().get(baseURI+"/UsersSkillsMap/"+skillid);
	}

	@Then("skill id details will be displayed with the status code {int}")
	public void skill_id_details_will_be_displayed_with_the_status_code(Integer statusCode) {
		//usersskillmapskillsget.json 
		response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/usersskillmapskillsget.json")).extract().response();
		//System.out.println(response.asPrettyString());

		//response body validation
		Assert.assertEquals(statusCode,response.getStatusCode());	    
		assertTrue(response.asString().contains("users"));
		assertTrue(response.asString().contains("id"));
		assertTrue(response.asString().contains("firstName"));
		assertTrue(response.asString().contains("skillmap"));
		assertTrue(response.asString().contains("id"));
		assertTrue(response.asString().contains("skill"));
	}

}
