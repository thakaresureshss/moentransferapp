package com.assgiment.moneytransfer.dto;

import com.sun.istack.NotNull;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AddressDto {
	
	private UUID id;
	private String address1;
	private String address2;
	
	private String city;
	@NotNull
	private String state;
	@NotNull
	private String zip;
	@NotNull
	private String country;

}
