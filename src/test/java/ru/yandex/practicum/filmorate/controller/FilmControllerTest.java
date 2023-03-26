package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController controller = new FilmController();
    Film film1 = Film.builder()
            .id(1)
            .description("description")
            .name("nameTest")
            .releaseDate(LocalDate.of(2010, 10, 10))
            .duration(10)
            .build();
    Film film1Update = Film.builder()
            .id(1)
            .description("NewDescription")
            .name("NewNameTest")
            .releaseDate(LocalDate.of(2011, 10, 10))
            .duration(11)
            .build();
    Film filmNoName = Film.builder()
            .description("description")
            .releaseDate(LocalDate.of(2010, 10, 10))
            .duration(10)
            .build();

    Film filmNoDescription = Film.builder()
            .name("nameTest")
            .releaseDate(LocalDate.of(2010, 10, 10))
            .duration(10)
            .build();
    Film filmNoReleaseDate = Film.builder()
            .description("description")
            .name("nameTest")
            .duration(10)
            .build();
    Film filmNoDuration = Film.builder()
            .description("description")
            .name("nameTest")
            .releaseDate(LocalDate.of(2010, 10, 10))
            .build();

    Film filmIncorrectDescription = Film.builder()
            .name("nameTest")
            .description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                    "Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, " +
                    "а именно 20 миллионов. о Куглов, который за время «своего отсутствия»" +
                    ", стал кандидатом Коломбани.")
            .releaseDate(LocalDate.of(2010, 10, 10))
            .duration(10)
            .build();
    Film filmIncorrectReleaseDate = Film.builder()
            .description("description")
            .name("nameTest")
            .duration(10)
            .releaseDate(LocalDate.of(1010, 10, 10))
            .build();
    Film filmIncorrectDuration = Film.builder()
            .description("description")
            .name("nameTest")
            .releaseDate(LocalDate.of(2010, 10, 10))
            .duration(-10)
            .build();
    Film FilmIncorrect = Film.builder()
            .build();


    @Test
    void put() {
        controller.create(film1Update);
        assertEquals(controller.put(film1Update), film1Update);
        assertNotEquals(controller.put(film1Update), film1);
        try {
            controller.put(filmNoDescription);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Описания не может быть пустым или больше 200 символов.");
        }
        try {
            controller.put(filmNoName);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Название фильма не может быть пустым.");
        }
        try {
            controller.put(filmNoDuration);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Продолжительность фильма неможет быть меньше или равна 0.");
        }
        try {
            controller.put(filmNoReleaseDate);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата релиза не может быть пустой или раньше чем 28.12.1895.");
        }
        try {
            controller.put(filmIncorrectDescription);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Описания не может быть пустым или больше 200 символов.");
        }
        try {
            controller.put(filmIncorrectDuration);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Продолжительность фильма неможет быть меньше или равна 0.");
        }
        try {
            controller.put(filmIncorrectReleaseDate);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата релиза не может быть пустой или раньше чем 28.12.1895.");
        }
        try {
            controller.put(FilmIncorrect);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Все данные фильма пусты.");
        }
        assertEquals(controller.findAll().size(),1);
    }

    @Test
    void create() {
        assertEquals(controller.create(film1), film1);
        try {
            controller.create(filmNoDescription);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Описания не может быть пустым или больше 200 символов.");
        }
        try {
            controller.create(filmNoName);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Название фильма не может быть пустым.");
        }
        try {
            controller.create(filmNoDuration);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Продолжительность фильма неможет быть меньше или равна 0.");
        }
        try {
            controller.create(filmNoReleaseDate);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата релиза не может быть пустой или раньше чем 28.12.1895.");
        }
        try {
            controller.create(filmIncorrectDescription);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Описания не может быть пустым или больше 200 символов.");
        }
        try {
            controller.create(filmIncorrectDuration);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Продолжительность фильма неможет быть меньше или равна 0.");
        }
        try {
            controller.create(filmIncorrectReleaseDate);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата релиза не может быть пустой или раньше чем 28.12.1895.");
        }
        try {
            controller.create(FilmIncorrect);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Все данные фильма пусты.");
        }
        assertEquals(controller.findAll().size(),1);
    }
}