package DemoRA.DemoRA;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

public class OAuth {

	public static void main(String[] args) {
		
		
		String[] courseTitles = {"Selenium Webdriver Java","Cypress","Protractor"};

		// Google does'nt allow automation so manually hit URL ,login and place URL in
		// the the String variable
		// "code" changes every time so tell developer to increase lifetime/scope of
		// "code" so you no need to change every time"
		String url = "https://rahulshettyacademy.com/getCourse.php?state=verifyfjdss&code=4%2F0AfJohXl0fyXQvivhlm90lDK9yBhIUSuimQwmLIY_2ZFG5Jd20WN4_f7f2hw2CsnGpGAnzw&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
		String partialcode = url.split("code=")[1];
		String code = partialcode.split("&scope")[0];
		System.out.println(code);

		// urlEncodingEnabled(false) - RestAssured doesn't allow special character here
		// in
		// code(4%2F0AfJohXlN5Hvd5xdaqoBR8Pu4LaD8vRqwGguX_ClXPNWTHHDtmJOTDxrPLVnRcfIVA78X7w)
		// we have % so to let know RestAssured that encoding should not be done we use
		// this flag

		String accessTokenResponse = given().log().all().urlEncodingEnabled(false).queryParams("code", code)
				.queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParams("grant_type", "authorization_code").when().log().all()
				.post("https://www.googleapis.com/oauth2/v4/token").asString();

		JsonPath js = ReusableMethods.rawToJson(accessTokenResponse);
		String accessToken = js.getString("access_token");
		
//		String response  = given().queryParam("access_token", accessToken).log().all()
//				.when().get("https://rahulshettyacademy.com/getCourse.php").asString();
//		
//		System.out.println(response);

		GetCourse_Deserialization gc = given().queryParam("access_token", accessToken).expect()
				.defaultParser(Parser.JSON).when().get("https://rahulshettyacademy.com/getCourse.php")
				.as(GetCourse_Deserialization.class);
		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getInstructor());
		System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());

		List<API_Deserialization> apiCourses = gc.getCourses().getApi();
		for (int i = 0; i < apiCourses.size(); i++) {
			if (apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {

				System.out.println(apiCourses.get(i).getPrice());  

			}
			
			ArrayList<String> a = new ArrayList<String>();
			
			List<WebAutomation_Deserialization> webAutomationCourses = gc.getCourses().getWebAutomation();
			for (int j = 0; j < webAutomationCourses.size(); j++) {
					a.add(webAutomationCourses.get(j).getCourseTitle());  

				}
			
			List<String> b = Arrays.asList(courseTitles);
			Assert.assertTrue(a.equals(b));
		}
	}
}
