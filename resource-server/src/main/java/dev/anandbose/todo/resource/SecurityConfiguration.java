package dev.anandbose.todo.resource;

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
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.oauth2ResourceServer(config -> config.jwt(Customizer.withDefaults()))
                .authorizeHttpRequests(config -> config
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
        return JwtDecoders.fromIssuerLocation("http://localhost:9000");
    }
}
