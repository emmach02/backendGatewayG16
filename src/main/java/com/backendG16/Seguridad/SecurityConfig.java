package com.backendG16.Seguridad;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
/* public class SecurityConfig  {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Desactiva CSRF, si es necesario
                .authorizeHttpRequests(http ->
                        http.requestMatchers("api/vehiculos/**").hasRole("Admin") // Requiere autenticación para todas las rutas
                )
                .oauth2ResourceServer(oauth -> {
                    oauth.jwt(jwt -> {}); // Configura el manejo de JWT
                })
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Desactiva sesiones
                )
                .build();
    }
} */

public class SecurityConfig {

    @Autowired
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Desactiva CSRF, si es necesario
                .authorizeHttpRequests(http ->
                        http.requestMatchers("/api/vehiculos/*").hasRole("ROLE_VEHICULO") // Asegura que otras rutas también estén protegidas
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth -> {
                    oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter));
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Desactiva sesiones
                )
                .build();
    }
}