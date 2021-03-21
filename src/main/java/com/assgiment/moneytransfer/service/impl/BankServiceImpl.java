package com.assgiment.moneytransfer.service.impl;

import com.assgiment.moneytransfer.dto.AccountDto;
import com.assgiment.moneytransfer.dto.CustomerDto;
import com.assgiment.moneytransfer.dto.TransactionDetailsDto;
import com.assgiment.moneytransfer.dto.TransferDetailsDto;
import com.assgiment.moneytransfer.exceptions.BadRequestException;
import com.assgiment.moneytransfer.exceptions.ResourceNotFoundException;
import com.assgiment.moneytransfer.model.Account;
import com.assgiment.moneytransfer.model.Customer;
import com.assgiment.moneytransfer.model.Transaction;
import com.assgiment.moneytransfer.repository.AccountRepository;
import com.assgiment.moneytransfer.repository.CustomerRepository;
import com.assgiment.moneytransfer.repository.TransactionRepository;
import com.assgiment.moneytransfer.service.BankService;
import com.assgiment.moneytransfer.utils.CustomerStatusEnum;
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

@Service
@Transactional
@Slf4j
public class BankServiceImpl implements BankService {
	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private MoneyTransferMapper bankServiceMapper;

	public BankServiceImpl(CustomerRepository repository) {
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

	/**
	 * Find Account
	 * 
	 * @param accountNumber
	 * @return
	 */
	public AccountDto findByAccountNumber(Long accountNumber) {
		Optional<Account> accountEntityOpt = accountRepo.findByAccountNumber(accountNumber);
		if (accountEntityOpt.isPresent()) {
			return bankServiceMapper.mapToAccountDto(accountEntityOpt.get());
		} else {
			log.error("Account Not found with Account Number {} .", accountNumber);
			throw new ResourceNotFoundException("Account", "accountNumber", accountNumber);
		}

	}

	/**
	 * Create new account
	 * 
	 * @param accountDto
	 * @param customerNumber
	 * 
	 * @return
	 */
	public AccountDto addNewAccount(AccountDto accountDto) {
		Account account = bankServiceMapper.mapToAccountEntity(accountDto, new Account());
		account.setCreateDateTime(new Date());
		return bankServiceMapper.mapToAccountDto(accountRepo.save(account));
	}

	/**
	 * Transfer funds from one account to another for a specific customer
	 * 
	 * @param transferDetails
	 * @param customerNumber
	 * @return
	 */
	@Transactional
	public AccountDto transferDetails(TransferDetailsDto transferDetails, Long accountNumber) {
		List<Account> accounts = new ArrayList<>();
		Account toAccount = null;
		Optional<Account> accountOpt = accountRepo.findByAccountNumber(accountNumber);
		if (accountOpt.isPresent()) {

			Account fromAccount = accountOpt.get();
			Optional<Account> toAccountOpt = accountRepo.findByAccountNumber(transferDetails.getToAccountNumber());
			toAccount = validateAccounts(transferDetails, toAccount, fromAccount, toAccountOpt);

			synchronized (this) {
				// update FROM ACCOUNT
				fromAccount.setAccountBalance(fromAccount.getAccountBalance() - transferDetails.getTransferAmount());
				fromAccount.setUpdateDateTime(new Date());
				accounts.add(fromAccount);

				// update TO ACCOUNT
				toAccount.setAccountBalance(toAccount.getAccountBalance() + transferDetails.getTransferAmount());
				toAccount.setUpdateDateTime(new Date());
				accounts.add(toAccount);
				accountRepo.saveAll(accounts);

				// Create transaction for FROM Account
				Transaction fromTransaction = bankServiceMapper.createTransaction(transferDetails,
						fromAccount.getAccountNumber(), "DEBIT");
				transactionRepository.save(fromTransaction);

				// Create transaction for TO Account
				Transaction toTransaction = bankServiceMapper.createTransaction(transferDetails,
						toAccount.getAccountNumber(), "CREDIT");

				transactionRepository.save(toTransaction);
			}
			return bankServiceMapper.mapToAccountDto(accountRepo.findByAccountNumber(accountNumber).get());

		} else {
			log.error("From account number is not valid {} .", accountNumber);
			throw new BadRequestException("From account number is not valid" + accountNumber);
		}
	}

	private Account validateAccounts(TransferDetailsDto transferDetails, Account toAccount, Account fromAccount,
			Optional<Account> toAccountOpt) {
		if (toAccountOpt.isPresent()) {
			toAccount = toAccountOpt.get();
		} else {
			log.error("Payee Account Number {} not found.", transferDetails.getToAccountNumber());
			throw new BadRequestException(
					"Payee Account Number " + transferDetails.getToAccountNumber() + " not found.");
		}

		// Validate From Account Balance
		if (fromAccount.getAccountBalance() < transferDetails.getTransferAmount()) {
			log.error("Requested {} balance Not available in {} .", fromAccount.getAccountBalance(),
					fromAccount.getAccountNumber());
			throw new BadRequestException("Insufficient balance.");
		}

		// Validate From Account Balance
		if (toAccount.getAccountNumber() == fromAccount.getAccountNumber()) {
			log.error("Same account payment transfer not allowed.");
			throw new BadRequestException("Same from and payee account numbers are same");
		}

		if (toAccount.getCustomer() != null
				&& !CustomerStatusEnum.ACTIVE.getValue().equals(toAccount.getCustomer().getStatus())) {
			log.error("Payee Account is not linked to any customer or linked customer is not active");
			throw new BadRequestException("Payee Account is not to any customer or linked customer is not active");
		}
		if (fromAccount.getCustomer() != null
				&& !CustomerStatusEnum.ACTIVE.getValue().equals(fromAccount.getCustomer().getStatus())) {
			log.error(" From account is not linked to any customer or linked customer is not active");
			throw new BadRequestException(
					"From Account is not linked to any customer or linked customer is not active");
		}

		return toAccount;
	}

	/**
	 * Get all transactions for a specific account
	 * 
	 * @param accountNumber
	 * @return
	 */
	public List<TransactionDetailsDto> findTransactionsByAccountNumber(Long accountNumber) {
		List<TransactionDetailsDto> transactionDetails = new ArrayList<>();
		Optional<Account> accountEntityOpt = accountRepo.findByAccountNumber(accountNumber);
		if (accountEntityOpt.isPresent()) {
			Optional<List<Transaction>> transactionEntitiesOpt = transactionRepository
					.findByAccountNumber(accountNumber);
			if (transactionEntitiesOpt.isPresent()) {
				transactionEntitiesOpt.get().forEach(transaction -> {
					transactionDetails.add(bankServiceMapper.mapToTransactionDto(transaction));
				});
			}
		}
		return transactionDetails;
	}

	@Transactional
	public AccountDto updateAccount(AccountDto accountDto, Long accountNumber) {
		Optional<Account> existintAccountOpt = accountRepo.findByAccountNumber(accountNumber);
		if (existintAccountOpt.isPresent()) {
			Account dbAccount = existintAccountOpt.get();
			dbAccount = bankServiceMapper.mapToAccountEntity(accountDto, dbAccount);
			dbAccount.setUpdateDateTime(new Date());
			return bankServiceMapper.mapToAccountDto(accountRepo.save(dbAccount));
		} else {
			log.error("Account not available with customerNumber {} .", accountNumber);
			throw new BadRequestException("Account not available with customerNumber :" + accountNumber);
		}
	}

	/**
	 * DELETE Account
	 * 
	 * @param accountNumber
	 * @return
	 */
	public void deleteAccount(Long accountNumber) {
		Optional<Account> accountOpt = accountRepo.findByAccountNumber(accountNumber);
		if (accountOpt.isPresent()) {
			Account account = accountOpt.get();
			int linkedAccounts = account.getCustomer().getAccounts().size();
			if (linkedAccounts == 1) {
				customerRepo.delete(account.getCustomer());
			}
			accountRepo.delete(account);
		} else {
			log.error("{} Account does not exist.", accountNumber);
			throw new BadRequestException(accountNumber + "Account does not exist.");
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
