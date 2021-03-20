package com.assgiment.moneytransfer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TransferDetailsDto {
	private Long toAccountNumber;
	private Double transferAmount;
}
