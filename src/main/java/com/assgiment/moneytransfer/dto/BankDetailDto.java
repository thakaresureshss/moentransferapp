package com.assgiment.moneytransfer.dto;

import com.sun.istack.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BankDetailDto {
	private UUID id;
	
	@NotNull
	private String branchName;
	@NotNull
	private String branchCode;
	private AddressDto branchAddress;
}
