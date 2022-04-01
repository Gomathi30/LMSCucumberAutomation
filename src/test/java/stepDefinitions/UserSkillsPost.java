package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static io.restassured.RestAssured.* ;

import org.json.simple.JSONObject;
import org.testng.Assert;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ExcelHandler;
import utils.PropertiesFileReader;
import static org.hamcrest.Matchers.*;

public class UserSkillsPost {
	RequestSpecification request ;
	Response response;
	static int row =1;
	String user_skill_id;
	Object user_id,skill_id,months_of_exp;

	//loading from properties file
	PropertiesFileReader prop= new PropertiesFileReader();
	String username=prop.getUsername();
	String password=prop.getPassword();
	String baseURI=prop.getBaseURI();
	String endPoint=prop.getuserSkillsEndPoint();

	String projectPath=System.getProperty("user.dir");
	String filepath = "/src/test/resources/data/UserSkillsTestData.xlsx";
	
	ExcelHandler excel= new ExcelHandler(projectPath+filepath, "Post");
	JSONObject json = new JSONObject();
	
	@Given("User is on post method of userskills API")
	public void user_is_on_post_method_of_userskills_api() {
		request= given();

	}

	@When("User sends post request with valid values with endpoint URL in userskills")
	public void user_sends_post_request_with_valid_values_with_endpoint_url_in_userskills() {


		user_id= excel.getCellData(row,1);
		skill_id= excel.getCellData(row,2);
		months_of_exp=excel.getCellData(row,3);


		json.put("user_id",user_id);
		json.put("skill_id",skill_id);
		json.put("months_of_exp",months_of_exp);

		response= request.auth().preemptive().basic(username, password).
				contentType("application/json").
				when().body(json.toJSONString()).
				post(baseURI+endPoint);
		++row;
	}

	@Then("The status code is {int} with success message OK")
	public void the_status_code_is_with_success_message_ok(Integer status) {

		//json schema validation
		response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/userskillspost.json")).extract().response();

		user_skill_id = response.jsonPath().getString("user_skill_id");

		//response body validation
		Assert.assertEquals(status, response.getStatusCode());
		Assert.assertNotNull(user_skill_id );
		Assert.assertEquals(response.jsonPath().getString("user_id"),user_id );
		Assert.assertEquals(response.jsonPath().getString("skill_id"),skill_id );
		Assert.assertEquals(response.jsonPath().getString("months_of_exp"),months_of_exp );


		//updating user id in put/post/get/delete methods in excel
		excel.setCellData(1, 0, user_skill_id,"Post");
		excel.setCellData(1, 0, user_skill_id,"Get");
		excel.setCellData(1, 0, user_skill_id,"Put");
		excel.setCellData(1, 0, user_skill_id,"Delete");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getMessage();
		}
		excel.close();
		
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

}
