package com.paquetrack.apigateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.web.cors.reactive.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTests {

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Autowired
    private ReactiveJwtDecoder reactiveJwtDecoder;

    @Test
    void testCorsConfigurationSourceExists() {
        assertNotNull(corsConfigurationSource, "CorsConfigurationSource should be configured");
    }

    @Test
    void testReactiveJwtDecoderExists() {
        assertNotNull(reactiveJwtDecoder, "ReactiveJwtDecoder should be configured");
    }

    @Test
    void testSecurityFilterChainIsConfigured() {
        // Validar que los beans de seguridad están presentes
        assertTrue(corsConfigurationSource != null && reactiveJwtDecoder != null,
                "Security configuration should have CORS and JWT decoder");
    }

}
