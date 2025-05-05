## 🗄️ Структура базы данных

Приложение использует реляционную базу данных для хранения информации о фильмах, пользователях и их взаимодействиях.

Ниже представлены основные таблицы:

### 🎬 films
Хранит данные о фильмах:
- `title` — название фильма  
- `description` — описание  
- `duration` — продолжительность  
- `release_date` — дата релиза  
- `mpa_rating_id` — ссылка на возрастной рейтинг

### 👤 users
Содержит информацию о пользователях:
- `email` — электронная почта  
- `full_name` — имя  
- `login` — логин  
- `birth_date` — дата рождения

### 🎭 genres
Справочник жанров фильмов:
- Комедия, драма, боевик и т.д.

### 🔞 mpa_ratings
Справочник возрастных рейтингов:
- `G`, `PG`, `PG-13`, `R`, `NC-17`

### 🎞️ film_genres
Связывает фильмы с жанрами (**многие-ко-многим**):
- `film_id` ↔ `genre_id`

### 👍 film_likes
Хранит информацию о лайках фильмов пользователями:
- `film_id`, `user_id`

### 🤝 friendships
Реализует **односторонние дружеские связи** между пользователями:
- `user_id` добавляет в друзья `friend_id`

## Схема базы данных

### Таблицы

#### `genres`
| Столбец     | Тип данных          | Примечание                    |
|-------------|---------------------|------------------------------|
| genre_id    | INTEGER             | PRIMARY KEY, AUTO_INCREMENT   |
| name        | CHARACTER VARYING(128) | NOT NULL                    |

#### `mpa_ratings`
| Столбец      | Тип данных          | Примечание                    |
|--------------|---------------------|------------------------------|
| mpa_rating_id | INTEGER            | PRIMARY KEY, AUTO_INCREMENT   |
| name         | CHARACTER VARYING(50) | NOT NULL, CHECK ('G', 'PG', 'PG-13', 'R', 'NC-17') |

#### `films`
| Столбец        | Тип данных          | Примечание                    |
|----------------|---------------------|------------------------------|
| film_id        | INTEGER             | PRIMARY KEY, AUTO_INCREMENT   |
| title          | CHARACTER VARYING(255) | NOT NULL                    |
| description    | CHARACTER VARYING(255) |                              |
| release_date   | DATE                | NOT NULL                     |
| duration       | INTEGER             | NOT NULL                     |
| mpa_rating_id  | INTEGER             | FOREIGN KEY (mpa_ratings)    |

#### `users`
| Столбец    | Тип данных          | Примечание                    |
|------------|---------------------|------------------------------|
| user_id    | INTEGER             | PRIMARY KEY, AUTO_INCREMENT   |
| email      | CHARACTER VARYING(255) | NOT NULL                    |
| full_name  | CHARACTER VARYING(128) | NOT NULL                    |
| login      | CHARACTER VARYING(128) | NOT NULL                    |
| birth_date | DATE                | CHECK (birth_date <= CURRENT_DATE) |

#### `film_genres`
| Столбец    | Тип данных          | Примечание                    |
|------------|---------------------|------------------------------|
| film_id    | INTEGER             | FOREIGN KEY (films)          |
| genre_id   | INTEGER             | FOREIGN KEY (genres)         |

#### `film_likes`
| Столбец    | Тип данных          | Примечание                    |
|------------|---------------------|------------------------------|
| film_like_id | INTEGER           | PRIMARY KEY, AUTO_INCREMENT   |
| film_id    | INTEGER             | FOREIGN KEY (films)          |
| user_id    | INTEGER             | FOREIGN KEY (users)          |

#### `friendships`
| Столбец    | Тип данных          | Примечание                    |
|------------|---------------------|------------------------------|
| user_id    | INTEGER             | FOREIGN KEY (users)          |
| friend_id  | INTEGER             | FOREIGN KEY (users)          |

### Индексы

- `FILMS_MPA_RATING_FK_INDEX_3`: Индекс на `mpa_rating_id` в таблице `films`.
- `FILM_GENRES_FILMS_FK_INDEX_C`: Индекс на `film_id` в таблице `film_genres`.
- `FILM_GENRES_GENRES_FK_INDEX_C`: Индекс на `genre_id` в таблице `film_genres`.
- `FILM_LIKES_FILMS_FK_INDEX_7`: Индекс на `film_id` в таблице `film_likes`.
- `FILM_LIKES_USERS_FK_INDEX_7`: Индекс на `user_id` в таблице `film_likes`.
- `FRIENDSHIPS_USERS_FK_INDEX_D`: Индекс на `user_id` в таблице `friendships`.