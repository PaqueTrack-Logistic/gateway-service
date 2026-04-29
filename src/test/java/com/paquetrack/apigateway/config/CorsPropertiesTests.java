package com.paquetrack.apigateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CorsPropertiesTests {

    @Autowired
    private CorsProperties corsProperties;

    @Test
    void testCorsPropertiesIsLoaded() {
        assertNotNull(corsProperties, "CorsProperties should be loaded");
    }

    @Test
    void testAllowedOriginsIsNotEmpty() {
        assertNotNull(corsProperties.allowedOrigins(), "Allowed origins should not be null");
        assertFalse(corsProperties.allowedOrigins().isEmpty(), "Allowed origins should not be empty");
    }

    @Test
    void testAllowedOriginsContainsValidValues() {
        List<String> origins = corsProperties.allowedOrigins();
        assertTrue(origins.stream().anyMatch(origin -> origin != null && !origin.isEmpty()), 
                   "At least one valid origin should be configured");
    }

}
