package com.assgiment.moneytransfer.service;

import com.assgiment.moneytransfer.dto.AccountDto;
import com.assgiment.moneytransfer.dto.TransactionDetailsDto;
import com.assgiment.moneytransfer.dto.TransferDetailsDto;
import java.util.List;

public interface AccountService {

	public AccountDto findByAccountNumber(Long accountNumber);

	public AccountDto addNewAccount(AccountDto accountInformation);

	public AccountDto transferDetails(TransferDetailsDto transferDetails, Long customerNumber);

	public List<TransactionDetailsDto> findTransactionsByAccountNumber(Long accountNumber);

}
