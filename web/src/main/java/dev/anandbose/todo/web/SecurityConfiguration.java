package dev.anandbose.todo.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain defauSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(http -> http
                .requestMatchers(HttpMethod.GET, "/todo/create").hasAuthority("SCOPE_todo.write")
                .requestMatchers(HttpMethod.POST, "/todo/create").hasAuthority("SCOPE_todo.write")
                .requestMatchers(HttpMethod.GET, "/todo/update/*").hasAuthority("SCOPE_todo.write")
                .requestMatchers(HttpMethod.POST, "/todo/update/*").hasAuthority("SCOPE_todo.write")
                .requestMatchers(HttpMethod.GET, "/todo/delete/*").hasAuthority("SCOPE_todo.write")
                .requestMatchers(HttpMethod.POST, "/todo/delete/*").hasAuthority("SCOPE_todo.write")
                .requestMatchers(HttpMethod.GET, "/todo", "/todo/*").hasAuthority("SCOPE_todo.read")
                .requestMatchers(HttpMethod.GET, "/category/create").hasAuthority("SCOPE_todo.write")
                .requestMatchers(HttpMethod.POST, "/category/create").hasAuthority("SCOPE_todo.write")
                .requestMatchers(HttpMethod.GET, "/category/update/*").hasAuthority("SCOPE_todo.write")
                .requestMatchers(HttpMethod.POST, "/category/update/*").hasAuthority("SCOPE_todo.write")
                .requestMatchers(HttpMethod.GET, "/category/delete/*").hasAuthority("SCOPE_todo.write")
                .requestMatchers(HttpMethod.POST, "/category/delete/*").hasAuthority("SCOPE_todo.write")
                .requestMatchers(HttpMethod.GET, "/category", "/category/*").hasAuthority("SCOPE_todo.read")
                .requestMatchers("/", "/error", "/webjars/**").permitAll()
                .anyRequest().authenticated())
                //.exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2Login(Customizer.withDefaults())
                .build();
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService(JdbcOperations jdbcOperations, ClientRegistrationRepository clientRegistrationRepository) {
        return new JdbcOAuth2AuthorizedClientService(jdbcOperations, clientRegistrationRepository);
    }
}
