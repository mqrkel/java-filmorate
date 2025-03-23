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
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
}