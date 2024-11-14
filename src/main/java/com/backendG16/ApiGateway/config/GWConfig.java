package com.backendG16.ApiGateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GWConfig {

    @Value("${api.uri.alquiler}")
    private String uriAlquiler;

    @Bean
    public RouteLocator configurarRutas(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/api/vehiculos/**").uri(uriAlquiler))
                .route(p -> p.path("/api/empleados/**").uri(uriAlquiler))
                .route(p -> p.path("/api/marcas/**").uri(uriAlquiler))
                .route(p -> p.path("/api/modelos/**").uri(uriAlquiler))
                .route(p -> p.path("/api/posiciones/**").uri(uriAlquiler))
                .route(p -> p.path("/api/interesados/**").uri(uriAlquiler))
                .build();
    }
}
