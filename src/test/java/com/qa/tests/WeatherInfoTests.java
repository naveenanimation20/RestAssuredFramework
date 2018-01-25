package com.qa.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.util.TestBase;
import com.qa.util.TestUtil;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class WeatherInfoTests extends TestBase{
	
	
	@BeforeMethod
	public void setUp(){
		TestBase.init();
	}
	
	@DataProvider
	public Object[][] getData(){
		Object testData[][] = TestUtil.getDataFromSheet(TestUtil.WeatherSheetName);
		return testData;
	}
	
	
	
	@Test(dataProvider="getData")
	public void getWeatherDetailsWithCorrectCityNameTest(String city,	String HTTPMethod, String humidity, String temperature,	
			String weatherdescription, String windspeed,	String winddirectiondegree){
		
		//1. define the base url
		//http://restapi.demoqa.com/utilities/weather/city
		RestAssured.baseURI = prop.getProperty("serviceurl");
		
		//2. define the http request:
		RequestSpecification httpRequest = RestAssured.given();
		
		//3. make a request/execute the request:
		Response response = httpRequest.request(Method.GET, "/"+city);
		
		//4. get the response body:
		String responseBody = response.getBody().asString();
		System.out.println("Response Body is: "+ responseBody);
		//validate city name or validate the key or value
		Assert.assertEquals(responseBody.contains(city), true);
		
		//5. get the status code and validate it:
		int statusCode = response.getStatusCode();
		System.out.println("the status code is: "+ statusCode);
		
		Assert.assertEquals(statusCode, TestUtil.RESPONSE_CODE_200);
		
		System.out.println("the status line is: "+ response.getStatusLine());
		
		//6. get the headers:
		Headers headers = response.getHeaders();
		System.out.println(headers);
		
		String contentType = response.getHeader("Content-Type");
		System.out.println("the value of content-type header is: "+ contentType);
		
		String contentLength = response.getHeader("Content-Length");
		System.out.println("the value of Content-Length header is: "+ contentLength);

		//get the key value by using JsonPath:
		JsonPath jsonPathValue = response.jsonPath();
		String cityVal = jsonPathValue.get("City");
		System.out.println("the value of city is: "+ cityVal);
		
		Assert.assertEquals(cityVal, city);
		
		String temp = jsonPathValue.get("Temperature");
		System.out.println("the value of Temperature is: "+ temp);
		Assert.assertEquals(temp, temperature);


		String Humidity = jsonPathValue.get("Humidity");
		System.out.println("the value of Humidity is: "+ Humidity);

		String WeatherDescription = jsonPathValue.get("WeatherDescription");
		System.out.println("the value of WeatherDescription is: "+ WeatherDescription);

		String WindSpeed = jsonPathValue.get("WindSpeed");
		System.out.println("the value of WindSpeed is: "+ WindSpeed);

		String WindDirectionDegree = jsonPathValue.get("WindDirectionDegree");
		System.out.println("the value of WindDirectionDegree is: "+ WindDirectionDegree);

		
		
	}
	
	
	
	
	
	
	

}
