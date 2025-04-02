package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.RequestUserDto;
import ru.yandex.practicum.filmorate.dto.RequestUserWithIdDto;
import ru.yandex.practicum.filmorate.dto.ResponseUserDto;
import ru.yandex.practicum.filmorate.exception.CreationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
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
            User user = UserMapper.convertToUser(userDto);
            userStorage.addUser(user);
            log.info("Пользователь с id={} успешно создан", user.getId());
            return UserMapper.convertToResponseUserDto(user);
        } catch (Exception e) {
            log.error("Ошибка при создании пользователя: {}. Причина: {}", userDto, e.getMessage(), e);
            throw new CreationException("Не удалось создать пользователя: " + userDto, e);
        }
    }

    @Override
    public ResponseUserDto updateUser(RequestUserWithIdDto userDto) {
        log.info("Попытка обновить пользователя с id={}", userDto.id());

        try {
            User user = UserMapper.convertToUser(userDto);

            User updatedUser = userStorage.updateUser(user).orElseThrow(() -> {
                final String message = "Пользователь с id= %d не найден".formatted(userDto.id());
                log.warn(message);
                return new NotFoundException(message);
            });
            log.info("Пользователь обновлён: {}", updatedUser);
            return UserMapper.convertToResponseUserDto(updatedUser);
        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя с id={}. Причина: {}", userDto.id(), e.getMessage(), e);
            throw new UpdateException("Не удалось обновить пользователя с id=" + userDto.id(), e);
        }
    }

    @Override
    public List<ResponseUserDto> getUsers() {
        log.info("Получение списка всех пользователей.");
        return userStorage.getUsers().stream()
                .map(UserMapper::convertToResponseUserDto)
                .toList();
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        log.info("Попытка добавить в друзья: userId={}, friendId={}", userId, friendId);
        boolean added = userStorage.addFriend(userId, friendId)
                .orElseThrow(() -> {
                    final String message = "Ошибка при добавлении в друзья. Проверьте корректность ID";
                    log.warn(message);
                    return new NotFoundException(message);
                });
        if (added) {
            log.info("Пользователь с id={} добавил в друзья пользователя с id={}", userId, friendId);
        }
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        log.info("Попытка удалить из друзей: userId={}, friendId={}", userId, friendId);
        boolean removed = userStorage.removeFriend(userId, friendId)
                .orElseThrow(() -> {
                    final String message = "Ошибка при удалении из друзей. Проверьте корректность ID";
                    log.warn(message);
                    return new NotFoundException(message);
                });

        if (removed) {
            log.info("Пользователь с id={} удалил из друзей пользователя с id={}", userId, friendId);
        }
    }

    @Override
    public List<ResponseUserDto> getUserFriends(Integer userId) {
        log.info("Получение списка друзей пользователя с id={}", userId);
        return userStorage.getUserFriends(userId)
                .orElseThrow(() -> {
                    final String message = "Пользователь с id=%d не найден".formatted(userId);
                    log.warn(message);
                    return new NotFoundException(message);
                })
                .stream()
                .map(UserMapper::convertToResponseUserDto)
                .toList();
    }

    @Override
    public List<ResponseUserDto> getCommonFriends(Integer userId, Integer friendId) {
        log.info("Получение общих друзей между userId={} и friendId={}", userId, friendId);
        return userStorage.getCommonFriends(userId, friendId)
                .orElseThrow(() -> {
                    final String message = "Один из пользователей не найден";
                    log.warn(message);
                    return new NotFoundException(message);
                })
                .stream()
                .map(UserMapper::convertToResponseUserDto)
                .toList();
    }

    @Override
    public ResponseUserDto getUser(Integer id) {
        log.info("Получение пользователя с id={}", id);
        User user = userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=%d не найден".formatted(id)));
        return UserMapper.convertToResponseUserDto(user);
    }
}