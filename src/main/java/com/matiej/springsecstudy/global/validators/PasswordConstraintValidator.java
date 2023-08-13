package com.matiej.springsecstudy.global.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

import java.util.Arrays;

public class PasswordConstraintValidator implements ConstraintValidator<PasswordsValidator, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        final PasswordValidator validator = new PasswordValidator(Arrays.asList(new LengthRule(8, 30),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new WhitespaceRule()));
        final RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }

        constraintValidatorContext.buildConstraintViolationWithTemplate(
                        String.join(", ", validator.getMessages(result)))
                .addConstraintViolation();
        return false;
    }
}
