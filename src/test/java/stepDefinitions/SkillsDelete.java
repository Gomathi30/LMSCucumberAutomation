package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ExcelHandler;
import utils.PropertiesFileReader;

public class SkillsDelete {
	RequestSpecification request;
	Response response;

	PropertiesFileReader prop= new PropertiesFileReader();
	String username=prop.getUsername();
	String password=prop.getPassword();
	String baseURI=prop.getBaseURI();
	String endPoint=prop.getSkillsEndpoint();

	String sheetName=prop.getSheetNameDelete();

	static int row=1;
	Object id;
	// String projectPath=System.getProperty("user.dir");
	// String filepath = "/src/test/resources/data/SkillsTestData.xlsx";
	String filepath ="/Users/suresh/eclipse-workspace/LMSCucumberAutomation/src/test/resources/data/SkillsTestData.xlsx";
	ExcelHandler excel= new ExcelHandler(filepath, sheetName);

	@Given("user is on delete method in skills Api")
	public void user_is_on_delete_method_in_skills_api() {
		System.out.println("excel:"+ filepath +"\n"+ sheetName);
		request=given(); 
	}
	//comment
	@When("user sends delete request with valid skill id")
	public void user_sends_delete_request_with_valid_skill_id() {
		id = excel.getCellData(row, 0);
		row++;
		System.out.println("ID:"+id);
		System.out.println(baseURI+endPoint+"/"+id);
		//hardcorded skills
		response= request.auth().basic(username, password).delete(baseURI+endPoint+"/"+id);

	}

	@Then("the skill id is deleted with response code {int}")
	public void the_skill_id_is_deleted_with_response_code(Integer statusCode) {
		response.then().extract().response();
		System.out.println("response is :"+ response.asString());
		Assert.assertEquals(response.getStatusCode(), statusCode);
		//Assert.assertTrue(response.asString().contains("The record has been deleted"));

		//DB Validation
		String skillId = response.then().extract().path("skill_id");
		Response getReponseAfterPost = given().auth().basic(username, password)
				.get(baseURI + endPoint + "/" +id); 


		getReponseAfterPost.then().assertThat().statusCode(404);

	}



	@When("user sends delete request with non existing skill id")
	public void user_sends_delete_request_with_non_existing_skill_id() {
		System.out.println("Row value in 2 method: "+ row);
		id = excel.getCellData(2, 0);
		System.out.println("ID:"+id);
		response= request.auth().basic(username, password).delete(baseURI+endPoint+"/"+id);

	}

	@Then("{int} not found error response code is displayed")
	public void not_found_error_response_code_is_displayed(Integer statusCode) {
		response.then().extract().response();
		Assert.assertEquals(response.getStatusCode(), statusCode);
		Assert.assertTrue(response.asString().contains("Not Found"));

	}


	@When("user sends delete request without skill id")
	public void user_sends_delete_request_without_skill_id() {
		response= request.auth().basic(username, password).delete(baseURI+endPoint);

	}

	@Then("{int} method not found error is displayed in skills")
	public void method_not_found_error_is_displayed_in_skills(Integer statusCode) {
		response.then().extract().response();
		Assert.assertEquals(response.getStatusCode(), statusCode);
		Assert.assertTrue(response.asString().contains("DELETE is not one of the supported Http Methods"));

	}
}
