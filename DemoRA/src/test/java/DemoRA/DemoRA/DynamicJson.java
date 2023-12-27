package DemoRA.DemoRA;

import static io.restassured.RestAssured.*;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static org.hamcrest.Matchers.*;

public class DynamicJson {

	@Test(dataProvider="booksdata")
	public void addBook(String isbn,String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().log().all()
				.header("Content-Type", "application/json")
				.body(PayLoad.addBooks(isbn,aisle))
				.when()
				.post("/Library/Addbook.php")
				.then().log().all()
				.assertThat().statusCode(200).extract().response().asString();
		JsonPath js = ReusableMethods.rawToJson(response);
		String id = js.get("ID");
		System.out.println(id);
	
	}
		@Test(dataProvider="booksdata")
		public void deleteBook(String isbn,String aisle) {
		given().log().all()
		.header("Content-Type", "application/json")
				.body(PayLoad.deleteBooks(isbn, aisle))
				.when()
				.post("/Library/DeleteBook.php")
				.then().log().all().assertThat().statusCode(200)
				.body("msg", equalTo("book is successfully deleted"));

	}

	@DataProvider(name = "booksdata")
	public Object[][] getData1() {
		return new Object[][] { { "Hi", "1234" }, { "Hello", "56789" }, { "GoodDay", "101112" } };
	}
	
	
}