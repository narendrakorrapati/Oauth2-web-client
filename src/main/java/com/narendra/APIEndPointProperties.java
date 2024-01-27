package com.narendra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class APIEndPointProperties {

	@Value("${api.gateway.url}")
	private String apiGatewayUrl;
	@Value("${dispute.service.uri}")
	private String disputeServiceUri;
	@Value("${dispute.search.uri}")
	private String disputeSearchUri;
}
