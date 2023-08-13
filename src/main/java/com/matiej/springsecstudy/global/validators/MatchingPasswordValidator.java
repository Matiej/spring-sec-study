package com.matiej.springsecstudy.global.validators;

import com.matiej.springsecstudy.user.controller.command.RegisterUserCommand;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatchingPasswordValidator implements ConstraintValidator<MatchingPassword, RegisterUserCommand> {


    @Override
    public boolean isValid(RegisterUserCommand registerUserCommand, ConstraintValidatorContext constraintValidatorContext) {
        return registerUserCommand.getPassword().equals(registerUserCommand.getMatchingPassword());

    }
}
