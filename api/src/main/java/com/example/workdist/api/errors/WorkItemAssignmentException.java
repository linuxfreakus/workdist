package com.example.workdist.api.errors;

import javassist.NotFoundException;

public class WorkItemAssignmentException extends NotFoundException {

    public WorkItemAssignmentException(final String msg) {
        super(msg);
    }

}
