package com.web_chat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class enableCors {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // For development - allow all origins
        // In production, specify your frontend domain
        config.addAllowedOriginPattern("*");

        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        // WebSocket specific headers
        config.addExposedHeader("Upgrade");
        config.addExposedHeader("Connection");
        config.addExposedHeader("Sec-WebSocket-Accept");
        config.addExposedHeader("Sec-WebSocket-Key");
        config.addExposedHeader("Sec-WebSocket-Version");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}