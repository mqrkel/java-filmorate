package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dto.RequestUserDto;
import ru.yandex.practicum.filmorate.dto.RequestUserWithIdDto;
import ru.yandex.practicum.filmorate.dto.ResponseUserDto;

import java.util.List;

public interface UserService {

    ResponseUserDto createUser(RequestUserDto userDto);

    ResponseUserDto updateUser(RequestUserWithIdDto userDto);

    List<ResponseUserDto> getUsers();
}