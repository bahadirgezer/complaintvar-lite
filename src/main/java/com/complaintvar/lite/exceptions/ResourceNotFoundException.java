package com.complaintvar.lite.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super("Fetched Resource Does Not Exist.");
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
}