package com.assgiment.moneytransfer.errors;

import java.io.Serializable;
import java.util.Date;

public class ErrorMessage implements Serializable {
  private static final long serialVersionUID = 953972935711585688L;
  private Date timestamp;
  private String message;
  private String description;

  public ErrorMessage(Date timestamp, String message, String description) {
    super();
    this.timestamp = timestamp;
    this.message = message;
    this.description = description;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public String toString() {
    return "Error [message=" + message + ", description=" + description + "]";
  }

}
