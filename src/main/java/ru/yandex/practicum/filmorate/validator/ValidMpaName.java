package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MpaValidator.class)
public @interface ValidMpaName {
    String message() default "Не корректное имя для возрастного рейтинга";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}