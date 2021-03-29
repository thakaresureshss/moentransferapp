package com.assgiment.moneytransfer.errors;

// Make this class mutables
public final class Violation {

    private final String field;
    private final String message;

    public Violation(final String field, final String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

}