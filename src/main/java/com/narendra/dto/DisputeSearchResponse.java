package com.narendra.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisputeSearchResponse {

	private Long idCol;
	private String responseCode;
	private String messageType;
	private Double amount;
	private String issuer;
	private String acquirer;
	private String sequenceNumber;
	private LocalDateTime dateTime;
}
