package com.technical.assgiment.moneytransfer;

import com.assgiment.moneytransfer.MoneytransferApplication;
import com.assgiment.moneytransfer.rest.CustomerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = MoneytransferApplication.class)
class MoneytransferApplicationTests {

	@Autowired
	private CustomerController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
