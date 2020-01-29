package com.example.workdist.api.errors;

import java.security.InvalidParameterException;

public class UnknownSkillNameException extends InvalidParameterException {

    public UnknownSkillNameException(final String msg) {
        super(msg);
    }

}
