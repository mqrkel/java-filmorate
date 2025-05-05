package ru.yandex.practicum.filmorate.dao;

import lombok.Getter;

@Getter
public enum FilmSql {

    GET_ALL_FILMS("""
             SELECT FILMS.FILM_ID,
             FILMS.TITLE,
             FILMS.DESCRIPTION,
             FILMS.RELEASE_DATE,
             FILMS.DURATION,
             FILMS.MPA_RATING_ID
             FROM FILMS;
            """),

    INSERT_FILM("""
            INSERT INTO FILMS (TITLE, DESCRIPTION, RELEASE_DATE,
            DURATION, MPA_RATING_ID)
            VALUES (?, ?, ?, ?, ?);
            """),

    UPDATE_FILM("""
            UPDATE FILMS
            SET TITLE         = ?,
            DESCRIPTION   = ?,
            RELEASE_DATE  = ?,
            DURATION      = ?,
            MPA_RATING_ID = ?
            WHERE FILM_ID = ?;
            """),

    GET_FILM_BY_ID("""
            SELECT FILMS.FILM_ID,
            FILMS.TITLE,
            FILMS.DESCRIPTION,
            FILMS.RELEASE_DATE,
            FILMS.DURATION,
            FILMS.MPA_RATING_ID
            FROM FILMS
            WHERE FILM_ID = ?;
            """),

    GET_POPULAR_FILM("""
            SELECT f.FILM_ID,
            f.TITLE,
            f.DESCRIPTION,
            f.RELEASE_DATE,
            f.DURATION,
            f.MPA_RATING_ID
            FROM FILMS f
            LEFT JOIN FILM_LIKES fl ON f.FILM_ID = fl.FILM_ID
            GROUP BY f.FILM_ID
            ORDER BY COUNT(fl.USER_ID) DESC
            LIMIT ?;
            """),

    ADD_LIKE("""
            INSERT INTO FILM_LIKES(FILM_ID, USER_ID)
            VALUES (?, ?);
            """),

    REMOVE_LIKE("""
            DELETE FROM FILM_LIKES
            WHERE FILM_ID = ?
            AND USER_ID = ?;
            """),

    DELETE_GENRES("""
            DELETE FROM FILM_GENRES WHERE FILM_ID = ?;
            """),

    DELETE_FILMS("""
            DELETE FROM FILMS WHERE FILM_ID = ?;
            """),

    GENRE_INSERT("""
            INSERT INTO FILM_GENRES (film_id, genre_id) VALUES (?, ?);
            """),

    SELECT_GENRE_NAME_GENRE_ID_BY_FILM_ID("""
            SELECT fg.FILM_ID, g.GENRE_ID, g.NAME
            FROM FILM_GENRES fg
            JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID
            WHERE fg.FILM_ID IN (:filmIds)
            """),

    SELECT_MPA_ID_AND_NAME("""
            SELECT f.FILM_ID,m.MPA_RATING_ID, m.NAME
            FROM FILMS f
            JOIN MPA_RATINGS m ON f.MPA_RATING_ID = m.MPA_RATING_ID
            WHERE f.FILM_ID IN (:filmIds)
            """),

    SELECT_IDS_USERS_LIKE("""
            SELECT FILM_ID,USER_ID
            FROM FILM_LIKES
            WHERE FILM_ID IN (:filmIds)
            """),

    CHECK_EXIST_GENRE_ID("""
            SELECT COUNT(GENRE_ID)
            FROM GENRES
            WHERE GENRE_ID = ?
            """),

    CHECK_EXIST_MPA_ID("""
            SELECT COUNT(MPA_RATING_ID)
            FROM MPA_RATINGS
            WHERE MPA_RATING_ID = ?
            """);

    private final String query;

    FilmSql(String query) {
        this.query = query;
    }
}