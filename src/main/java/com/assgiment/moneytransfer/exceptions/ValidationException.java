package com.assgiment.moneytransfer.exceptions;

import com.assgiment.moneytransfer.errors.Violation;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationException extends RuntimeException {
	private final List<Violation> violations;

	public ValidationException(String fieldPath, String fieldMessage) {
		super();
		this.violations = List.of(new Violation(fieldPath, fieldMessage));
	}

}
