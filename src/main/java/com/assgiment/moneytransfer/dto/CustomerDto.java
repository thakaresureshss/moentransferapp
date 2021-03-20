package com.assgiment.moneytransfer.dto;

import com.assgiment.moneytransfer.utils.CustomerStatusEnum;
import com.sun.istack.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CustomerDto {

	private UUID id;

	@NotNull
	private String firstName;

	private String lastName;

	@NotNull
	private Date dob;

	@NotNull
	private Long customerNumber;
	
	@NotNull
	private CustomerStatusEnum status;

	private List<AddressDto> customerAddressDtos;

	private ContactDto contactDetailDto;

	private List<AccountDto> accountDtos;

}
