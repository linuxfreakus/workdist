package com.example.workdist.api.errors;

import javassist.NotFoundException;

public class WorkItemNotFoundException extends NotFoundException {

    public WorkItemNotFoundException(final String msg) {
        super(msg);
    }

}
