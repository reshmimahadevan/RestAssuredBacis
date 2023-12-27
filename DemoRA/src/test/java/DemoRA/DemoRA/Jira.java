package DemoRA.DemoRA;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

public class Jira {

	public static void main(String args[]) {
		
		RestAssured.baseURI = "http://localhost:8080";
		
		//A session filter can be used record the session id returned from the server as well as automatically 
		//apply this session id in subsequent requests

		SessionFilter session = new SessionFilter();
		
		//Cookie Authorization - uses HTTP cookies to authenticate client requests and maintain session information.
		//relaxedHTTPSValidation() - means that you'll trust all hosts regardless if the SSL certificate is invalid. 
		 given()
		  .relaxedHTTPSValidation()
		  .header("Content-Type", "application/json")
		  .body("{ \"username\": \"Reshmi\", \"password\": \"Reshmi@04\" }")
		  .log().all()
		  .filter(session)
		  .when()
		  .post("/rest/auth/1/session")
		  .then().log().all()
		  .extract().response().asString();
		 
		String expectedMessage= "This is my first comment via RestAssured.";
		
       //Add comment
		String addCommentResponse = given().log().all()
				.pathParam("id", "10002").header("Content-Type", "application/json")
				.body("{\r\n" + "    \"body\": \""+expectedMessage+"\",\r\n"
						+ "     \"visibility\": {\r\n" + "     \"type\": \"role\",\r\n"
						+ "        \"value\": \"Administrators\"\r\n" + "    }\r\n" + "}")
				.filter(session)
				.when()
				.post("/rest/api/2/issue/{id}/comment")
				.then().log().all()
				.assertThat().statusCode(201)
				.extract().response().asString();
		
		//JSONPath is a query language for JSON
		JsonPath js = new JsonPath(addCommentResponse);
		String commentId = js.getString("id");
		
		//Add attachment
		given().log().all()
		.header("X-Atlassian-Token","no-check")
		.pathParam("id", "10002")
		.header("Content-Type", "multipart/form-data")
		.multiPart("file",new File("jira.txt"))
		.filter(session)
		.when()
		.post("/rest/api/2/issue/{id}/attachments")
		.then().log().all().assertThat().statusCode(200);
		
		//Get comment and attachment
		String response =given()
				.filter(session)
				.pathParam("key", "10002")
				.queryParam("feilds", "comment")
	          	.when()
	          	.get("/rest/api/2/issue/{key}/comment")
	          	.then().log().all()
	          	.extract().response().asString();
		System.out.println(response);
		
		JsonPath js1 = new JsonPath(response);
		int commentsCount = js1.get("comments.size()");
		for(int i=0;i<commentsCount;i++)
		{
			String commentIDIssue = js1.get("comments["+i+"].id").toString(); 
			if(commentIDIssue.equalsIgnoreCase(commentId))
			{
				String message = js1.get("comments["+i+"].body").toString(); 
				System.out.println(message);
				Assert.assertEquals(message, expectedMessage);
			}
		}
	}
}
