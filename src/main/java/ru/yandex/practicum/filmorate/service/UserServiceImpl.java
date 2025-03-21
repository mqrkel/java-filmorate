package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.RequestUserDto;
import ru.yandex.practicum.filmorate.dto.RequestUserWithIdDto;
import ru.yandex.practicum.filmorate.dto.ResponseUserDto;
import ru.yandex.practicum.filmorate.exception.UserCreationException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserUpdateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public ResponseUserDto createUser(RequestUserDto userDto) {
        log.info("Попытка создать пользователя: {}", userDto);
        try {
            User user = convertToUser(userDto);
            userStorage.addUser(user);
            log.info("Пользователь с id={} успешно создан", user.getId());
            return convertToResponseUserDto(user);
        } catch (Exception e) {
            log.error("Ошибка при создании пользователя: {}. Причина: {}", userDto, e.getMessage(), e);
            throw new UserCreationException("Не удалось создать пользователя: " + userDto, e);
        }
    }

    @Override
    public ResponseUserDto updateUser(RequestUserWithIdDto userDto) {
        log.info("Попытка обновить пользователя с id={}", userDto.id());

        try {
            User user = convertToUser(userDto);

            User updatedUser = userStorage.updateUser(user).orElseThrow(() -> {
                final String message = "Пользователь с id= %d не найден".formatted(userDto.id());
                log.warn(message);
                return new UserNotFoundException(message);
            });
            log.info("Пользователь обновлён: {}", updatedUser);
            return convertToResponseUserDto(updatedUser);
        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя с id={}. Причина: {}", userDto.id(), e.getMessage(), e);
            throw new UserUpdateException("Не удалось обновить пользователя с id=" + userDto.id(), e);
        }
    }

    @Override
    public List<ResponseUserDto> getUsers() {
        log.info("Получение списка всех пользователей.");
        return userStorage.getUsers().stream()
                .map(this::convertToResponseUserDto)
                .toList();
    }

    private User convertToUser(RequestUserWithIdDto userDto) {
        return User.builder()
                .id(userDto.id())
                .email(userDto.email())
                .login(userDto.login())
                .name(userDto.getValidName())
                .birthday(userDto.birthday())
                .build();
    }

    private User convertToUser(RequestUserDto userDto) {
        return User.builder()
                .email(userDto.email())
                .login(userDto.login())
                .name(userDto.getValidName())
                .birthday(userDto.birthday())
                .build();
    }

    private ResponseUserDto convertToResponseUserDto(User user) {
        return ResponseUserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }
}