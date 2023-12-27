package endtoendEcom;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;


public class EcomAPITest {

	public static void main(String args[]) {
		
		
        //Login 
		RequestSpecification req = new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).build();

		LoginRequest lr1 = new LoginRequest();
		lr1.setUserEmail("p1@gmail.com");
		lr1.setUserPassword("Xyz@4567");

		RequestSpecification reqLoginIn = given().log().all().spec(req).body(lr1);

		LoginResponse lr2 = reqLoginIn.when().post("/api/ecom/auth/login").then().log().all().extract().response()
				.as(LoginResponse.class);
		String token= lr2.getToken();
		String userId = lr2.getUserId();
		
		
		//Add Product
		RequestSpecification addProductReq = new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", token)
				.build();
		
		RequestSpecification reqAddProd = given().log().all().spec(addProductReq)
		.param("productName", "Laptop")
		.param("productAddedBy", userId)
		.param("productCategory", "electronics")
		.param("productSubCategory", "laptops")
		.param("productPrice", "11500")
		.param("productDescription", "Hp laptops")
		.param("productFor", "Engineers")
		.multiPart("productImage",new File("C://Users//reshm//Desktop//image.jpg"));
		
		
		String addProdResponse = reqAddProd.when().post("/api/ecom/product/add-product")
		.then().log().all().extract().response().asString();
		JsonPath js = new JsonPath(addProdResponse);
		String productId= js.get("productId");
		
	
		//Create Order
		RequestSpecification createOrderBaseReq = new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON)
				.addHeader("authorization", token)
				.build();
		
		OrderDetails od = new OrderDetails();
		od.setCountry("India");
		od.setProductOrderedId(productId);
		
		List<OrderDetails> orderDetailList = new ArrayList<OrderDetails>();
		orderDetailList.add(od);
		
		Order o = new Order();
		o.setOrders(orderDetailList);
		
		RequestSpecification createOrderReq = given().log().all().spec(createOrderBaseReq).body(o);
				
		
		String  responseAddOrder = createOrderReq.when().post("/api/ecom/order/create-order")
		.then().log().all().extract().asString();
		
		System.out.println(responseAddOrder);
		
		
		
		//Get Order
		JsonPath createOrdjs = new JsonPath(responseAddOrder);
		List<Object> orderId = createOrdjs.getList("orders"); 
		List<Object> productOrderedId = createOrdjs.getList("productOrderId");
		
		//Converting [orderId] to "orderId" as while deleting order its expecting only string 
		String orderedId =orderId.toString();
		String finalOrderedId = orderedId.replaceAll("[\\[\\]]", "");
		
		System.out.println(orderId);
		System.out.println(finalOrderedId);
		System.out.println(productOrderedId);
		
	
		RequestSpecification viewOrderBaseReq = new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com")
        		.addHeader("authorization", token)
        		.build();

		RequestSpecification viewOrdReq = given().log().all()
				.spec(viewOrderBaseReq)
				.queryParam("id", orderId);

		String viewOrderResponse = viewOrdReq.when()
				.get("/api/ecom/order/get-orders-details")
				.then().log().all()
        		.extract().response().asString();

		System.out.println(viewOrderResponse);
		
	
		//Delete Product
		RequestSpecification deleteBaseReq = new RequestSpecBuilder()
				.setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", token)
				.build();
		
		RequestSpecification reqDeleteProd = given().log().all()
				.pathParam("productId", productId)
				.spec(deleteBaseReq);
			
				
		String deleteResponse =	reqDeleteProd.when()
				.delete("/api/ecom/product/delete-product/{productId}").then().log().all()
				.extract().response().asString();
		
		JsonPath js1 = new JsonPath(deleteResponse);
		Assert.assertEquals("Product Deleted Successfully", js1.get("message"));
		
		
		//DeleteOrder
	    RequestSpecification deleteBaseReq1 = new RequestSpecBuilder()
	 					.setBaseUri("https://rahulshettyacademy.com")
						.addHeader("authorization", token)
						.build();
				
		RequestSpecification reqDeleteProd1 = given().log().all()
						.pathParam("orderId", finalOrderedId)
						.spec(deleteBaseReq1);
					
						
		String deleteResponse1 =	reqDeleteProd1.when()
						.delete("/api/ecom/order/delete-order/{orderId}").then().log().all()
						.extract().response().asString();
				
		JsonPath js2 = new JsonPath(deleteResponse1);
		System.out.println(js2.get("message"));
		 
						
	}
}
