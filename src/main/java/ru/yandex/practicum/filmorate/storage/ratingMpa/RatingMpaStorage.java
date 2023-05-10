package ru.yandex.practicum.filmorate.storage.ratingMpa;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;

public interface RatingMpaStorage {
    RatingMpa getRatingMpaById(Long ratingId);

    List<RatingMpa> getRatingsMpa();
}
