package dev.anandbose.todo.authorization;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@SpringBootApplication
public class AuthorizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RegisteredClientRepository repository, JdbcUserDetailsManager userDetailsService,
			PasswordEncoder passwordEncoder) {
		return _ -> {
			try {
				RegisteredClient client = RegisteredClient.withId("todo-app")
						.clientId("todo-app")
						.clientName("TODO")
						.clientSecret("{noop}secret")
						.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
						.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
						.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
						.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
						.redirectUri("dev.anandbose.todo://oauth2/todo-app")
						.scope(OidcScopes.OPENID)
						.scope(OidcScopes.PROFILE)
						.scope("todo.read")
						.scope("todo.write")
						.clientSettings(
							ClientSettings.builder()
							.requireAuthorizationConsent(true)
							.build()
						)
						.build();
				repository.save(client);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				UserDetails user = User.withUsername("anandbose")
						.password(passwordEncoder.encode("password"))
						.roles("USER")
						.build();
				userDetailsService.createUser(user);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}
}
