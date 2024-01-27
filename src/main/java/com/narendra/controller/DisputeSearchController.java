package com.narendra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.narendra.APIEndPointProperties;
import com.narendra.dto.DisputeSearchResponse;

@Controller
public class DisputeSearchController {
	
	@Autowired
	OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	APIEndPointProperties apiEndPointProperties;

	@GetMapping("/txns")
	public String getTransactions(Model model, @AuthenticationPrincipal OidcUser principal) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

		OAuth2AuthorizedClient authorizedClient = oAuth2AuthorizedClientService
				.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());

		String jwtAccessToken = authorizedClient.getAccessToken().getTokenValue();
		System.out.println("jwtAccessToken = " + jwtAccessToken);

		System.out.println("Princiapl = " + principal);

		OidcIdToken idToken = principal.getIdToken();

		String idTokenValue = idToken.getTokenValue();

		System.out.println("IdTokenValue = " + idTokenValue);

		String url = apiEndPointProperties.getApiGatewayUrl() + apiEndPointProperties.getDisputeServiceUri() + apiEndPointProperties.getDisputeSearchUri();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + jwtAccessToken);
		
		HttpEntity<List<DisputeSearchResponse>> httpEntity = new HttpEntity<>(headers);
		
		ResponseEntity<List<DisputeSearchResponse>> exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<List<DisputeSearchResponse>>() {});
		
		List<DisputeSearchResponse> transactions = exchange.getBody();
		
		model.addAttribute("transactions", transactions);

		return "txns";
	}
}
