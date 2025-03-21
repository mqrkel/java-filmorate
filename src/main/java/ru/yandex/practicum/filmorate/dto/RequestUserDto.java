package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import ru.yandex.practicum.filmorate.validator.NoSpaces;

import java.time.LocalDate;

@Builder
public record RequestUserDto(
        @NotEmpty(message = "Email не должен быть пустым")
        @Email(message = "Некорректный email")
        @NotBlank(message = "Email не должен быть пустым")
        String email,

        @NotEmpty(message = "Логин не может быть пустым")
        @NotBlank(message = "Логин не может быть пустым")
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