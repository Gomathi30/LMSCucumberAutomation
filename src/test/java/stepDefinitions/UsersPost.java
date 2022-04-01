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
import static org.hamcrest.Matchers.equalTo;

public class UsersPost {

	RequestSpecification request ;
	Response response;
	Object name,location,timeZone,url,pg, ug,visaStatus,comments;
	Object phone; 
	static int row =1;
	String user_id;
	
	PropertiesFileReader prop= new PropertiesFileReader();
	String username=prop.getUsername();
	String password=prop.getPassword();
	String baseURI=prop.getBaseURI();
	String endPoint=prop.getUsersEndPoint();

	String projectPath=System.getProperty("user.dir");
	String filepath = "/src/test/resources/data/UsersTestData.xlsx";
	ExcelHandler excel= new ExcelHandler(projectPath+filepath, "Post");


	@Given("User is on post method of users API")
	public void user_is_on_post_method_of_users_api() {
		request= given();
	}

	@When("User sends post request with valid values with endpoint URL")
	public void user_sends_post_request_with_valid_values_with_endpoint_url() {
		/*{

	"name":"gom,kan",
	"phone_number":34675543,
	"location":"Pittsburgh",
	"time_zone":"EST",
	"linkedin_url":"www.linkedin.com/in/gomkan",
	"education_ug":"UG",
	"education_pg":"PG",
	"visa_status":"H4",
	"comments":"Through Post"
} */
		JSONObject json = new JSONObject();
		// userid=  excel.getCellData(row, 0);
		name= excel.getCellData(row,1);
		phone= excel.getCellData(row,2);
		location = excel.getCellData(row,3);
		timeZone= excel.getCellData(row,4);
		url = excel.getCellData(row,5);
		ug= excel.getCellData(row,6);
		pg= excel.getCellData(row,7);
		visaStatus= excel.getCellData(row,8);
		comments= excel.getCellData(row, 9);

		// json.put("user_id", userid);
		json.put("name",name );
		json.put("phone_number", phone);
		json.put("location",location);
		json.put("time_zone", timeZone);
		json.put("linkedin_url", url);
		json.put("education_ug", ug);
		json.put("education_pg", pg);
		json.put("visa_status", visaStatus);
		json.put("comments", comments);
		System.out.println("Name from excel:"+name);
		response= request.auth().preemptive().basic(username,password).contentType("application/json").
				when().body(json.toJSONString()).post(baseURI+endPoint);
		System.out.println("Reponse:"+response.asPrettyString());

	}

	@Then("The status code is {int} with success message")
	public void the_status_code_is_with_success_message(Integer status) {
		//json schema validation
		response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/usersputschema.json")).extract().response();

		user_id = response.jsonPath().getString("user_id");

		//response body validation
		Assert.assertEquals(status, response.getStatusCode());
		Assert.assertNotNull(user_id );
		Assert.assertEquals(response.jsonPath().getString("name"),name );
		Assert.assertEquals(response.jsonPath().getString("phone_number"),phone );
		Assert.assertEquals(response.jsonPath().getString("location"),location );
		Assert.assertEquals(response.jsonPath().getString("time_zone"),timeZone );
		Assert.assertEquals(response.jsonPath().getString("linkedin_url"),url );
		Assert.assertEquals(response.jsonPath().getString("education_ug"),ug );
		Assert.assertEquals(response.jsonPath().getString("education_pg"),pg );
		Assert.assertEquals(response.jsonPath().getString("visa_status"),visaStatus );
		Assert.assertEquals(response.jsonPath().getString("comments"),comments );
		//Assert.assertTrue((response.jsonPath().getString("message_response")).contains("Successfully Updated"));

		//updating user id in put/post/get/delete methods in excel
		excel.setCellData(1, 0, user_id,"Post");
		excel.setCellData(1, 0, user_id,"Get");
		excel.setCellData(1, 0, user_id,"Put");
		excel.setCellData(1, 0, user_id,"Delete");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e.getMessage();
		}
		excel.close();
		
		//DB validation
		
		String userId = response.then().extract().path("user_id");
		System.out.println("Auto generated skill_id: " + userId );
		Response getReponseAfterPost = request.given().auth().basic(username, password)
				.get(baseURI + endPoint + "/" +user_id); 
		
       //Asserting newly created user_skill_id is present in DB by making GET call for that id
		getReponseAfterPost.then().assertThat().statusCode(200).and().body("user_id", equalTo(userId));
		
		//Another way of DB validation
		/*request.get(RestAssured.baseURI+RestAssured.basePath).then()
		.body("user_id",hasItem(userid),"name",hasItem(uname),"location",hasItem(ulocation),
				"time_zone",hasItem(utimezone),"linkedin_url",hasItem(linkedinUrl));
		System.out.println("DB Validated and has the inserted record");*/
	}


}
