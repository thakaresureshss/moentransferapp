package com.assgiment.moneytransfer.errors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessage implements Serializable {
	private static final long serialVersionUID = 953972935711585688L;
	private Date timestamp;
	private String message;
	private String description;

	private List<Violation> errros;

	public ErrorMessage(Date timestamp, String message, String description) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.description = description;
	}

	@Override
	public String toString() {
		return "Error [message=" + message + ", description=" + description + "]";
	}

}
