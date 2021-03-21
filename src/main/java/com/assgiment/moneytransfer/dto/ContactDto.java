package com.assgiment.moneytransfer.dto;

import com.sun.istack.NotNull;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ContactDto {

	private UUID id;

	@NotBlank(message = "customer email should not empty")
	private String emailId;

	private String phone;

	@NotNull
	private String mobile;

}
