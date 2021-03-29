
package com.assgiment.moneytransfer.rest;

import com.assgiment.moneytransfer.dto.CustomerDto;
import com.assgiment.moneytransfer.service.CustomerService;
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
@RequestMapping("/v1/customers")
@Api(tags = { "Customer endpoints" })
@Slf4j
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
 */

public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@PostMapping(path = "/add")
	@ApiOperation(value = "Add a Customer", notes = "Add customer and create an account")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<CustomerDto> addCustomer(@Valid @RequestBody CustomerDto customer) {
		log.debug("Adding customer details {}", customer);
		return new ResponseEntity<>(customerService.addCustomer(customer), HttpStatus.CREATED);
	}

	@GetMapping(path = "/{customerNumber}")
	@ApiOperation(value = "Get customer details", notes = "Get Customer details by customer number.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = CustomerDto.class, responseContainer = "Object"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long customerNumber) {
		log.debug("Retrieving customer for customerNumber {}", customerNumber);
		return new ResponseEntity<>(customerService.findByCustomerNumber(customerNumber), HttpStatus.OK);
	}

	@PutMapping(path = "/{customerNumber}")
	@ApiOperation(value = "Update customer", notes = "Update customer and any other account information associated with him.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Object.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<Object> updateCustomer(@Valid @RequestBody CustomerDto customerDetails,
			@PathVariable Long customerNumber) {
		log.debug("Updating customer for customerNumber {} Customer Details {}", customerNumber, customerDetails);
		return new ResponseEntity<>(customerService.updateCustomer(customerDetails, customerNumber), HttpStatus.OK);
	}

	@GetMapping(path = "/all")
	@ApiOperation(value = "Find all customers", notes = "Gets details of all the customers")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public ResponseEntity<List<CustomerDto>> getAllCustomers() {
		log.debug("Retrieving All Customers");
		return new ResponseEntity<>(customerService.findAll(), HttpStatus.OK);
	}

	@DeleteMapping(path = "/{customerNumber}")
	@ApiOperation(value = "Delete customer and related accounts", notes = "Delete customer and all accounts associated with him.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Object.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public ResponseEntity<?> deleteCustomer(@PathVariable Long customerNumber) {
		log.debug("Deleting customer details");
		customerService.deleteCustomer(customerNumber);
		return new ResponseEntity<>(customerNumber + "Customer deleted successfully", HttpStatus.NO_CONTENT);
	}

	@PatchMapping(path = "/{customerNumber}/link/accounts/{accountNumber}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Object.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	@ApiOperation(value = "Link Account to Customer", notes = "Link Account To Customer")
	public ResponseEntity<?> updateAccount(@PathVariable Long accountNumber, @PathVariable Long customerNumber) {
		log.debug("Linking Account Number {} to Customer Number", accountNumber, customerNumber);
		customerService.linkAccount(accountNumber, customerNumber);
		return new ResponseEntity<>("Account Number " + accountNumber + "Linked to Customer Number" + customerNumber,
				HttpStatus.OK);
	}
}
