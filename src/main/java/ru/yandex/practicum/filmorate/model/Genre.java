package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "name"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Genre {
    @PositiveOrZero
    Integer id;
    String name;
}