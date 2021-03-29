package com.assgiment.moneytransfer.service.impl;

import com.assgiment.moneytransfer.dto.CustomerDto;
import com.assgiment.moneytransfer.exceptions.BadRequestException;
import com.assgiment.moneytransfer.exceptions.ResourceNotFoundException;
import com.assgiment.moneytransfer.model.Account;
import com.assgiment.moneytransfer.model.Customer;
import com.assgiment.moneytransfer.repository.AccountRepository;
import com.assgiment.moneytransfer.repository.CustomerRepository;
import com.assgiment.moneytransfer.service.CustomerService;
import com.assgiment.moneytransfer.utils.MoneyTransferMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author suresh.thakare
 *
 *         Note :
 * 
 *         Avoid field Injection User proper
 * 
 *         Use of stream API's wherever required.
 * 
 *         Throw proper error codes (HTTP Status code in case of validation)
 * 
 * 
 */
@Service
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private AccountRepository accountRepo;
	
	@Autowired
	private MoneyTransferMapper bankServiceMapper;

	public CustomerServiceImpl(CustomerRepository repository) {
		this.customerRepo = repository;
	}

	public List<CustomerDto> findAll() {

		List<CustomerDto> allCustomerDetails = new ArrayList<>();

		Iterable<Customer> customerList = customerRepo.findAll();

		customerList.forEach(customer -> {
			allCustomerDetails.add(bankServiceMapper.mapToCustomerDto(customer));
		});

		return allCustomerDetails;
	}

	/**
	 * CREATE Customer
	 * 
	 * @param customerDto
	 * @return
	 */
	public CustomerDto addCustomer(CustomerDto customerDto) {
		validateCustomerData(customerDto);
		Customer customer = bankServiceMapper.mapToCustomerEntity(customerDto, new Customer());
		customer.setCreateDateTime(new Date());
		return bankServiceMapper.mapToCustomerDto(customerRepo.save(customer));
	}

	private void validateCustomerData(CustomerDto customerDto) {
		if (customerDto.getDob() == null) {
			log.error("Validation Error : Date of Birth Should not be blank or empty");
			throw new BadRequestException("Validation Error: Date of Birth Should not be blank or empty");
		}

		if (customerDto.getContactDetailDto() == null || customerDto.getContactDetailDto().getEmailId() == null
				|| " ".equalsIgnoreCase(customerDto.getContactDetailDto().getEmailId())) {
			log.error("Validation Error : Email Should not be blank or empty");
			throw new BadRequestException("Validation Error: Email Should not be blank or empty");
		}
		Iterable<Customer> findAll = customerRepo.findAll();
		Spliterator<Customer> spliterator = findAll.spliterator();
		if (StreamSupport.stream(spliterator, false).anyMatch(customer -> customer.getContactDetails().getEmailId()
				.equals(customerDto.getContactDetailDto().getEmailId()))) {
			log.error("Validation Error : Email is not available with this id {}",
					customerDto.getContactDetailDto().getEmailId());
			throw new BadRequestException("Validation Error: Email is not available with this id"
					+ customerDto.getContactDetailDto().getEmailId());
		}
		if (StreamSupport.stream(spliterator, false)
				.anyMatch(customer -> customer.getCustomerNumber() == customerDto.getCustomerNumber())) {
			log.error("Validation Error : Customer id not available with this id {}", customerDto.getCustomerNumber());
			throw new BadRequestException(
					"Validation Error: Customer id not available with this id" + customerDto.getCustomerNumber());
		}
	}

	/**
	 * READ Customer
	 * 
	 * @param customerNumber
	 * @return
	 */

	public CustomerDto findByCustomerNumber(Long customerNumber) {
		Optional<Customer> customerEntityOpt = customerRepo.findByCustomerNumber(customerNumber);
		if (customerEntityOpt.isPresent()) {
			return bankServiceMapper.mapToCustomerDto(customerEntityOpt.get());
		} else {
			log.error("Customer Not found {}", customerNumber);
			throw new ResourceNotFoundException("Customer", "customerNumber", customerNumber);
		}
	}

	/**
	 * UPDATE Customer
	 * 
	 * @param customerDetails
	 * @param customerNumber
	 * @return
	 */
	@Transactional
	public CustomerDto updateCustomer(CustomerDto customerDetails, Long customerNumber) {
		Optional<Customer> existingDbCustomerOpt = customerRepo.findByCustomerNumber(customerNumber);
		if (existingDbCustomerOpt.isPresent()) {
			Customer dbCustomer = existingDbCustomerOpt.get();
			dbCustomer = bankServiceMapper.mapToCustomerEntity(customerDetails, dbCustomer);
			dbCustomer.setUpdateDateTime(new Date());
			return bankServiceMapper.mapToCustomerDto(customerRepo.save(dbCustomer));
		} else {
			log.error("Customer not available with customer number {}", customerNumber);
			throw new BadRequestException("Customer not available with customerNumber :" + customerNumber);
		}
	}

	/**
	 * DELETE Customer
	 * 
	 * @param customerNumber
	 * @return
	 */

	public void deleteCustomer(Long customerNumber) {
		Optional<Customer> managedCustomerEntityOpt = customerRepo.findByCustomerNumber(customerNumber);
		if (managedCustomerEntityOpt.isPresent()) {
			Customer managedCustomerEntity = managedCustomerEntityOpt.get();
			customerRepo.delete(managedCustomerEntity);
		} else {
			log.error("{} Customer does not exist.", customerNumber);
			throw new BadRequestException(customerNumber + "Customer does not exist.");
		}
	}

	@Override
	public void linkAccount(Long accountNumber, Long customerNumber) {
		Optional<Account> accountOptional = accountRepo.findByAccountNumber(accountNumber);
		if (!accountOptional.isPresent()) {
			log.error("{} Invalid Account number.", accountNumber);
			throw new BadRequestException("Invalid Account number");
		}
		Optional<Customer> customerOptional = customerRepo.findByCustomerNumber(customerNumber);
		if (!customerOptional.isPresent()) {
			log.error("{} Invalid Customer number.", customerNumber);
			throw new BadRequestException("Invalid Customer number");
		}
		Customer customer = customerOptional.get();
		Account acc = accountOptional.get();
		acc.setCustomer(customer);
		List<Account> accounts = customer.getAccounts();
		accounts.add(acc);
		customer.setAccounts(accounts);
		customerRepo.save(customer);
	}

}
