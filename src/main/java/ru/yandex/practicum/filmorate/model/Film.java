package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class Film {
    int id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
}
