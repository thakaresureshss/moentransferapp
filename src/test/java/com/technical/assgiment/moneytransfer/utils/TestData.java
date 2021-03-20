package com.technical.assgiment.moneytransfer.utils;

import com.assgiment.moneytransfer.dto.AccountDto;
import com.assgiment.moneytransfer.dto.AddressDto;
import com.assgiment.moneytransfer.dto.BankDetailDto;
import com.assgiment.moneytransfer.dto.ContactDto;
import com.assgiment.moneytransfer.dto.CustomerDto;
import com.assgiment.moneytransfer.utils.AccountStatusEnum;
import com.assgiment.moneytransfer.utils.AccountTypeEnum;
import com.assgiment.moneytransfer.utils.CustomerStatusEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestData {
	public static ContactDto getContactDto() {
		ContactDto contactDto = new ContactDto();
		contactDto.setEmailId("test1@gmail.com");
		contactDto.setMobile("971589002023");
		contactDto.setPhone("044043400"); // optional
		return contactDto;
	}

	public static ContactDto getSecondContactDto() {
		ContactDto contactDto = new ContactDto();
		contactDto.setEmailId("test2@gmail.com");
		contactDto.setMobile("971589002000");
		contactDto.setPhone("044043400"); // optional
		return contactDto;
	}

	public static AddressDto getAddressDto() {
		AddressDto addressDto = new AddressDto();
		addressDto.setAddress1("Flat No :622");
		addressDto.setAddress2("Arabian Gate, DSO");
		addressDto.setCity("Dubai");
		addressDto.setState("Dubai");
		addressDto.setCountry("UAE");
		addressDto.setZip("341041");
		return addressDto;
	}

	public static AddressDto getSecondAddressDto() {
		AddressDto addressDto = new AddressDto();
		addressDto.setAddress1("Flat No :421");
		addressDto.setAddress2("White Palace, DSO");
		addressDto.setCity("Dubai");
		addressDto.setState("Dubai");
		addressDto.setCountry("UAE");
		addressDto.setZip("341041");
		return addressDto;
	}

	public static CustomerDto getFirstCustomer() {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setFirstName("Suresh");
		customerDto.setLastName("Thakare");
		customerDto.setStatus(CustomerStatusEnum.ACTIVE);
		customerDto.setCustomerNumber(1001l);
		customerDto.setContactDetailDto(getContactDto());
		customerDto.setCustomerAddressDtos(Collections.singletonList(getAddressDto()));
		return customerDto;
	}

	public static CustomerDto getSecondCustomer() {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setFirstName("Prajakta");
		customerDto.setLastName("Thakare");
		customerDto.setStatus(CustomerStatusEnum.ACTIVE);
		customerDto.setCustomerNumber(1002l); // Different Customer Number
		customerDto.setContactDetailDto(getSecondContactDto());
		List<AddressDto> addressess = new ArrayList<AddressDto>();
		addressess.add(getAddressDto());
		addressess.add(getSecondAddressDto());
		customerDto.setCustomerAddressDtos(addressess);
		return customerDto;
	}

	public static CustomerDto getThirdCustomer() {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setFirstName("Sahebrao");
		customerDto.setLastName("Thakare");
		customerDto.setStatus(CustomerStatusEnum.INACTIVE);
		customerDto.setCustomerNumber(1003l);
		customerDto.setContactDetailDto(getSecondContactDto());
		List<AddressDto> addressess = new ArrayList<AddressDto>();
		addressess.add(getSecondAddressDto());
		customerDto.setCustomerAddressDtos(addressess);
		return customerDto;
	}

// Account DATA	
	public static AccountDto getFirstAccount() {
		AccountDto acc1 = new AccountDto();
		acc1.setAccountBalance(1000.00);
		acc1.setAccountNumber(1111l);
		acc1.setAccountStatus(AccountStatusEnum.ACTIVE);
		acc1.setAccountType(AccountTypeEnum.CURRENT);
		acc1.setBankDetailDto(getBankDetailsDto());
		return acc1;
	}

	public static AccountDto getSecondAccount() {
		AccountDto acc1 = new AccountDto();
		acc1.setAccountBalance(800.00);
		acc1.setAccountNumber(1112l);
		acc1.setAccountStatus(AccountStatusEnum.ACTIVE);
		acc1.setAccountType(AccountTypeEnum.SAVING);
		acc1.setBankDetailDto(getBankDetailsDto());
		return acc1;
	}

	public static BankDetailDto getBankDetailsDto() {
		BankDetailDto bankDetailDto = new BankDetailDto();
		bankDetailDto.setBranchAddress(getAddressDto());
		bankDetailDto.setBranchCode("ADCB-2080");
		bankDetailDto.setBranchName("ADCB Bank BUR DUBAI");
		return bankDetailDto;
	}
}
