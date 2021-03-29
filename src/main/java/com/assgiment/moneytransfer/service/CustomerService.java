package com.assgiment.moneytransfer.service;

import com.assgiment.moneytransfer.dto.CustomerDto;
import java.util.List;

public interface CustomerService {
	public List<CustomerDto> findAll();

	public CustomerDto addCustomer(CustomerDto customerDetails);

	public CustomerDto findByCustomerNumber(Long customerNumber);

	public CustomerDto updateCustomer(CustomerDto customerDetails, Long customerNumber);

	public void deleteCustomer(Long customerNumber);

	public void linkAccount(Long accountNumber, Long customerNumber);
}
