package com.narendra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfiguration {

	@Autowired
	public ClientRegistrationRepository clientRegistrationRepository;
	
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	WebClient webClient(ClientRegistrationRepository clientRegistrationRepository,
			OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository) {

		ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
				clientRegistrationRepository, oAuth2AuthorizedClientRepository);
		
		oauth2.setDefaultOAuth2AuthorizedClient(true);

		return WebClient.builder().apply(oauth2.oauth2Configuration()).build();
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity.authorizeHttpRequests(t -> t.requestMatchers("/").permitAll().anyRequest().authenticated())
				.oauth2Login(Customizer.withDefaults()).logout(t -> 
						//t.logoutSuccessUrl("/")
						t.logoutSuccessHandler(oidcLogoutSuccessHandler())
						.invalidateHttpSession(true)
						.clearAuthentication(true).deleteCookies("JSESSIONID"));

		return httpSecurity.build();
	}

	private OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
		OidcClientInitiatedLogoutSuccessHandler successHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
		successHandler.setPostLogoutRedirectUri("http://localhost:8086/");
		return successHandler;
	}
}
