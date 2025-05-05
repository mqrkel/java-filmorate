package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.dto.UserRequestDto;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.dto.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDto> getAllUsers() {
        log.info("Получение списка всех пользователей.");
        return mapToUserDtoList(userDao.getUsers());
    }

    @Override
    public UserResponseDto getUserById(Integer userId) {
        log.info("Получение пользователя с id={}", userId);
        return mapToUserDto(getExistingUser(userId));
    }

    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        User user = userMapper.toEntity(dto);
        log.info("Создание пользователя: {}", user);
        User createdUser = userDao.createUser(user);
        log.info("Пользователь с id={} успешно создан", createdUser.getId());
        return mapToUserDto(createdUser);
    }

    @Override
    public UserResponseDto updateUser(UserRequestDto dto) {
        User user = userMapper.toEntity(dto);
        log.info("Обновление пользователя с id={}", user.getId());
        getExistingUser(user.getId());
        User updatedUser = userDao.updateUser(user);
        log.info("Пользователь с id={} обновлен", updatedUser.getId());
        return mapToUserDto(updatedUser);
    }


    @Override
    public void deleteUserById(Integer userId) {
        log.info("Удаление пользователя с id={}", userId);
        getExistingUser(userId);
        userDao.deleteUserById(userId);
        log.info("Пользователь с id={} удален", userId);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        log.info("Добавление в друзья: userId={}, friendId={}", userId, friendId);
        validateSelfFriendship(userId, friendId);
        getExistingUser(userId);
        getExistingUser(friendId);
        userDao.addFriend(userId, friendId);
        log.info("Пользователь с id={} добавил в друзья пользователя с id={}", userId, friendId);
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        log.info("Удаление из друзей: userId={}, friendId={}", userId, friendId);
        validateSelfFriendship(userId, friendId);
        getExistingUser(userId);
        getExistingUser(friendId);
        userDao.removeFriend(userId, friendId);
        log.info("Пользователь с id={} удалил из друзей пользователя с id={}", userId, friendId);
    }

    @Override
    public List<UserResponseDto> getUserFriends(Integer userId) {
        log.info("Получение списка друзей пользователя с id={}", userId);
        getExistingUser(userId);
        return mapToUserDtoList(userDao.getUserFriends(userId));
    }

    @Override
    public List<UserResponseDto> getCommonFriends(Integer userId, Integer friendId) {
        log.info("Получение списка общих друзей для userId={} и friendId={}", userId, friendId);
        validateSelfFriendship(userId, friendId);
        getExistingUser(userId);
        getExistingUser(friendId);
        return mapToUserDtoList(userDao.getCommonFriends(userId, friendId));
    }

    private User getExistingUser(Integer userId) {
        return userDao.getUserById(userId).orElseThrow(() -> {
            String message = "Пользователь с id=%d не найден".formatted(userId);
            log.warn(message);
            return new NotFoundException(message);
        });
    }


    private static void validateSelfFriendship(Integer userId, Integer friendId) {
        if (Objects.equals(userId, friendId)) {
            String message = "Пользователь не может добавить сам себя в список друзей";
            log.warn(message);
            throw new ValidationException(message);
        }
    }

    private UserResponseDto mapToUserDto(User user) {
        return userMapper.toDto(user);
    }

    private List<UserResponseDto> mapToUserDtoList(List<User> users) {
        return users.stream().map(userMapper::toDto).toList();
    }
}