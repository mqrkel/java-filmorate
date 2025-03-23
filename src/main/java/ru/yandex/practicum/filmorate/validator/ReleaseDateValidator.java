package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, LocalDate> {
    private LocalDate minDate;

    @Override
    public void initialize(ReleaseDate constraintAnnotation) {
        this.minDate = LocalDate.parse(constraintAnnotation.value(), DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        return date != null && !date.isBefore(minDate);
    }
}