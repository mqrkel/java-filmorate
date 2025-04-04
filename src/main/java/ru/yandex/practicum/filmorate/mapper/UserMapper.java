package ru.yandex.practicum.filmorate.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dto.RequestUserDto;
import ru.yandex.practicum.filmorate.dto.RequestUserWithIdDto;
import ru.yandex.practicum.filmorate.dto.ResponseUserDto;
import ru.yandex.practicum.filmorate.model.User;

@UtilityClass
public class UserMapper {
    public static User convertToUser(RequestUserWithIdDto userDto) {
        return User.builder()
                .id(userDto.id())
                .email(userDto.email())
                .login(userDto.login())
                .name(userDto.getValidName())
                .birthday(userDto.birthday())
                .build();
    }

    public static User convertToUser(RequestUserDto userDto) {
        return User.builder()
                .email(userDto.email())
                .login(userDto.login())
                .name(userDto.getValidName())
                .birthday(userDto.birthday())
                .build();
    }

    public static ResponseUserDto convertToResponseUserDto(User user) {
        return ResponseUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }
}