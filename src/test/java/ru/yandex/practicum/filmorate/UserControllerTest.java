package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.dto.RequestUserDto;
import ru.yandex.practicum.filmorate.dto.ResponseUserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private RequestUserDto validRequestUserDto;

    @BeforeEach
    void setUp() {
        validRequestUserDto = RequestUserDto.builder()
                .email("test@example.com")
                .login("testlogin")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
    }

    @Test
    @DisplayName("Добавление пользователя должно возвращать созданного пользователя")
    void addUser_ShouldReturnCreatedUser() throws Exception {
        ResponseUserDto responseUserDto = ResponseUserDto.builder()
                .id(1)
                .email("test@example.com")
                .login("testlogin")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        when(userService.createUser(validRequestUserDto)).thenReturn(responseUserDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestUserDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Добавление пользователя должно возвращать ошибку, если email некорректен")
    void addUser_ShouldReturnBadRequest_WhenInvalidEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(RequestUserDto.builder()
                                .email("invalidemail")
                                .login("testlogin")
                                .name("Test User")
                                .birthday(LocalDate.of(1990, 1, 1))
                                .build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Некорректный email"));
    }

    @Test
    @DisplayName("Добавление пользователя должно возвращать ошибку, если логин содержит пробелы")
    void addUser_ShouldReturnBadRequest_WhenLoginContainsSpaces() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(RequestUserDto.builder()
                                .email("test@example.com")
                                .login("test login")
                                .name("Test User")
                                .birthday(LocalDate.of(1990, 1, 1))
                                .build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Логин не должен содержать пробелы"));
    }

    @Test
    @DisplayName("Добавление пользователя должно возвращать ошибку, если дата рождения в будущем")
    void addUser_ShouldReturnBadRequest_WhenBirthdayIsInFuture() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(RequestUserDto.builder()
                                .email("test@example.com")
                                .login("testlogin")
                                .name("Test User")
                                .birthday(LocalDate.of(3000, 1, 1))
                                .build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Дата рождения не может быть в будущем"));
    }

    @Test
    @DisplayName("Добавление пользователя должно возвращать ошибку, если email пустой")
    void addUser_ShouldReturnBadRequest_WhenEmailIsNull() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(RequestUserDto.builder()
                                .login("testlogin")
                                .name("Test User")
                                .birthday(LocalDate.of(1990, 1, 1))
                                .build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Email не должен быть пустым"));
    }

    @Test
    @DisplayName("Добавление пользователя должно использовать логин в качестве имени, если имя пустое")
    void addUser_ShouldUseLoginAsName_WhenNameIsEmpty() throws Exception {
        ResponseUserDto responseUserDto = ResponseUserDto.builder()
                .id(1)
                .email("test@example.com")
                .login("testlogin")
                .name("testlogin")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        when(userService.createUser(any(RequestUserDto.class))).thenReturn(responseUserDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(RequestUserDto.builder()
                                .email("test@example.com")
                                .login("testlogin")
                                .name("")
                                .birthday(LocalDate.of(1990, 1, 1))
                                .build())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("testlogin"));
    }

    @Test
    @DisplayName("Получение пользователя по id должно возвращать пользователя")
    void getUser_ShouldReturnUser() throws Exception {
        ResponseUserDto responseUserDto = ResponseUserDto.builder()
                .id(1)
                .email("test@example.com")
                .login("testlogin")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        when(userService.getUser(1)).thenReturn(responseUserDto);

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.login").value("testlogin"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.birthday").value("1990-01-01"));
    }

    @Test
    @DisplayName("Получение пользователя по id должно возвращать ошибку, если пользователь не найден")
    void getUser_ShouldReturnNotFound_WhenUserNotFound() throws Exception {
        when(userService.getUser(999)).thenThrow(new NotFoundException("Пользователь с id=999 не найден"));

        mockMvc.perform(get("/users/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]").value("Пользователь с id=999 не найден"));
    }

    @Test
    @DisplayName("Добавить друга пользователю")
    void addFriend_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(put("/users/{userId}/friends/{friendId}", 1, 2))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Удаление из друзей")
    void removeFriend_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/users/1/friends/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Получение списка друзей пользователя должно возвращать список пользователей")
    void getFriends_ShouldReturnUserList() throws Exception {
        ResponseUserDto friend1 = ResponseUserDto.builder()
                .id(2)
                .email("friend1@example.com")
                .login("friend1")
                .name("Friend One")
                .birthday(LocalDate.of(1992, 2, 2))
                .build();
        ResponseUserDto friend2 = ResponseUserDto.builder()
                .id(3)
                .email("friend2@example.com")
                .login("friend2")
                .name("Friend Two")
                .birthday(LocalDate.of(1993, 3, 3))
                .build();

        when(userService.getUserFriends(1)).thenReturn(List.of(friend1, friend2));

        mockMvc.perform(get("/users/1/friends"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[1].id").value(3));
    }

    @Test
    @DisplayName("Получение общих друзей должно возвращать список пользователей")
    void getCommonFriends_ShouldReturnCommonFriendsList() throws Exception {
        ResponseUserDto commonFriend1 = ResponseUserDto.builder()
                .id(2)
                .email("commonFriend1@example.com")
                .login("commonFriend1")
                .name("Common Friend One")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        ResponseUserDto commonFriend2 = ResponseUserDto.builder()
                .id(3)
                .email("commonFriend2@example.com")
                .login("commonFriend2")
                .name("Common Friend Two")
                .birthday(LocalDate.of(1992, 2, 2))
                .build();

        when(userService.getCommonFriends(1, 4)).thenReturn(List.of(commonFriend1, commonFriend2));

        mockMvc.perform(get("/users/1/friends/common/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[1].id").value(3));
    }
}