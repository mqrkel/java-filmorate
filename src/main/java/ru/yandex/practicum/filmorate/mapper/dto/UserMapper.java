package ru.yandex.practicum.filmorate.mapper.dto;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserRequestDto;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;

@Component
public class UserMapper {
    public UserResponseDto toDto(ru.yandex.practicum.filmorate.model.User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getValidName())
                .birthday(user.getBirthday())
                .friendshipIds(user.getFriendshipIds())
                .likedFilms(user.getLikedFilms())
                .build();
    }

    public User toEntity(UserRequestDto dto) {
        return User.builder()
                .id(dto.id() != null ? dto.id() : null)
                .email(dto.email())
                .login(dto.login())
                .name(dto.name() != null ? dto.name() : null)
                .birthday(dto.birthday())
                .friendshipIds(dto.friendshipIds() != null ? dto.friendshipIds() : new HashSet<>())
                .likedFilms(dto.likedFilms() != null ? dto.likedFilms() : null)
                .build();
    }
}