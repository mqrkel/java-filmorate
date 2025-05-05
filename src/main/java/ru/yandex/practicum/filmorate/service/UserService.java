package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.UserRequestDto;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserRequestDto dto);

    UserResponseDto updateUser(UserRequestDto dto);

    List<UserResponseDto> getAllUsers();

    void addFriend(Integer userId, Integer friendId);

    void removeFriend(Integer userId, Integer friendId);

    List<UserResponseDto> getUserFriends(Integer userId);

    List<UserResponseDto> getCommonFriends(Integer userId, Integer friendId);

    UserResponseDto getUserById(Integer userId);

    void deleteUserById(Integer userId);
}