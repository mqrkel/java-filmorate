//package ru.yandex.practicum.filmorate;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.yandex.practicum.filmorate.controller.FilmController;
//import ru.yandex.practicum.filmorate.dto.FilmDto;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.service.FilmService;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(FilmController.class)
//class FilmControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private FilmService filmService;
//
//    private RequestFilmDto validRequestFilmDto;
//
//    @BeforeEach
//    void setUp() {
//        validRequestFilmDto = RequestFilmDto.builder()
//                .name("Test Film")
//                .description("Description")
//                .releaseDate(LocalDate.of(2025, 3, 1))
//                .duration(120)
//                .build();
//    }
//
//    @Test
//    @DisplayName("Получить список всех фильмов")
//    void testGetFilms() throws Exception {
//        when(filmService.getAllFilms()).thenReturn(List.of(
//                ResponseFilmDto.builder()
//                        .id(1)
//                        .name("Test Film")
//                        .description("Description")
//                        .releaseDate(LocalDate.of(2025, 3, 1))
//                        .duration(120)
//                        .build()
//        ));
//
//        mockMvc.perform(get("/films"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value("Test Film"));
//    }
//
//    @Test
//    @DisplayName("Создать новый фильм")
//    void testAddFilm() throws Exception {
//        ResponseFilmDto responseFilmDto = ResponseFilmDto.builder()
//                .id(1)
//                .name("Test Film")
//                .description("Description")
//                .releaseDate(LocalDate.of(2025, 3, 1))
//                .duration(120)
//                .build();
//
//        when(filmService.createFilm(any(RequestFilmDto.class))).thenReturn(responseFilmDto);
//
//        mockMvc.perform(post("/films")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(validRequestFilmDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").value("Test Film"));
//    }
//
//    @Test
//    @DisplayName("Обновить фильм")
//    void testUpdateFilm() throws Exception {
//        FilmDto filmDto = FilmDto.builder()
//                .id(1)
//                .name("Updated Test Film")
//                .description("Updated Description")
//                .releaseDate(LocalDate.of(2025, 3, 2))
//                .duration(130)
//                .build();
//
//        ResponseFilmDto responseFilmDto = ResponseFilmDto.builder()
//                .id(1)
//                .name("Updated Test Film")
//                .description("Updated Description")
//                .releaseDate(LocalDate.of(2025, 3, 2))
//                .duration(130)
//                .build();
//
//        when(filmService.updateFilm(any(FilmDto.class))).thenReturn(responseFilmDto);
//
//        mockMvc.perform(put("/films")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(filmDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Updated Test Film"))
//                .andExpect(jsonPath("$.description").value("Updated Description"));
//    }
//
//    @Test
//    @DisplayName("Пытаемся создать фильм с пустым названием")
//    void testAddFilmWithEmptyName() throws Exception {
//        RequestFilmDto filmDto = RequestFilmDto.builder()
//                .name("")
//                .description("Description")
//                .releaseDate(LocalDate.of(2025, 3, 1))
//                .duration(120)
//                .build();
//
//        mockMvc.perform(post("/films")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(filmDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors[0]").value("Название не может быть пустым"));
//    }
//
//    @Test
//    @DisplayName("Создание фильма с максимальной длиной описания")
//    void testAddFilmWithMaxDescription() throws Exception {
//        String longDescription = "A".repeat(200);
//        RequestFilmDto filmDto = RequestFilmDto.builder()
//                .name("Film with max description")
//                .description(longDescription)
//                .releaseDate(LocalDate.of(2025, 3, 1))
//                .duration(120)
//                .build();
//
//        ResponseFilmDto responseFilmDto = ResponseFilmDto.builder()
//                .id(1)
//                .name("Film with max description")
//                .description(longDescription)
//                .releaseDate(LocalDate.of(2025, 3, 1))
//                .duration(120)
//                .build();
//
//        when(filmService.createFilm(any(RequestFilmDto.class))).thenReturn(responseFilmDto);
//
//        mockMvc.perform(post("/films")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(filmDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.description").value(longDescription));
//    }
//
//    @Test
//    @DisplayName("Ошибка при создании фильма с некорректной датой выпуска")
//    void testAddFilmWithInvalidReleaseDate() throws Exception {
//        RequestFilmDto filmDto = RequestFilmDto.builder()
//                .name("Invalid Date Film")
//                .description("Description")
//                .releaseDate(LocalDate.of(1895, 12, 27))
//                .duration(120)
//                .build();
//
//        mockMvc.perform(post("/films")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(filmDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors[0]").value("Дата релиза не может быть раньше 1895-12-28"));
//    }
//
//    @Test
//    @DisplayName("Ошибка при создании фильма с отрицательной длительностью")
//    void testAddFilmWithNegativeDuration() throws Exception {
//        RequestFilmDto filmDto = RequestFilmDto.builder()
//                .name("Film with negative duration")
//                .description("Description")
//                .releaseDate(LocalDate.of(2025, 3, 1))
//                .duration(-120)
//                .build();
//
//        mockMvc.perform(post("/films")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(filmDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors[0]").value("Продолжительность должна быть положительным числом"));
//    }
//
//    @Test
//    @DisplayName("Ошибка при создании фильма с нулевой длительностью")
//    void testAddFilmWithZeroDuration() throws Exception {
//        RequestFilmDto filmDto = RequestFilmDto.builder()
//                .name("Film with zero duration")
//                .description("Description")
//                .releaseDate(LocalDate.of(2025, 3, 1))
//                .duration(0)
//                .build();
//
//        mockMvc.perform(post("/films")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(filmDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors[0]").value("Продолжительность должна быть положительным числом"));
//    }
//
//    @Test
//    @DisplayName("Обновление фильма с некорректным id")
//    void testUpdateFilmWithInvalidId() throws Exception {
//        FilmDto filmDto = FilmDto.builder()
//                .id(999)
//                .name("Non-Existent Film")
//                .description("Description")
//                .releaseDate(LocalDate.of(2025, 3, 1))
//                .duration(120)
//                .build();
//
//        when(filmService.updateFilm(any(FilmDto.class)))
//                .thenThrow(new NotFoundException("Фильм не найден"));
//
//        mockMvc.perform(put("/films")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(filmDto)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.errors[0]").value("Фильм не найден"));
//    }
//
//    @Test
//    @DisplayName("Получить фильм по ID")
//    void testGetFilmById() throws Exception {
//        ResponseFilmDto responseFilmDto = ResponseFilmDto.builder()
//                .id(1)
//                .name("Test Film")
//                .description("Description")
//                .releaseDate(LocalDate.of(2025, 3, 1))
//                .duration(120)
//                .build();
//
//        when(filmService.getFilm(1)).thenReturn(responseFilmDto);
//
//        mockMvc.perform(get("/films/{id}", 1))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("Test Film"));
//    }
//
//    @Test
//    @DisplayName("Получить фильм по некорректному ID")
//    void testGetFilmByInvalidId() throws Exception {
//        when(filmService.getFilm(999)).thenThrow(new NotFoundException("Фильм не найден"));
//
//        mockMvc.perform(get("/films/{id}", 999))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.errors[0]").value("Фильм не найден"));
//    }
//
//    @Test
//    @DisplayName("Добавить лайк фильму")
//    void testLikeFilm() throws Exception {
//        mockMvc.perform(put("/films/{id}/like/{userId}", 1, 1))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    @DisplayName("Удалить лайк фильма")
//    void testUnlikeFilm() throws Exception {
//        mockMvc.perform(delete("/films/{id}/like/{userId}", 1, 1))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    @DisplayName("Получить популярные фильмы (позитивный тест)")
//    void testGetPopularFilms() throws Exception {
//        List<ResponseFilmDto> popularFilms = List.of(
//                ResponseFilmDto.builder()
//                        .id(1)
//                        .name("Popular Film")
//                        .description("Description")
//                        .releaseDate(LocalDate.of(2025, 3, 1))
//                        .duration(120)
//                        .build()
//        );
//
//        when(filmService.getPopularFilms(5)).thenReturn(popularFilms);
//
//        mockMvc.perform(get("/films/popular").param("count", "5"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value("Popular Film"));
//    }
//
//    @Test
//    @DisplayName("Получить популярные фильмы с некорректным значением")
//    void testGetPopularFilmsWithInvalidValue() throws Exception {
//        mockMvc.perform(get("/films/popular").param("count", "-5"))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Получить топ-10 популярных фильмов из 11 доступных")
//    void testGetTop10PopularFilms() throws Exception {
//        List<ResponseFilmDto> allFilms = List.of(
//                ResponseFilmDto.builder().id(1).name("Film 1").likes(110).build(),
//                ResponseFilmDto.builder().id(2).name("Film 2").likes(100).build(),
//                ResponseFilmDto.builder().id(3).name("Film 3").likes(90).build(),
//                ResponseFilmDto.builder().id(4).name("Film 4").likes(80).build(),
//                ResponseFilmDto.builder().id(5).name("Film 5").likes(70).build(),
//                ResponseFilmDto.builder().id(6).name("Film 6").likes(60).build(),
//                ResponseFilmDto.builder().id(7).name("Film 7").likes(50).build(),
//                ResponseFilmDto.builder().id(8).name("Film 8").likes(40).build(),
//                ResponseFilmDto.builder().id(9).name("Film 9").likes(30).build(),
//                ResponseFilmDto.builder().id(10).name("Film 10").likes(20).build(),
//                ResponseFilmDto.builder().id(11).name("Film 11").likes(10).build()
//        );
//
//        when(filmService.getPopularFilms(10)).thenReturn(allFilms.subList(0, 10));
//
//        mockMvc.perform(get("/films/popular").param("count", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(10))
//                .andExpect(jsonPath("$[0].name").value("Film 1"))
//                .andExpect(jsonPath("$[0].likes").value(110))
//                .andExpect(jsonPath("$[9].name").value("Film 10"))
//                .andExpect(jsonPath("$[9].likes").value(20));
//    }
//}