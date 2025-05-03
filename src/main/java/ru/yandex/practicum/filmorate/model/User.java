package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "login"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Integer id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    @Builder.Default
    Set<Integer> friendshipIds = new HashSet<>();
    @Builder.Default
    Set<Integer> likedFilms = new HashSet<>();

    public String getValidName() {
        return (name == null || name.isBlank()) ? login : name;
    }
}