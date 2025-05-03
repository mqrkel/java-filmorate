-- Заполнение таблицы genres
INSERT INTO genres (name)
SELECT 'Комедия' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Комедия');
INSERT INTO genres (name)
SELECT 'Драма' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Драма');
INSERT INTO genres (name)
SELECT 'Мультфильм' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Мультфильм');
INSERT INTO genres (name)
SELECT 'Триллер' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Триллер');
INSERT INTO genres (name)
SELECT 'Документальный' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Документальный');
INSERT INTO genres (name)
SELECT 'Боевик' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Боевик');

-- Заполнение таблицы mpa_ratings
INSERT INTO mpa_ratings (name)
SELECT 'G' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE name = 'G');
INSERT INTO mpa_ratings (name)
SELECT 'PG' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE name = 'PG');
INSERT INTO mpa_ratings (name)
SELECT 'PG-13' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE name = 'PG-13');
INSERT INTO mpa_ratings (name)
SELECT 'R' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE name = 'R');
INSERT INTO mpa_ratings (name)
SELECT 'NC-17' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE name = 'NC-17');

-- Заполнение таблицы users
INSERT INTO users (email, full_name, login, birth_date)
SELECT 'alice@example.com', 'Alice Smith', 'alice', '1990-05-10'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'alice@example.com');
INSERT INTO users (email, full_name, login, birth_date)
SELECT 'bob@example.com', 'Bob Johnson', 'bobby', '1985-11-23'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'bob@example.com');
INSERT INTO users (email, full_name, login, birth_date)
SELECT 'charlie@example.com', 'Charlie Rose', 'charlie', '1993-02-17'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'charlie@example.com');

-- Заполнение таблицы films
INSERT INTO films (title, description, release_date, duration, mpa_rating_id)
SELECT 'Funny Movie', 'A very funny movie', '2020-01-15', 90, 1
WHERE NOT EXISTS (SELECT 1 FROM films WHERE title = 'Funny Movie');
INSERT INTO films (title, description, release_date, duration, mpa_rating_id)
SELECT 'Sad Movie', 'A drama film', '2019-10-01', 120, 2
WHERE NOT EXISTS (SELECT 1 FROM films WHERE title = 'Sad Movie');
INSERT INTO films (title, description, release_date, duration, mpa_rating_id)
SELECT 'Animated Film', 'A cartoon for kids', '2021-06-21', 75, 1
WHERE NOT EXISTS (SELECT 1 FROM films WHERE title = 'Animated Film');

-- Связывание фильмов с жанрами
INSERT INTO film_genres (film_id, genre_id)
SELECT 1, 1 WHERE NOT EXISTS (SELECT 1 FROM film_genres WHERE film_id = 1 AND genre_id = 1);
INSERT INTO film_genres (film_id, genre_id)
SELECT 2, 2 WHERE NOT EXISTS (SELECT 1 FROM film_genres WHERE film_id = 2 AND genre_id = 2);
INSERT INTO film_genres (film_id, genre_id)
SELECT 3, 3 WHERE NOT EXISTS (SELECT 1 FROM film_genres WHERE film_id = 3 AND genre_id = 3);

-- Лайки фильмов пользователями
INSERT INTO film_likes (film_id, user_id)
SELECT 1, 1 WHERE NOT EXISTS (SELECT 1 FROM film_likes WHERE film_id = 1 AND user_id = 1);
INSERT INTO film_likes (film_id, user_id)
SELECT 1, 2 WHERE NOT EXISTS (SELECT 1 FROM film_likes WHERE film_id = 1 AND user_id = 2);
INSERT INTO film_likes (film_id, user_id)
SELECT 2, 3 WHERE NOT EXISTS (SELECT 1 FROM film_likes WHERE film_id = 2 AND user_id = 3);

-- Друзья (односторонняя дружба)
INSERT INTO friendships (user_id, friend_id)
SELECT 1, 2 WHERE NOT EXISTS (SELECT 1 FROM friendships WHERE user_id = 1 AND friend_id = 2);
INSERT INTO friendships (user_id, friend_id)
SELECT 1, 3 WHERE NOT EXISTS (SELECT 1 FROM friendships WHERE user_id = 1 AND friend_id = 3);
