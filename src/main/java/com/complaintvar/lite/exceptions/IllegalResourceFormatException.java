package com.complaintvar.lite.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IllegalResourceFormatException extends RuntimeException {
    public IllegalResourceFormatException() {
        super("Resource Format is Not Correct.");
    }

    public IllegalResourceFormatException(String message) {
        super(message);
    }
}
// exception controller,