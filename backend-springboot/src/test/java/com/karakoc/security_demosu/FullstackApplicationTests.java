package com.karakoc.security_demosu;

import com.karakoc.sofra.FullstackApplication;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(classes = FullstackApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class FullstackApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	public void testHelloWorld() {
		RestAssured.baseURI = "http://localhost:8080"; // Uygulamanızın çalıştığı port

		given()
				.when()
				.get("/hello/test-endpoint")
				.then()
				.statusCode(200)
				.body(equalTo("HELLO WORLD!"));
	}
}
