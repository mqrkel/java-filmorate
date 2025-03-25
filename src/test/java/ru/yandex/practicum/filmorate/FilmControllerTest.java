package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.dto.RequestFilmDto;
import ru.yandex.practicum.filmorate.dto.RequestFilmWithIdDto;
import ru.yandex.practicum.filmorate.dto.ResponseFilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FilmController.class)
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FilmService filmService;

    private RequestFilmDto validRequestFilmDto;

    @BeforeEach
    void setUp() {
        validRequestFilmDto = RequestFilmDto.builder()
                .name("Test Film")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 3, 1))
                .duration(120)
                .build();
    }

    @Test
    @DisplayName("Получить список всех фильмов")
    void testGetFilms() throws Exception {
        when(filmService.getAllFilms()).thenReturn(List.of(
                ResponseFilmDto.builder()
                        .id(1)
                        .name("Test Film")
                        .description("Description")
                        .releaseDate(LocalDate.of(2025, 3, 1))
                        .duration(120)
                        .build()
        ));

        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Film"));
    }

    @Test
    @DisplayName("Создать новый фильм")
    void testAddFilm() throws Exception {
        ResponseFilmDto responseFilmDto = ResponseFilmDto.builder()
                .id(1)
                .name("Test Film")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 3, 1))
                .duration(120)
                .build();

        when(filmService.createFilm(any(RequestFilmDto.class))).thenReturn(responseFilmDto);

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(validRequestFilmDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Film"));
    }

    @Test
    @DisplayName("Обновить фильм")
    void testUpdateFilm() throws Exception {
        RequestFilmWithIdDto filmDto = RequestFilmWithIdDto.builder()
                .id(1)
                .name("Updated Test Film")
                .description("Updated Description")
                .releaseDate(LocalDate.of(2025, 3, 2))
                .duration(130)
                .build();

        ResponseFilmDto responseFilmDto = ResponseFilmDto.builder()
                .id(1)
                .name("Updated Test Film")
                .description("Updated Description")
                .releaseDate(LocalDate.of(2025, 3, 2))
                .duration(130)
                .build();

        when(filmService.updateFilm(any(RequestFilmWithIdDto.class))).thenReturn(responseFilmDto);

        mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Test Film"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    @DisplayName("Пытаемся создать фильм с пустым названием")
    void testAddFilmWithEmptyName() throws Exception {
        RequestFilmDto filmDto = RequestFilmDto.builder()
                .name("")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 3, 1))
                .duration(120)
                .build();

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Название не может быть пустым"));
    }

    @Test
    @DisplayName("Создание фильма с максимальной длиной описания")
    void testAddFilmWithMaxDescription() throws Exception {
        String longDescription = "A".repeat(200);
        RequestFilmDto filmDto = RequestFilmDto.builder()
                .name("Film with max description")
                .description(longDescription)
                .releaseDate(LocalDate.of(2025, 3, 1))
                .duration(120)
                .build();

        ResponseFilmDto responseFilmDto = ResponseFilmDto.builder()
                .id(1)
                .name("Film with max description")
                .description(longDescription)
                .releaseDate(LocalDate.of(2025, 3, 1))
                .duration(120)
                .build();

        when(filmService.createFilm(any(RequestFilmDto.class))).thenReturn(responseFilmDto);

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value(longDescription));
    }

    @Test
    @DisplayName("Ошибка при создании фильма с некорректной датой выпуска")
    void testAddFilmWithInvalidReleaseDate() throws Exception {
        RequestFilmDto filmDto = RequestFilmDto.builder()
                .name("Invalid Date Film")
                .description("Description")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(120)
                .build();

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Дата релиза не может быть раньше 1895-12-28"));
    }

    @Test
    @DisplayName("Ошибка при создании фильма с отрицательной длительностью")
    void testAddFilmWithNegativeDuration() throws Exception {
        RequestFilmDto filmDto = RequestFilmDto.builder()
                .name("Film with negative duration")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 3, 1))
                .duration(-120)
                .build();

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Продолжительность должна быть положительным числом"));
    }

    @Test
    @DisplayName("Ошибка при создании фильма с нулевой длительностью")
    void testAddFilmWithZeroDuration() throws Exception {
        RequestFilmDto filmDto = RequestFilmDto.builder()
                .name("Film with zero duration")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 3, 1))
                .duration(0)
                .build();

        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Продолжительность должна быть положительным числом"));
    }

    @Test
    @DisplayName("Обновление фильма с некорректным id")
    void testUpdateFilmWithInvalidId() throws Exception {
        RequestFilmWithIdDto filmDto = RequestFilmWithIdDto.builder()
                .id(999)
                .name("Non-Existent Film")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 3, 1))
                .duration(120)
                .build();

        when(filmService.updateFilm(any(RequestFilmWithIdDto.class)))
                .thenThrow(new NotFoundException("Фильм не найден"));

        mockMvc.perform(put("/films")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(filmDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]").value("Фильм не найден"));
    }
}