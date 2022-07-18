package com.complaintvar.lite.exceptions;

public class DatabaseQueryException extends RuntimeException {
    public DatabaseQueryException() {
        super("Invalid Database Query");
    }
    public DatabaseQueryException(String message) {
        super(message);
    }
}
