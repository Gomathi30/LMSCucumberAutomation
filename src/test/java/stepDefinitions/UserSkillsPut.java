package stepDefinitions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.json.simple.JSONObject;
import org.testng.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ExcelHandler;
import utils.PropertiesFileReader;

public class UserSkillsPut {

	RequestSpecification request ;
	Response response;

	//loading from properties file
	PropertiesFileReader prop= new PropertiesFileReader();
	String username=prop.getUsername();
	String password=prop.getPassword();
	String baseURI=prop.getBaseURI();
	String endPoint=prop.getuserSkillsEndPoint();

	JSONObject json=new JSONObject();
	Object user_skill_id,user_id,skill_id,months_of_exp;
	String projectPath=System.getProperty("user.dir");
	String filepath = "/src/test/resources/data/UserSkillsTestData.xlsx";
	//instance for excel reader file
	ExcelHandler excel= new ExcelHandler(projectPath+filepath, "Put");
	static int row =1;
	@Given("User selects put method in UserSkills api")
	public void user_selects_put_method_in_user_skills_api() {
		request= given();

	}

	@When("User sends put request with valid user skill id")
	public void user_sends_put_request_with_valid_user_skill_id() {
		user_skill_id = excel.getCellData(row, 0);
		user_id= excel.getCellData(row,1);
		skill_id= excel.getCellData(row,2);
		months_of_exp=excel.getCellData(row,3);

		json.put("user_skill_id", user_skill_id);
		json.put("user_id",user_id);
		json.put("skill_id",skill_id);
		json.put("months_of_exp",months_of_exp);

		response= request.auth().preemptive().basic(username, password).
				contentType("application/json").
				when().body(json.toJSONString()).
				put(baseURI+endPoint+"/"+user_skill_id);
		++row;
	}

	@Then("The {int} status code is returned in UserSkills api")
	public void the_status_code_is_returned_in_user_skills_api(Integer statusCode) {
		//json schema validation
		//	response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/userskillspost.json")).extract().response();
		System.out.println(response.asString());
		//response body validation	
		Assert.assertEquals(statusCode, response.getStatusCode());
		System.out.println("User id after json path"+response.jsonPath().getString("user_id"));
		Assert.assertEquals(response.jsonPath().getString("user_skill_id"),user_skill_id );
		Assert.assertEquals(response.jsonPath().getString("user_id"),user_id );
		Assert.assertEquals(response.jsonPath().getString("skill_id"),skill_id );
		Assert.assertEquals(response.jsonPath().getString("months_of_exp"),months_of_exp );

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
		getReponseAfterPost.then().assertThat().statusCode(200).and().body("user_skill_id", equalTo(userSkillId));



	}

	@When("User sends put request with invalid user skill id")
	public void user_sends_put_request_with_invalid_user_skill_id() {
		user_skill_id = excel.getCellData(row, 0);
		user_id= excel.getCellData(row,1);
		skill_id= excel.getCellData(row,2);
		months_of_exp=excel.getCellData(row,3);

		json.put("user_skill_id", user_skill_id);
		json.put("user_id",user_id);
		json.put("skill_id",skill_id);
		json.put("months_of_exp",months_of_exp);
		response= request.auth().preemptive().basic(username, password).
				contentType("application/json").
				when().body(json.toJSONString()).
				put(baseURI+endPoint+"/"+user_skill_id);
		++row;

	}

	@Then("The response should show status code {int} Not Found in UserSkills api")
	public void the_response_should_show_status_code_not_found_in_user_skills_api(Integer statuscode) {
		Assert.assertEquals(statuscode, response.getStatusCode());

	}
}
