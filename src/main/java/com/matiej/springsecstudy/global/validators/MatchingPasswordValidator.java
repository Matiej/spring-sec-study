package com.matiej.springsecstudy.global.validators;

import com.matiej.springsecstudy.user.controller.command.CreateUserMatchPass;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatchingPasswordValidator implements ConstraintValidator<MatchingPassword, CreateUserMatchPass> {

    @Override
    public boolean isValid(CreateUserMatchPass command, ConstraintValidatorContext constraintValidatorContext) {
        return command.getPassword().equals(command.getMatchingPassword());

    }
}
