package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.RatingMpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class RatingMpaController {
    private final RatingMpaService ratingMpaService;

    public RatingMpaController(RatingMpaService ratingMpaService) {
        this.ratingMpaService = ratingMpaService;
    }

    @GetMapping
    public List<RatingMpa> getRatingsMpa() {
        log.info("Запрос на все MPA");
        return ratingMpaService.getRatingsMpa();
    }

    @GetMapping("/{id}")
    public RatingMpa getRatingMpaById(@PathVariable Long id) {
        log.info("Запрос на получение MPA c id {}", id);
        return ratingMpaService.getRatingMpaById(id);
    }
}