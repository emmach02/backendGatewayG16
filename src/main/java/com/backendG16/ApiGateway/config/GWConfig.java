package com.backendG16.ApiGateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GWConfig {



    @Bean
    public RouteLocator configurarRutas(RouteLocatorBuilder builder,
                                        @Value("${api.uri.alquiler}") String uriAlquiler) {
        return builder.routes()
                .route(p -> p.path("/api/vehiculos/**").uri(uriAlquiler))
                .route(p -> p.path("/api/empleados/**").uri(uriAlquiler))
                .route(p -> p.path("/api/marcas/**").uri(uriAlquiler))
                .route(p -> p.path("/api/modelos/**").uri(uriAlquiler))
                .route(p -> p.path("/api/posiciones/**").uri(uriAlquiler))
                .route(p -> p.path("/api/interesados/**").uri(uriAlquiler))
                .build();
    }

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                        // Solo empleados pueden crear pruebas y mandar notificaciones
                        .pathMatchers("/api/pruebas/crear", "/api/notificaciones/enviar")
                        .hasAnyRole("EMPLEADO", "ADMIN")

                        // Solo usuarios asociados a un vehículo pueden enviar posiciones
                        .pathMatchers("/api/posiciones/enviar")
                        .hasAnyRole("VEHICULO", "ADMIN") // Usa un rol o autoridad específica para usuarios de vehículos

                        // Solo administradores pueden ver los reportes
                        .pathMatchers("/api/reportes/**")
                        .hasRole("ADMIN")

                        // Cualquier otra petición debe estar autenticada
                        .anyExchange()
                        .authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .csrf(csrf -> csrf.disable()); // Desactiva CSRF si no es necesario para tu caso
        return http.build();
    }


    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        var grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        // Se especifica el nombre del claim a analizar
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        // Se agrega este prefijo en la conversión por una convención de Spring
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        // Se asocia el conversor de Authorities al Bean que convierte el token JWT a un objeto Authorization
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                new ReactiveJwtGrantedAuthoritiesConverterAdapter(grantedAuthoritiesConverter));
        // También se puede cambiar el claim que corresponde al nombre que luego se utilizará en el objeto
        // Authorization
        // jwtAuthenticationConverter.setPrincipalClaimName("user_name");

        return jwtAuthenticationConverter;
    }
}
