package ru.yandex.practicum.filmorate.dao;

import lombok.Getter;

@Getter
public enum UserSql {

    CREATE_USER("""
            INSERT INTO USERS(EMAIL, FULL_NAME, LOGIN, BIRTH_DATE)
            VALUES (?, ?, ?, ?);
            """),

    UPDATE_USER("""
            UPDATE USERS
            SET EMAIL      = ?,
            FULL_NAME  = ?,
            LOGIN      = ?,
            BIRTH_DATE = ?
            WHERE USER_ID = ?;
            """),

    GET_ALL_USERS("""
            SELECT USER_ID,
            EMAIL,
            FULL_NAME,
            LOGIN,
            BIRTH_DATE
            FROM USERS;
            """),

    GET_USER_BY_ID("""
            SELECT USER_ID,
            EMAIL,
            FULL_NAME,
            LOGIN,
            BIRTH_DATE
            FROM USERS
            WHERE USER_ID = ?;
            """),

    CHECK_EXISTING_FRIENDSHIP("""
            SELECT COUNT(USER_ID)
            FROM FRIENDSHIPS
            WHERE (USER_ID = ? AND FRIEND_ID = ?);
            """),

    ADD_FRIEND("""
            INSERT INTO FRIENDSHIPS(USER_ID, FRIEND_ID)
            VALUES (?, ?);
            """),

    REMOVE_FRIEND("""
            DELETE FROM FRIENDSHIPS
            WHERE USER_ID = ?
            AND FRIEND_ID = ?;
            """),

    GET_USER_FRIENDS("""
            SELECT U.USER_ID,
            U.EMAIL,
            U.FULL_NAME,
            U.LOGIN,
            U.BIRTH_DATE
            FROM USERS AS U
            JOIN FRIENDSHIPS F ON U.USER_ID = F.FRIEND_ID
            WHERE F.USER_ID = ?;
            """),

    GET_COMMON_FRIENDS("""
            SELECT U.USER_ID,
            U.EMAIL,
            U.FULL_NAME,
            U.LOGIN,
            U.BIRTH_DATE
            FROM FRIENDSHIPS F1
            JOIN FRIENDSHIPS F2 ON F1.FRIEND_ID = F2.FRIEND_ID
            JOIN USERS U ON U.USER_ID = F1.FRIEND_ID
            WHERE F1.USER_ID = ?
            AND F2.USER_ID = ?;
            """),

    DELETE_USER("""
            DELETE FROM USERS
            WHERE USER_ID = ?;
            """),

    REMOVE_ALL_FRIENDS("""
            DELETE FROM FRIENDSHIPS
            WHERE USER_ID = ? OR FRIEND_ID = ?;
            """);

    private final String query;

    UserSql(String query) {
        this.query = query;
    }
}