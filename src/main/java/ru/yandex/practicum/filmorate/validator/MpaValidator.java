package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class MpaValidator implements ConstraintValidator<ValidMpaName, String> {

    private static final Set<String> VALID_MPA_NAMES = Set.of(
            "G", "PG", "PG-13", "R", "NC-17"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && VALID_MPA_NAMES.contains(value.toUpperCase());
    }
}