package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ExcelHandler;
import utils.PropertiesFileReader;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;

public class UserSkillsDelete {
	RequestSpecification request;
	Response response;

	String projectPath=System.getProperty("user.dir");
	String filepath = "/src/test/resources/data/UserSkillsTestData.xlsx";

	ExcelHandler excel= new ExcelHandler(projectPath+filepath, "Delete");



	static int row=1;

	PropertiesFileReader prop= new PropertiesFileReader();
	String username=prop.getUsername();
	String password=prop.getPassword();
	String baseURI=prop.getBaseURI();
	String endPoint=prop.getuserSkillsEndPoint();
	Object userSkillId;

	@Given("User selects DELETE method in UserSkills api")
	public void user_selects_delete_method_in_user_skills_api() {
		request= given();

	}

	@When("User sends delete request with valid user skill id")
	public void user_sends_delete_request_with_valid_user_skill_id() {

		userSkillId = excel.getCellData(row, 0);
		row++;
		System.out.println("ID:"+userSkillId);
		response= request.auth().basic(username, password).
				when().delete(baseURI+endPoint+"/"+userSkillId);

	}

	@Then("The response with the status code {int} is returned")
	public void the_response_with_the_status_code_is_returned(Integer statusCode) {
		System.out.println("response is :"+ response.asString());
		Assert.assertEquals(response.getStatusCode(), statusCode);
		Assert.assertTrue(response.asString().contains("The record has been deleted"));

		//DB validation
		/*request.get(baseURI+endPoint+"/"+user_skill_id).then().assertThat().statusCode(200)
				.body("user_skill_id",hasItem(user_skill_id).toString(),"user_id",hasItem(user_id).toString(),"skill_id",hasItem(skill_id).toString(),
						"months_of_exp",hasItem(months_of_exp).toString());
				System.out.println("DB Validated and has the inserted record");*/
		String userSkillId = response.then().extract().path("user_skill_id");
		System.out.println("Auto generated skill_id: " + userSkillId );
		Response getReponseAfterPost = request.given().auth().basic(username, password)
				.get(baseURI + endPoint + "/" +userSkillId); 

		//Asserting newly created user_skill_id is present in DB by making GET call for that id
		getReponseAfterPost.then().assertThat().statusCode(404);



	}

	@When("User sends delete request with in valid user skill id")
	public void user_sends_delete_request_with_in_valid_user_skill_id() {

		System.out.println("Row value in 2 method: "+ row);
		userSkillId = excel.getCellData(2, 0);
		System.out.println("ID:"+userSkillId);
		response= request.auth().basic(username, password).delete(baseURI+endPoint+"/"+userSkillId);

	}

	@Then("{int} not found error will be returned in userskills api")
	public void not_found_error_will_be_returned_in_userskills_api(Integer statusCode) {
		Assert.assertEquals(response.getStatusCode(), statusCode);
		Assert.assertTrue(response.asString().contains("Not Found"));


	}

}
