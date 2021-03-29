package com.assgiment.moneytransfer.rest;

import com.assgiment.moneytransfer.dto.AccountDto;
import com.assgiment.moneytransfer.dto.TransactionDetailsDto;
import com.assgiment.moneytransfer.dto.TransferDetailsDto;
import com.assgiment.moneytransfer.service.impl.AccountServiceImpl;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author suresh.thakare
 * 
 *         Note :
 * 
 *         Controller should return specific object
 * 
 *         Controller should return unified structure return JSON everywhere(Not
 *         String and JSON Combination)
 * 
 *         Don't add and suffix to post mapping for example addition of customer
 *         should be like /v1/customers ( Not like /v1/customers/add)
 * 
 *         Document all the thrown errors for each endpoint like for add ( BAD
 *         Request : 400, InternalServerError :500, OK : 200, NOT FOUND: 404,
 *         Resource Created:201, No Content :204, UNPROCESSABLE ENTITY
 *         :422(Validation Errors), CONFLICT : 409 (Database Unique constraint
 *         violations)
 * 
 *         Should not return full Request and Response object should be
 *         different. In case post it should be id only.
 * 
 *         Account should not exist without customer so it should be added under
 *         customer. Adding account url should be like
 *         v1/customers/{customerId}/accounts
 * 
 */

@RestController
@RequestMapping("/v1/accounts")
@Api(tags = { "Accounts and Transactions" })
@Slf4j
public class AccountController {

	@Autowired
	private AccountServiceImpl bankService;

	@GetMapping(path = "/{accountNumber}")
	@ApiOperation(value = "Get account details", notes = "Find account details by account number")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<AccountDto> getByAccountNumber(@PathVariable Long accountNumber) {
		log.debug("Retrieve account By {}", accountNumber);
		return new ResponseEntity<>(bankService.findByAccountNumber(accountNumber), HttpStatus.OK);
	}

	@PostMapping(path = "/add")
	@ApiOperation(value = "Add a new account", notes = "Create an new account for existing Account.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<AccountDto> addNewAccount(@Valid @RequestBody AccountDto accountInformation) {
		log.debug("Adding New  Account Details  {}", accountInformation);
		return new ResponseEntity<>(bankService.addNewAccount(accountInformation), HttpStatus.CREATED);
	}

	@PutMapping(path = "/{accountNumber}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Object.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@ApiOperation(value = "Update Account", notes = "Update Account")
	public ResponseEntity<AccountDto> updateAccount(@Valid @RequestBody AccountDto accountDto,
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

	

}
