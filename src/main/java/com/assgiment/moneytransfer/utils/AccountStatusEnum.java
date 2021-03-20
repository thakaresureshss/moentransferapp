package com.assgiment.moneytransfer.utils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountStatusEnum {

	ACTIVE("ACTIVE"),

	INACTIVE("INACTIVE");

	private String value;

	AccountStatusEnum(String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

}
