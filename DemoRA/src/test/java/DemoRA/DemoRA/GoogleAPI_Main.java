package DemoRA.DemoRA;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.List;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GoogleAPI_Main {

	public static void main(String[] args) {

		RestAssured.baseURI="https://rahulshettyacademy.com";	
		
		GoogleAPI_Serialization1 gs1 = new GoogleAPI_Serialization1();
		
		gs1.setAccuracy(50);
		gs1.setAddress("29, side layout, cohen 09");
		gs1.setName("Frontline house");
		gs1.setPhone_number("(+91) 983 893 3937");
		gs1.setWebsite("http://google.com");
		gs1.setLanguage("French-IN");
		List<String> myList = new ArrayList<String>();
		myList.add("shoe park");
		myList.add("shop");
		gs1.setTypes(myList);
		
		GoogleAPI_Serialization3 gs3 = new GoogleAPI_Serialization3();
		
		gs3.setLat(-38.383494);
		gs3.setLng(33.427362);
		gs1.setLocation(gs3);
		
		Response res = given().log().all()
				.queryParam("key", "qaclick123")
				.body(gs1)
				.when()
				.post("maps/api/place/add/json")
				.then()
				.assertThat()
				.statusCode(200).extract().response();
		
		String response = res.asString();
		System.out.println(response);
	}

}
