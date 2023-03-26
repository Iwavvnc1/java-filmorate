package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class User {
    int id;
    String email;
    String login;
    String name;
    LocalDate birthday;
}
