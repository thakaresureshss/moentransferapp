package com.assgiment.moneytransfer.utils;

import com.assgiment.moneytransfer.dto.AccountDto;
import com.assgiment.moneytransfer.dto.AddressDto;
import com.assgiment.moneytransfer.dto.BankDetailDto;
import com.assgiment.moneytransfer.dto.ContactDto;
import com.assgiment.moneytransfer.dto.CustomerDto;
import com.assgiment.moneytransfer.dto.TransactionDetailsDto;
import com.assgiment.moneytransfer.dto.TransferDetailsDto;
import com.assgiment.moneytransfer.model.Account;
import com.assgiment.moneytransfer.model.Address;
import com.assgiment.moneytransfer.model.BankDetail;
import com.assgiment.moneytransfer.model.Contact;
import com.assgiment.moneytransfer.model.Customer;
import com.assgiment.moneytransfer.model.Transaction;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MoneyTransferMapper {

	public CustomerDto mapToCustomerDto(Customer customer) {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setId(customer.getId());
		customerDto.setFirstName(customer.getFirstName());
		customerDto.setLastName(customer.getLastName());
		customerDto.setCustomerNumber(customer.getCustomerNumber());
		customerDto.setStatus(CustomerStatusEnum.valueOf(customer.getStatus()));
		customerDto.setDob(customer.getDob());
		customerDto.setContactDetailDto(mapToContactDto(customer.getContactDetails()));
		customerDto.setCustomerAddressDtos(mapToAddressDtos(customer.getCustomerAddresses()));
		return customerDto;
	}

	public Customer mapToCustomerEntity(CustomerDto customerDto, Customer customer) {
		customer.setId(customerDto.getId());
		customer.setFirstName(customerDto.getFirstName());
		customer.setLastName(customerDto.getLastName());
		customer.setCustomerNumber(customerDto.getCustomerNumber());
		customer.setStatus(customerDto.getStatus().getValue());
		customer.setDob(customerDto.getDob());
		Contact contact = mapToContactEntity(customerDto.getContactDetailDto());
		customer.setContactDetails(contact);
		List<Address> address = mapToAddressess(customerDto.getCustomerAddressDtos(), customer);
		customer.setCustomerAddresses(address);
		return customer;
	}

	public AccountDto mapToAccountDto(Account account) {
		AccountDto accountDto = new AccountDto();
		accountDto.setId(account.getId());
		accountDto.setAccountType(AccountTypeEnum.valueOf(account.getAccountType()));
		accountDto.setAccountBalance(account.getAccountBalance());
		accountDto.setAccountNumber(account.getAccountNumber());
		accountDto.setAccountStatus(AccountStatusEnum.valueOf(account.getAccountStatus()));
		accountDto.setBankDetailDto(mapToBankDetailDto(account.getBankDetail()));
		return accountDto;
	}

	public Account mapToAccountEntity(AccountDto accountDto, Account account) {
		account.setId(accountDto.getId());
		account.setAccountType(accountDto.getAccountType().getValue());
		account.setAccountBalance(accountDto.getAccountBalance());
		account.setAccountNumber(accountDto.getAccountNumber());
		account.setAccountStatus(accountDto.getAccountStatus().getValue());
		account.setBankDetail(mapToBankDetailEntity(accountDto.getBankDetailDto()));
		return account;
	}

	public List<AddressDto> mapToAddressDtos(List<Address> addresses) {
		return addresses.stream().map(address -> {
			return mapToAddressDto(address);
		}).collect(Collectors.toList());
	}

	public AddressDto mapToAddressDto(Address address) {
		AddressDto addressDto = new AddressDto();
		addressDto.setId(address.getId());
		addressDto.setAddress1(address.getAddress1());
		addressDto.setAddress2(address.getAddress2());
		addressDto.setCity(address.getCity());
		addressDto.setState(address.getState());
		addressDto.setZip(address.getZip());
		addressDto.setCountry(address.getCountry());
		return addressDto;
	}

	public List<Address> mapToAddressess(List<AddressDto> addresses, Customer customer) {
		return addresses.stream().map(address -> {
			Address addrs = mapToAddressEntity(address);
			addrs.setCustomer(customer);
			return addrs;
		}).collect(Collectors.toList());
	}

	public Address mapToAddressEntity(AddressDto addressDto) {
		Address address = new Address();
		address.setId(addressDto.getId());
		address.setAddress1(addressDto.getAddress1());
		address.setAddress2(addressDto.getAddress2());
		address.setCity(addressDto.getCity());
		address.setState(addressDto.getState());
		address.setZip(addressDto.getZip());
		address.setCountry(addressDto.getCountry());
		return address;
	}

	public ContactDto mapToContactDto(Contact contact) {
		ContactDto contactDto = new ContactDto();
		contactDto.setId(contact.getId());
		contactDto.setMobile(contact.getMobile());
		contactDto.setEmailId(contact.getEmailId());
		contactDto.setPhone(contact.getPhone());
		return contactDto;
	}

	public Contact mapToContactEntity(ContactDto contactDto) {
		Contact cantact = new Contact();
		cantact.setId(contactDto.getId());
		cantact.setMobile(contactDto.getMobile());
		cantact.setEmailId(contactDto.getEmailId());
		cantact.setPhone(contactDto.getPhone());
		return cantact;
	}

	public BankDetailDto mapToBankDetailDto(BankDetail bankDetail) {
		BankDetailDto bankDto = new BankDetailDto();
		bankDto.setId(bankDetail.getId());
		bankDto.setBranchCode(bankDetail.getBranchCode());
		bankDto.setBranchName(bankDetail.getBranchName());
		bankDto.setBranchAddress(mapToAddressDto(bankDetail.getBranchAddress()));
		return bankDto;
	}

	public BankDetail mapToBankDetailEntity(BankDetailDto bankDetailDto) {
		BankDetail bankDetail = new BankDetail();
		bankDetail.setId(bankDetailDto.getId());
		bankDetail.setBranchCode(bankDetailDto.getBranchCode());
		bankDetail.setBranchName(bankDetailDto.getBranchName());
		bankDetail.setBranchAddress(mapToAddressEntity(bankDetailDto.getBranchAddress()));
		return bankDetail;
	}

	public TransactionDetailsDto mapToTransactionDto(Transaction transaction) {
		TransactionDetailsDto transactionDetailsDto = new TransactionDetailsDto();
		transactionDetailsDto.setId(transaction.getId());
		transactionDetailsDto.setTxAmount(transaction.getTxAmount());
		transactionDetailsDto.setTxDateTime(transaction.getTxDateTime());
		transactionDetailsDto.setTxType(transaction.getTxType());
		transactionDetailsDto.setAccountNumber(transaction.getAccountNumber());
		return transactionDetailsDto;
	}

	public Transaction createTransaction(TransferDetailsDto transferDetails, Long accountNumber, String txType) {

		Transaction transaction = new Transaction();
		transaction.setTxAmount(transferDetails.getTransferAmount());
		transaction.setTxDateTime(new Date());
		transaction.setTxType(txType);
		transaction.setAccountNumber(accountNumber);
		return transaction;

	}
}
