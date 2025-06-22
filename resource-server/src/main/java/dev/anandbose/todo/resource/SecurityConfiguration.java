package dev.anandbose.todo.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Value("${todo.jwt.issuer.uri:http://localhost:9000}")
    private String issuerUri;
    
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.oauth2ResourceServer(config -> config.jwt(Customizer.withDefaults()))
                .authorizeHttpRequests(config -> config
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/todo/**")
                        .hasAnyAuthority("SCOPE_todo.read", "SCOPE_todo.write")
                        .requestMatchers(HttpMethod.GET, "/category/**")
                        .hasAnyAuthority("SCOPE_todo.read", "SCOPE_todo.write")
                        .requestMatchers(HttpMethod.POST, "/todo/**").hasAuthority("SCOPE_todo.write")
                        .requestMatchers(HttpMethod.POST, "/category/**").hasAuthority("SCOPE_todo.write")
                        .requestMatchers(HttpMethod.PATCH, "/todo/**").hasAuthority("SCOPE_todo.write")
                        .requestMatchers(HttpMethod.PATCH, "/category/**").hasAuthority("SCOPE_todo.write")
                        .anyRequest().authenticated())
                .csrf(config -> config.disable())
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }
}
