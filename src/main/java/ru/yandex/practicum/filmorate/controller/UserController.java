package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.RequestUserDto;
import ru.yandex.practicum.filmorate.dto.RequestUserWithIdDto;
import ru.yandex.practicum.filmorate.dto.ResponseUserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseUserDto addUser(@Valid @RequestBody RequestUserDto userDto) {
        return userService.createUser(userDto);
    }

    @PutMapping
    public ResponseUserDto updateUser(@Valid @RequestBody RequestUserWithIdDto userDto) {
        return userService.updateUser(userDto);
    }

    @GetMapping
    public List<ResponseUserDto> getAllUsers() {
        return userService.getUsers();
    }
}