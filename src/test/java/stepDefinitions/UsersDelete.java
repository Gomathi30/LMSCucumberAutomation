package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ExcelHandler;
import utils.PropertiesFileReader;

import static io.restassured.RestAssured.*;

import org.testng.Assert;

public class UsersDelete {

	RequestSpecification request;
	Response response;

	String projectPath=System.getProperty("user.dir");
	String filepath = "/src/test/resources/data/UsersTestData.xlsx";

	ExcelHandler excel= new ExcelHandler(projectPath+filepath, "Delete");



	static int row=1;

	PropertiesFileReader prop= new PropertiesFileReader();
	String username=prop.getUsername();
	String password=prop.getPassword();
	String baseURI=prop.getBaseURI();
	String endPoint=prop.getUsersEndPoint();
	Object id;

	@Given("user is on delete method")
	public void user_is_on_delete_method() {
		request= given();
	}

	@When("user sends delete request with valid user id")
	public void user_sends_delete_request_with_valid_user_id() {

		id = excel.getCellData(row, 0);
		row++;
		System.out.println("ID:"+id);
		response= request.auth().basic(username, password).
				when().delete(baseURI+endPoint+"/"+id);

	}

	@Then("the user id is deleted with response code {int}")
	public void the_user_id_is_deleted_with_response_code(Integer statusCode) {
		System.out.println("response is :"+ response.asString());
		Assert.assertEquals(response.getStatusCode(), statusCode);
		Assert.assertTrue(response.asString().contains("The record has been deleted"));
	}

	@When("user sends delete request with non existing user id")
	public void user_sends_delete_request_with_non_existing_user_id() {

		System.out.println("Row value in 2 method: "+ row);
		id = excel.getCellData(2, 0);
		System.out.println("ID:"+id);
		response= request.auth().basic(username, password).delete(baseURI+endPoint+"/"+id);
	}

	@Then("{int} not found response code is displayed")
	public void not_found_response_code_is_displayed(Integer statusCode) {
		Assert.assertEquals(response.getStatusCode(), statusCode);
		Assert.assertTrue(response.asString().contains("Not Found"));
		
		//DB Validation
				String userId = response.then().extract().path("user_id");
				Response getReponseAfterPost = given().auth().basic(username, password)
						.get(baseURI + endPoint + "/" +id); 


				getReponseAfterPost.then().assertThat().statusCode(404);
	}

	@When("user sends delete request without userid")
	public void user_sends_delete_request_without_userid() {

		response= request.auth().basic(username, password).delete(baseURI+endPoint);
	}

	@Then("{int} method not found error is displayed")
	public void method_not_found_error_is_displayed(Integer statusCode) {
		Assert.assertEquals(response.getStatusCode(), statusCode);
		Assert.assertTrue(response.asString().contains("DELETE is not one of the supported Http Methods"));
	}


}
