package DemoRA.DemoRA;

import io.restassured.path.json.JsonPath;

public class ReusableMethods {

	public static JsonPath rawToJson(String str) {

		JsonPath js = new JsonPath(str);
		return js;
	}

	
}
