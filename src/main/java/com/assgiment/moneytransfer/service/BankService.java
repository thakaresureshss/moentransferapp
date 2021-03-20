package com.assgiment.moneytransfer.service;

import com.assgiment.moneytransfer.dto.AccountDto;
import com.assgiment.moneytransfer.dto.CustomerDto;
import com.assgiment.moneytransfer.dto.TransactionDetailsDto;
import com.assgiment.moneytransfer.dto.TransferDetailsDto;
import java.util.List;

public interface BankService {

	public List<CustomerDto> findAll();

	public CustomerDto addCustomer(CustomerDto customerDetails);

	public CustomerDto findByCustomerNumber(Long customerNumber);

	public CustomerDto updateCustomer(CustomerDto customerDetails, Long customerNumber);

	public void deleteCustomer(Long customerNumber);

	public AccountDto findByAccountNumber(Long accountNumber);

	public AccountDto addNewAccount(AccountDto accountInformation);

	public AccountDto transferDetails(TransferDetailsDto transferDetails, Long customerNumber);

	public List<TransactionDetailsDto> findTransactionsByAccountNumber(Long accountNumber);

	public void linkAccount(Long accountNumber, Long customerNumber);
}
