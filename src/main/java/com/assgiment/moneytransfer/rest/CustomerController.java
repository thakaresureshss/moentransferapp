package com.assgiment.moneytransfer.rest;

import com.assgiment.moneytransfer.dto.CustomerDto;
import com.assgiment.moneytransfer.service.BankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
public class CustomerController {

	@Autowired
	private BankService bankService;

	@PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Add a Customer", notes = "Add customer and create an account")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<?> addCustomer(@Valid @RequestBody CustomerDto customer) {
		log.debug("Adding customer details {}", customer);
		return new ResponseEntity<>(bankService.addCustomer(customer), HttpStatus.CREATED);
	}

	@GetMapping(path = "/{customerNumber}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get customer details", notes = "Get Customer details by customer number.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = CustomerDto.class, responseContainer = "Object"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long customerNumber) {
		log.debug("Retrieving customer for customerNumber {}", customerNumber);
		return new ResponseEntity<>(bankService.findByCustomerNumber(customerNumber), HttpStatus.OK);
	}

	@PutMapping(path = "/{customerNumber}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update customer", notes = "Update customer and any other account information associated with him.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Object.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })

	public ResponseEntity<Object> updateCustomer(@Valid @RequestBody CustomerDto customerDetails,
			@PathVariable Long customerNumber) {
		log.debug("Updating customer for customerNumber {} Customer Details {}", customerNumber, customerDetails);
		return new ResponseEntity<>(bankService.updateCustomer(customerDetails, customerNumber), HttpStatus.OK);
	}

	@GetMapping(path = "/all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Find all customers", notes = "Gets details of all the customers")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public ResponseEntity<List<CustomerDto>> getAllCustomers() {
		log.debug("Retrieving All Customers");
		return new ResponseEntity<>(bankService.findAll(), HttpStatus.OK);
	}

	@DeleteMapping(path = "/{customerNumber}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Delete customer and related accounts", notes = "Delete customer and all accounts associated with him.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = Object.class),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public ResponseEntity<?> deleteCustomer(@PathVariable Long customerNumber) {
		log.debug("Deleting customer details");
		bankService.deleteCustomer(customerNumber);
		return new ResponseEntity<>(customerNumber + "Customer deleted successfully", HttpStatus.NO_CONTENT);
	}

}
