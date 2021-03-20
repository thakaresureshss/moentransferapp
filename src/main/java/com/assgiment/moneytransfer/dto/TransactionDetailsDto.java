package com.assgiment.moneytransfer.dto;

import java.util.Date;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TransactionDetailsDto {
	private UUID id;
	private Long accountNumber;

	private Date txDateTime;

	private String txType;

	private Double txAmount;
}
