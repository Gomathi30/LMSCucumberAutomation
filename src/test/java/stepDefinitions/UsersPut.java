package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import org.json.simple.JSONObject;
import org.testng.Assert;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ExcelHandler;
import utils.PropertiesFileReader;

public class UsersPut {
	RequestSpecification request ;
	Response response;

	//loading from properties file
	PropertiesFileReader prop= new PropertiesFileReader();
	String username=prop.getUsername();
	String password=prop.getPassword();
	String baseURI=prop.getBaseURI();
	String endPoint=prop.getUsersEndPoint();

	String projectPath=System.getProperty("user.dir");
	String filepath = "/src/test/resources/data/UsersTestData.xlsx";
	//instance for excel reader file
	ExcelHandler excel= new ExcelHandler(projectPath+filepath, "Put");

	Object userid,name,location,timeZone,url,pg, ug,visaStatus,comments;
	Object phone; 
	static int row =1;

	public  void readPutData(int row) {
		userid=  excel.getCellData(row, 0);
		name= excel.getCellData(row,1);
		phone= excel.getCellData(row,2);
		location = excel.getCellData(row,3);
		timeZone= excel.getCellData(row,4);
		url = excel.getCellData(row,5);
		pg= excel.getCellData(row,6);
		ug= excel.getCellData(row,7);
		visaStatus= excel.getCellData(row,8);
		comments= excel.getCellData(row, 9);
	}
	@Given("User is in the endpoint url with valid id")
	public void user_is_in_the_endpoint_url_with_valid_id() {
		request= given();
	}

	@When("User sends PUT request with endpoint url\\/users\\/id with updated fields and values")
	public void user_sends_put_request_with_endpoint_url_users_id_with_updated_fields_and_values() {
		JSONObject json=new JSONObject();
		/*UsersPut up= new UsersPut();
		up.readPutData(1);*/

		//reading data from excel for put
		userid=  excel.getCellData(row, 0);
		name= excel.getCellData(row,1);
		phone= excel.getCellData(row,2);
		location = excel.getCellData(row,3);
		timeZone= excel.getCellData(row,4);
		url = excel.getCellData(row,5);
		pg= excel.getCellData(row,6);
		ug= excel.getCellData(row,7);
		visaStatus= excel.getCellData(row,8);
		comments= excel.getCellData(row, 9);

		System.out.println("Read from excel :"+ userid);
		//request body
		json.put("user_id", userid);
		json.put("name",name );
		json.put("phone_number", phone);
		json.put("location",location);
		json.put("time_zone", timeZone);
		json.put("linkedin_url", url);
		json.put("education_ug", ug);
		json.put("education_pg", pg);
		json.put("visa_status", visaStatus);
		json.put("comments", comments);
		System.out.println();
		response= request.auth().preemptive().basic(username,password).contentType("application/json").
				when().body(json.toJSONString()).put(baseURI+endPoint+"/"+userid);
		System.out.println("Reponse:"+response.asPrettyString());

	}

	@Then("The response should show status code {int} Created")
	public void the_response_should_show_status_code_created(Integer statusCode) {
		//json schema validation
		response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/usersputschema.json")).extract().response();

		//response body validation	
		Assert.assertEquals(statusCode, response.getStatusCode());
		System.out.println("User id after json path"+response.jsonPath().getString("user_id"));
		Assert.assertEquals(response.jsonPath().getString("user_id"),userid );
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

		//DB validation

		String userId = response.then().extract().path("user_id");
		System.out.println("Auto generated skill_id: " + userId );
		Response getReponseAfterPost = request.given().auth().basic(username, password)
				.get(baseURI + endPoint + "/" +userid); 

		//Asserting newly created user_skill_id is present in DB by making GET call for that id
		getReponseAfterPost.then().assertThat().statusCode(200).and().body("user_id", equalTo(userId));

	}

	@Given("User is in the endpoint url with invalid id")
	public void user_is_in_the_endpoint_url_with_invalid_id() {
		request=given();
		++row;
	}

	@Then("The response should show status code {int} Not Found")
	public void the_response_should_show_status_code_not_found(Integer statuscode) {
		Assert.assertEquals(statuscode, response.getStatusCode());
	}

}
