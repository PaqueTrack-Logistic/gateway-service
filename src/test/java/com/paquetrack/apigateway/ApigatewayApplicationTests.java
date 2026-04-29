package com.paquetrack.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApigatewayApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		assertNotNull(applicationContext, "Application context should load successfully");
	}

	@Test
	void applicationStartsWithoutErrors() {
		assertNotNull(applicationContext.getBean(ApigatewayApplication.class), 
				"ApigatewayApplication bean should be available");
	}

	@Test
	void requiredBeansArePresent() {
		assertTrue(applicationContext.containsBean("corsConfigurationSource"), 
				"CorsConfigurationSource bean should be present");
		assertTrue(applicationContext.containsBean("reactiveJwtDecoder"), 
				"ReactiveJwtDecoder bean should be present");
	}

}

