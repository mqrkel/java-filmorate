package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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

    @GetMapping("/{id}")
    public ResponseUserDto getUser(@Positive @PathVariable int id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFriend(@PathVariable @Positive Integer id, @PathVariable @Positive Integer friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriend(@PathVariable @Positive Integer id, @PathVariable @Positive Integer friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<ResponseUserDto> getFriends(@PathVariable @Positive Integer id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<ResponseUserDto> getCommonFriends(@PathVariable @Positive Integer id, @PathVariable @Positive Integer otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}