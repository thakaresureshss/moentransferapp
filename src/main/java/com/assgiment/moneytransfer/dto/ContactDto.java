package com.assgiment.moneytransfer.dto;

import com.sun.istack.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ContactDto {

	private UUID id;

	@NotNull
	// Email Validation Can be added ignored for now
	private String emailId;

	private String phone;
	
	@NotNull
	private String mobile;

}
