package com.assgiment.moneytransfer.rest;

import com.assgiment.moneytransfer.dto.AccountDto;
import com.assgiment.moneytransfer.dto.TransactionDetailsDto;
import com.assgiment.moneytransfer.dto.TransferDetailsDto;
import com.assgiment.moneytransfer.service.impl.BankServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/accounts")
@Api(tags = { "Accounts and Transactions" })
@Slf4j
public class AccountController {

	@Autowired
	private BankServiceImpl bankService;

	@GetMapping(path = "/{accountNumber}")
	@ApiOperation(value = "Get account details", notes = "Find account details by account number")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<?> getByAccountNumber(@PathVariable Long accountNumber) {
		log.debug("Retrieve account By {}", accountNumber);
		return new ResponseEntity<>(bankService.findByAccountNumber(accountNumber), HttpStatus.OK);
	}

	@PostMapping(path = "/add")
	@ApiOperation(value = "Add a new account", notes = "Create an new account for existing Account.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<?> addNewAccount(@Valid @RequestBody AccountDto accountInformation) {
		log.debug("Adding New  Account Details  {}", accountInformation);
		return new ResponseEntity<>(bankService.addNewAccount(accountInformation), HttpStatus.CREATED);
	}

	@PutMapping(path = "/{accountNumber}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Object.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@ApiOperation(value = "Update Account", notes = "Update Account")
	public ResponseEntity<?> updateAccount(@Valid @RequestBody AccountDto accountDto,
			@PathVariable Long accountNumber) {
		log.debug("Updating Account Details  {} for accountNumber {}", accountDto, accountNumber);
		return new ResponseEntity<>(bankService.updateAccount(accountDto, accountNumber), HttpStatus.OK);
	}

	@DeleteMapping(path = "/{accountNumber}")
	@ApiOperation(value = "Delete Account and related accounts", notes = "Delete Account and all accounts associated with him.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Object.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public ResponseEntity<?> deleteAccount(@PathVariable Long accountNumber) {
		log.debug("Deleting Account for accountNumber {}", accountNumber);
		bankService.deleteAccount(accountNumber);
		return new ResponseEntity<>(accountNumber + "Account deleted successfully", HttpStatus.NO_CONTENT);
	}

	@PutMapping(path = "/transfer/{accountNumber}")
	@ApiOperation(value = "Transfer funds between accounts", notes = "Transfer funds between accounts.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Object.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<AccountDto> transferDetails(@Valid @RequestBody TransferDetailsDto transferDetails,
			@PathVariable Long accountNumber) {
		log.debug("Transferring Amount {}  from Account {} To Account ", transferDetails.getTransferAmount(),
				accountNumber, transferDetails.getToAccountNumber());
		return new ResponseEntity<>(bankService.transferDetails(transferDetails, accountNumber), HttpStatus.OK);
	}

	@GetMapping(path = "/transactions/{accountNumber}")
	@ApiOperation(value = "Get all transactions", notes = "Get all Transactions by account number")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<List<TransactionDetailsDto>> getTransactionByAccountNumber(@PathVariable Long accountNumber) {
		log.debug("Retrieving Transactions for Account Number {}", accountNumber);
		return new ResponseEntity<>(bankService.findTransactionsByAccountNumber(accountNumber), HttpStatus.OK);
	}

	@PatchMapping(path = "/{accountNumber}/link/customer/{customerNumber}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Object.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@ApiOperation(value = "Link Account to Customer", notes = "Link Account To Customer")
	public ResponseEntity<?> updateAccount(@PathVariable Long accountNumber, @PathVariable Long customerNumber) {
		log.debug("Linking Account Number {} to Customer Number", accountNumber, customerNumber);
		bankService.linkAccount(accountNumber, customerNumber);
		return new ResponseEntity<>("Account Number " + accountNumber + "Linked to Customer Number" + customerNumber,
				HttpStatus.OK);
	}

}
