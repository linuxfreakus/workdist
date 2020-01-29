package com.example.workdist.api.errors;

import java.security.InvalidParameterException;

public class WorkItemAlreadyCompletedException extends InvalidParameterException {

    public WorkItemAlreadyCompletedException(final String msg) {
        super(msg);
    }

}
