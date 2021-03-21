package com.technical.assgiment.moneytransfer;

import com.assgiment.moneytransfer.MoneytransferApplication;
import com.assgiment.moneytransfer.dto.AccountDto;
import com.assgiment.moneytransfer.dto.CustomerDto;
import com.assgiment.moneytransfer.dto.TransferDetailsDto;
import com.assgiment.moneytransfer.model.Account;
import com.assgiment.moneytransfer.model.Customer;
import com.assgiment.moneytransfer.repository.AccountRepository;
import com.assgiment.moneytransfer.repository.CustomerRepository;
import com.assgiment.moneytransfer.utils.AccountStatusEnum;
import com.assgiment.moneytransfer.utils.AccountTypeEnum;
import com.assgiment.moneytransfer.utils.JsonUtils;
import com.assgiment.moneytransfer.utils.MoneyTransferMapper;
import com.technical.assgiment.moneytransfer.utils.TestData;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@SpringBootTest(classes = MoneytransferApplication.class)
@Slf4j
@TestMethodOrder(OrderAnnotation.class)
public class TestController {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	JsonUtils jsonUtils;

	@Autowired
	private CustomerRepository customerRepo;

	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	MoneyTransferMapper mapper;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	@Order(1)
	public void getWhen_customerNotExisit_thenReturnNotFound() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/customers/" + 1005l)
				.content(jsonUtils.toJsonString(TestData.getFirstCustomer()))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNotFound());
	}

	@Test
	@Order(2)
	public void deleteWhen_customerNotExist_thenBadrequest() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/v1/customers/" + 1005l)
				.content(jsonUtils.toJsonString(TestData.getFirstCustomer()))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest());
	}

	@Order(3)
	@Test
	public void updateWhen_customerNotExist_thenReturn() throws Exception {
		Long customerNumber = TestData.getFirstCustomer().getCustomerNumber();
		log.info("Customer Number {}", customerNumber);
		this.mockMvc.perform(MockMvcRequestBuilders.put("/v1/customers/" + customerNumber)
				.content(jsonUtils.toJsonString(TestData.getSecondCustomer()))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest());
	}

	@Test
	@Order(4)
	public void getAllCustomer_When_EmptyDtabase() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/v1/customers/all").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andExpect(jsonPath("$").isEmpty());
	}

	@Test
	@Order(5)
	public void addWhen_customer_with_SingleAddress_thenReturnCustomerCreated() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/v1/customers/add")
						.content(jsonUtils.toJsonString(TestData.getFirstCustomer()))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isCreated()).andDo(print()).andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.customerNumber").value(1001l));
	}

	@Test
	@Order(6)
	public void addWhen_customer_with_MultiAddress_thenReturnCustomerCreated() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/v1/customers/add")
						.content(jsonUtils.toJsonString(TestData.getSecondCustomer()))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isCreated()).andDo(print()).andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.customerNumber").value(1002l));
	}

	@Order(7)
	@Test
	public void getWhen_customerExisit_thenReturn() throws Exception {
		Long customerNumber = TestData.getFirstCustomer().getCustomerNumber();
		log.info("Customer Number {}", customerNumber);
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/v1/customers/" + customerNumber)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.customerNumber").value(1001l));
	}

	@Order(8)
	@Test
	@Transactional
	public void updateWhen_customerExist_thenReturn() throws Exception {
		Long customerNumber = TestData.getFirstCustomer().getCustomerNumber();
		log.info("Customer Number {}", customerNumber);
		Optional<Customer> findByCustomerNumber = customerRepo.findByCustomerNumber(customerNumber);
		Customer customer = findByCustomerNumber.get();
		CustomerDto customerDto = mapper.mapToCustomerDto(customer);
		customerDto.setFirstName("Update FirstName 1");
		customerDto.setLastName("Update LastName 1");
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/v1/customers/" + customerNumber)
						.content(jsonUtils.toJsonString(customerDto)).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.firstName").value("Update FirstName 1"))
				.andExpect(jsonPath("$.lastName").value("Update LastName 1"));
	}

	@Test
	@Order(9)
	public void addWhen_customer_with_DuplicateCustomerNumber_thenReturnBadRequwest() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/v1/customers/add")
						.content(jsonUtils.toJsonString(TestData.getFirstCustomer()))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest()).andDo(print());
	}
	@Test
	@Order(9)
	public void addWhen_customer_with_DuplicateEmail_thenReturnBadRequwest() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/v1/customers/add")
						.content(jsonUtils.toJsonString(TestData.getFirstCustomer()))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest()).andDo(print());
	}
	
	@Test
	@Order(10)
	public void addWhen_customer_with_EmptyDob_thenReturnBadRequwest() throws Exception {
		CustomerDto firstCustomer = TestData.getFirstCustomer();
		firstCustomer.setDob(null);
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/v1/customers/add")
						.content(jsonUtils.toJsonString(firstCustomer))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest()).andDo(print());
	}


	// Account Test Case

	@Test
	@Order(11)
	public void getWhen_AccountNotExisit_thenReturnNotFound() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/accounts/" + 2222l)
				.content(jsonUtils.toJsonString(TestData.getFirstCustomer()))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	@Order(12)
	public void deleteWhen_AccountNotExist_thenBadrequest() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.delete("/v1/accounts/" + 2222l)
						.content(jsonUtils.toJsonString(TestData.getFirstCustomer()))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Order(3)
	@Test
	public void updateWhen_accountNotExist_thenReturn() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/v1/accounts/" + 2222l)
						.content(jsonUtils.toJsonString(TestData.getSecondCustomer()))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	@Order(14)
	public void addWhen_validAccount_thenReturnAccountCreated() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/v1/accounts/add")
						.content(jsonUtils.toJsonString(TestData.getFirstAccount()))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isCreated()).andDo(print()).andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.accountNumber").value(1111l));
	}

	@Test
	@Order(15)
	public void addWhen_account_thenReturnAccountCreated() throws Exception {

		AccountDto secondAccount = TestData.getSecondAccount();
		secondAccount.getBankDetailDto().setBranchCode("NBD-1120");
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/v1/accounts/add").content(jsonUtils.toJsonString(secondAccount))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isCreated()).andDo(print()).andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.accountNumber").value(1112l));
	}

	@Order(16)
	@Test
	public void getWhen_accountExist_thenReturn() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/v1/accounts/" + 1111l)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.accountNumber").value(1111l));
	}

	@Order(17)
	@Test
	@Transactional
	public void updateWhen_accountExist_thenReturn() throws Exception {
		Optional<Account> accOptional = accountRepo.findByAccountNumber(1111l);
		Account account = accOptional.get();
		AccountDto accountDto = mapper.mapToAccountDto(account);
		accountDto.setAccountStatus(AccountStatusEnum.INACTIVE);
		accountDto.setAccountType(AccountTypeEnum.CURRENT);
		accountDto.setAccountBalance(1000.21);
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/v1/accounts/" + 1111l).content(jsonUtils.toJsonString(accountDto))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.accountStatus").value(AccountStatusEnum.INACTIVE.getValue()))
				.andExpect(jsonPath("$.accountType").value(AccountTypeEnum.CURRENT.getValue()))
				.andExpect(jsonPath("$.accountBalance").value(1000.21));
	}

	@Test
	@Order(18)
	public void addWhen_duplicateAccountNumber_thenBadRequest() throws Exception {
		AccountDto secondAccount = TestData.getSecondAccount();
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/v1/accounts/add").content(jsonUtils.toJsonString(secondAccount))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	@Order(21)
	public void linkCustomerWhen_accountFrom_exist() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.patch("/v1/accounts/" + 1111l + "/link/customer/" + 1001l)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	@Order(22)
	public void linkCustomerWhen_accountFrom_not_exist() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.patch("/v1/accounts/" + 5000l + "/link/customer/" + 1001l)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	@Order(23)
	public void linkCustomerWhen_accountFrom_And_Customer_not_exist() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.patch("/v1/accounts/" + 5000l + "/link/customer/" + 5000l)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	@Order(24)
	public void linkCustomerWhen_Customer_not_exist() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.patch("/v1/accounts/" + 1111l + "/link/customer/" + 5000l)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	@Order(25)
	public void getAllCustomer_When_NonEmptyDtabase() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/v1/customers/all").contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andDo(print());

	}

	// Transfer Amount

	@Test
	@Order(31)
	public void transferAmountWhen_FromAndToAccount_exist() throws Exception {
		TransferDetailsDto dto = new TransferDetailsDto();
		dto.setToAccountNumber(1112l);
		dto.setTransferAmount(100.15);
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/v1/accounts/transfer/" + 1111l)
						.content(jsonUtils.toJsonString(dto)).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.accountNumber").value(1111l))
				.andExpect(jsonPath("$.accountBalance").value(899.85));

	}

	@Test
	@Order(32)
	public void transferAmountWhen_FromAndToAccount_not_exist() throws Exception {
		TransferDetailsDto dto = new TransferDetailsDto();
		dto.setToAccountNumber(5000l);
		dto.setTransferAmount(100.15);
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/v1/accounts/transfer/" + 5000l)
						.content(jsonUtils.toJsonString(dto)).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	@Order(33)
	public void transferAmountWhen_FromAccountExist_andToAccountNotExist() throws Exception {
		TransferDetailsDto dto = new TransferDetailsDto();
		dto.setToAccountNumber(5000l);
		dto.setTransferAmount(100.15);
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/v1/accounts/transfer/" + 1111l)
						.content(jsonUtils.toJsonString(dto)).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	@Order(34)
	public void transferAmountWhen_fromAccountNotExist_and_ToAccountExist() throws Exception {
		TransferDetailsDto dto = new TransferDetailsDto();
		dto.setToAccountNumber(1112l);
		dto.setTransferAmount(100.15);
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/v1/accounts/transfer/" + 5000l)
						.content(jsonUtils.toJsonString(dto)).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	@Order(35)
	public void transferAmountWhen_fromAccount_Insufficient_balance() throws Exception {
		TransferDetailsDto dto = new TransferDetailsDto();
		dto.setToAccountNumber(1112l);
		dto.setTransferAmount(50000.15);
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/v1/accounts/transfer/" + 1111l)
						.content(jsonUtils.toJsonString(dto)).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	@Order(41)
	public void getTransactionsWhen_AccountExist() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/v1/accounts/transactions/" + 1111l)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.[0].accountNumber").value(1111))
				.andExpect(jsonPath("$.[0].txType").value("DEBIT"));
	}

	@Test
	@Order(41)
	public void getTransactionsWhen_toPayeeAccount() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/v1/accounts/transactions/" + 1112l)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.[0].accountNumber").value(1112))
				.andExpect(jsonPath("$.[0].txType").value("CREDIT"));
	}

	@Test
	@Order(42)
	public void getTransactionsWhen_AccountNotExist() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/v1/accounts/transactions/" + 5000l)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$").isEmpty());
	}

	@Test
	@Order(51)
	public void deleteWhen_customerExist_thenDeleted() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/v1/customers/" + 1002l)
				.content(jsonUtils.toJsonString(TestData.getFirstCustomer()))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNoContent());
	}

	@Test
	@Order(52)
	public void deleteWhen_accountExist_thenDeleted() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/v1/accounts/" + 1111l)
				.content(jsonUtils.toJsonString(TestData.getFirstCustomer()))
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isNoContent());
	}
}
