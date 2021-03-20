package com.assgiment.moneytransfer.utils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountTypeEnum {

	SAVING("SAVING"),

	CURRENT("CURRENT");

	private String value;

	AccountTypeEnum(String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

}
