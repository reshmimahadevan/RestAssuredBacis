package DemoRA.DemoRA;

import org.testng.Assert;

import io.restassured.path.json.JsonPath;

public class ComplexJson {

	public static void main(String[] args) {

		JsonPath js = new JsonPath(PayLoad.courseDetails());
		// Print No of courses returned by API
		int count = js.getInt("courses.size()");
		System.out.println(count);
		// Print Purchase Amount
		int price = js.getInt("dashboard.purchaseAmount");
		System.out.println(price);
		// Print Title of the first course
		String title = js.get("courses[0].title");
		System.out.println(title);
		// Print All course titles and their respective Prices and
		// Print no of copies sold by RPA Course
		for (int i = 0; i < count; i++) {
			String courseTitle = js.get("courses[" + i + "].title");
			System.out.println(courseTitle + " : " + js.getInt("courses[" + i + "].price"));
			if (courseTitle.equalsIgnoreCase("RPA")) {
				System.out.println(js.get("courses[" + i + "].copies"));
				break;
			}
		}

		int total =0;
		
		//Verify if Sum of all Course prices matches with Purchase Amount
		for(int j=0;j<count;j++)
		{
			int prices = js.getInt("courses["+j+"].price");
		    int copies = js.getInt("courses["+j+"].copies");
			total = total+(prices*copies);
		}
		
		System.out.println(total);
		Assert.assertEquals(total, price);
		
		
		
        
	}

}
