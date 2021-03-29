package com.assgiment.moneytransfer.service.impl;

import com.assgiment.moneytransfer.dto.AccountDto;
import com.assgiment.moneytransfer.dto.TransactionDetailsDto;
import com.assgiment.moneytransfer.dto.TransferDetailsDto;
import com.assgiment.moneytransfer.errors.Violation;
import com.assgiment.moneytransfer.exceptions.BadRequestException;
import com.assgiment.moneytransfer.exceptions.ResourceNotFoundException;
import com.assgiment.moneytransfer.exceptions.ValidationException;
import com.assgiment.moneytransfer.model.Account;
import com.assgiment.moneytransfer.model.Transaction;
import com.assgiment.moneytransfer.repository.AccountRepository;
import com.assgiment.moneytransfer.repository.CustomerRepository;
import com.assgiment.moneytransfer.repository.TransactionRepository;
import com.assgiment.moneytransfer.service.AccountService;
import com.assgiment.moneytransfer.utils.CustomerStatusEnum;
import com.assgiment.moneytransfer.utils.MoneyTransferMapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 
 * @author suresh.thakare
 * 
 *         Note
 * 
 *         Synchronized should be used if scalable application expected
 * 
 *         Use database locking optimistic locking
 *
 */
@Service
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService {

	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private MoneyTransferMapper bankServiceMapper;

	public AccountServiceImpl(CustomerRepository repository) {
		this.customerRepo = repository;
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

			toAccount = validateAccounts(transferDetails, toAccount, fromAccount, toAccountOpt, violations);

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
		List<Violation> violations = new ArrayList<Violation>();
		if (toAccountOpt.isPresent()) {
			toAccount = toAccountOpt.get();
		} else {
			violations.add(new Violation("toAccountNumber",
					"Payee Account Number " + transferDetails.getToAccountNumber() + " not found."));
			log.error("Payee Account Number {} not found.", transferDetails.getToAccountNumber());
		}

		// Validate From Account Balance
		if (fromAccount.getAccountBalance() < transferDetails.getTransferAmount()) {
			violations
					.add(new Violation("transferAmount", transferDetails.getTransferAmount() + " Insufficient balance ("
							+ transferDetails.getTransferAmount() + ") in payers account(" + fromAccount + ")."));
			log.error("Requested {} balance Not available in {} .", fromAccount.getAccountBalance(),
					fromAccount.getAccountNumber());
		}

		// Validate From Account Balance
		if (toAccount.getAccountNumber() == fromAccount.getAccountNumber()) {
			log.error("Same account payment transfer not allowed.");
			violations.add(new Violation("toAccountNumber",
					"Payer and payee account numbers are same, It should be different "));
		}

		if (toAccount.getCustomer() != null
				&& !CustomerStatusEnum.ACTIVE.getValue().equals(toAccount.getCustomer().getStatus())) {
			log.error("Payee Account is not linked to any customer or linked customer is not active");
			violations.add(new Violation("toAccountNumber",
					"Payee Account is not linked to any customer or linked customer is not active"));
		}
		if (fromAccount.getCustomer() != null
				&& !CustomerStatusEnum.ACTIVE.getValue().equals(fromAccount.getCustomer().getStatus())) {
			log.error(" From account is not linked to any customer or linked customer is not active");
			violations.add(new Violation("fromAccountNumber",
					"From Account is not linked to any customer or linked customer is not active"));
		}
		if (!CollectionUtils.isEmpty(violations)) {
			throw new ValidationException(violations);
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

}
