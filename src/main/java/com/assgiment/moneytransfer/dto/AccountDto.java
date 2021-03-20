package com.assgiment.moneytransfer.dto;

import com.assgiment.moneytransfer.utils.AccountStatusEnum;
import com.assgiment.moneytransfer.utils.AccountTypeEnum;
import com.sun.istack.NotNull;
import java.util.Date;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class AccountDto {
	private UUID id;

	@NotNull
	private Long accountNumber;

	private BankDetailDto bankDetailDto;

	private AccountStatusEnum accountStatus;

	private AccountTypeEnum accountType;

	@NotNull
	private Double accountBalance;
	private Date accountCreated;
	private CustomerDto customerDto;
}
