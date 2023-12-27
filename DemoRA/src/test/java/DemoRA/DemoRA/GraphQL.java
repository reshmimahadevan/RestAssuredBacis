package DemoRA.DemoRA;

import static io.restassured.RestAssured.*;

import org.testng.Assert;

import io.restassured.path.json.JsonPath;

public class GraphQL {

	public static void main(String args[]) {

		// Query
		int locationId = 4800;
		String queryResponse = given().log().all().header("Content-Type", "application/json")
				.body("{\"query\":\"query($characterId : Int!)\\n{\\n  \\n  character(characterId :$characterId)\\n  "
						+ "{\\n    " + "\\n    name\\n    status\\n    species\\n    id\\n    "
						+ "\\n  }\\n  \\n  location(locationId :" + locationId + ")\\n  "
						+ "{\\n    type\\n  }\\n  \\n  episode(episodeId :3509)\\n  "
						+ "{\\n    name\\n    air_date\\n  }\\n   \\n}\",\"variables\":{\"characterId\":4297}}")
				.when().post("https://rahulshettyacademy.com/gq/graphql").then().log().all().extract().response()
				.asString();

		JsonPath js = new JsonPath(queryResponse);
		String name = js.get("data.character.name");
		Assert.assertEquals(name, "Raki");

		// Mutation
		String mutationResponse = given().log().all().header("Content-Type", "application/json")
				.body("{\"query\":\"mutation($locationName :String!,$characterName :String!,$episodeName :String!)"
						+ "\\n{\\n  createLocation(location:{name:$locationName,type:\\\"Asia\\\",dimension:\\\"123\\\"})\\n "
						+ " {\\n    id\\n  }"
						+ "\\n   createCharacter(character:{name:$characterName,type:\\\"funny\\\",status:\\\"alive\\\",species:\\\"human\\\",gender:\\\"male\\\",image:\\\"image.png\\\",originId:4792,locationId:4792})"
						+ "\\n  {\\n   id\\n  }\\n  createEpisode(episode :{name:$episodeName,air_date:\\\"01/29/1997\\\",episode:\\\"Episode1\\\"})"
						+ "\\n  {\\n    id\\n  }\\n  \\n  deleteLocations(locationIds:[4797,4798])"
						+ "\\n  {\\n    locationsDeleted\\n  }\\n \\n}\",\"variables\":{\"locationName\":\"NZ\",\"characterName\":\"Robert\",\"episodeName\":\"NMJ\"}}")
				.when().post("https://rahulshettyacademy.com/gq/graphql").then().log().all().extract().response()
				.asString();

		System.out.println(mutationResponse);
	}
}