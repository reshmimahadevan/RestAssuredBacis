package DemoRA.DemoRA;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

public class Basics {

	public static void main(String[] args) {

		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String response = given().log().all()
				.queryParam("key", "qaclick123")
				.header("Content-Type", "application/json")
				.body(PayLoad.addPlace())
				.when()
				.post("maps/api/place/add/json")
				.then().assertThat().statusCode(200)
				.body("scope", equalTo("APP")).header("Server", "Apache/2.4.52 (Ubuntu)")
				.extract().response().asString();
		System.out.println(response);

		JsonPath js = ReusableMethods.rawToJson(response);
		String placeId = js.getString("place_id");
		System.out.println(placeId);

		String newAddress = "70 Summer walk, USA";

		given().log().all()
		.queryParam("key", "qaclick123")
		.header("Content-Type", "application/json")
		.body("{\r\n" + "\"place_id\":\"" + placeId + "\",\r\n" + "\"address\":\"" + newAddress + "\",\r\n"
						+ "\"key\":\"qaclick123\"\r\n" + "}")
		.when()
		.put("maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200)
		.body("msg", equalTo("Address successfully updated"));

		String getPlaceResponse = given().log().all()
				.queryParam("key", "qaclick123")
				.queryParam("place_id", placeId)
				.when()
				.get("maps/api/place/get/json")
				.then().log().all().assertThat().statusCode(200)
				.extract()
				.response().asString();
		// .body("address",equalTo("70 Summer walk, USA")) OR ;

		JsonPath js1 = ReusableMethods.rawToJson(getPlaceResponse);
		String actualaddress = js1.getString("address");
		System.out.println(actualaddress);
		Assert.assertEquals(newAddress, actualaddress);

	}

}
