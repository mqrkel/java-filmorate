package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    private final Set<Integer> friendshipIds = new HashSet<>();

    public void addFriendship(Integer friendshipId) {
        friendshipIds.add(friendshipId);
    }

    public void removeFriendship(Integer friendshipId) {
        friendshipIds.remove(friendshipId);
    }
}