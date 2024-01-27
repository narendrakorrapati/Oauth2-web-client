package com.narendra.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import com.narendra.APIEndPointProperties;
import com.narendra.dto.DisputeSearchResponse;

@Controller
public class DisputeSearchController {
	
	@Autowired
	private WebClient webClient;
	
	@Autowired
	APIEndPointProperties apiEndPointProperties;

	@GetMapping("/txns")
	public String getTransactions(Model model) {

		String url = apiEndPointProperties.getApiGatewayUrl() + apiEndPointProperties.getDisputeServiceUri() + apiEndPointProperties.getDisputeSearchUri();
		
		List<DisputeSearchResponse> transactions = webClient.get().uri(url).retrieve()
				.bodyToFlux(DisputeSearchResponse.class).collect(Collectors.toList()).block();
		
		model.addAttribute("transactions", transactions);

		return "txns";
	}
}
