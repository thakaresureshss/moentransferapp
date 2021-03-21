package com.assgiment.moneytransfer.dto;

import com.assgiment.moneytransfer.utils.CustomerStatusEnum;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CustomerDto {

	private UUID id;

	@NotBlank(message = "First should not empty")
	private String firstName;

	private String lastName;

	@NotBlank(message = "DOB should not empty")
	private Date dob;

	@NotBlank(message = "Customer number should not empty")
	private Long customerNumber;

	@NotBlank(message = "customer status should not empty")
	private CustomerStatusEnum status;

	private List<AddressDto> customerAddressDtos;

	private ContactDto contactDetailDto;

	private List<AccountDto> accountDtos;

}
