package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Builder;
import ru.yandex.practicum.filmorate.validator.NoSpaces;

import java.time.LocalDate;

@Builder
public record RequestUserWithIdDto(
        @Positive(message = "Не корректный id")
        int id,

        @Email(message = "Не верный формат email")
        @NotEmpty(message = "Поле email пустое")
        @NotBlank
        String email,

        @NotEmpty(message = "Поле login пустое")
        @NotBlank(message = "Поле login пустое")
        @NoSpaces
        String login,

        String name,

        @JsonFormat(pattern = "yyyy-MM-dd")
        @Past
        LocalDate birthday
) {
    public String getValidName() {
        return (name == null || name.isBlank()) ? login : name;
    }
}