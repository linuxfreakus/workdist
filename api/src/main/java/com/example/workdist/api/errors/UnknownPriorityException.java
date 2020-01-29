package com.example.workdist.api.errors;

import java.security.InvalidParameterException;

public class UnknownPriorityException extends InvalidParameterException {

    public UnknownPriorityException(final String msg) {
        super(msg);
    }

}
