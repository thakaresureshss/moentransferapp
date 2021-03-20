package com.assgiment.moneytransfer.utils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CustomerStatusEnum {

	ACTIVE("ACTIVE"),

	INACTIVE("INACTIVE");

	private String value;

	CustomerStatusEnum(String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

}
