package ru.yandex.practicum.filmorate.TestService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.yandex.practicum.filmorate.adapter.LocalDateAdapter;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FilmServiceTest {
    FilmService service;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
    String film1 = "{\n" +
            "  \"name\": \"nisi eiusmod\",\n" +
            "  \"description\": \"adipisicing\",\n" +
            "  \"releaseDate\": \"1967-03-25\",\n" +
            "  \"duration\": 100\n" +
            "}";
    String film1New = "{\n" +
            "  \"id\":1,\n" +
            "  \"name\": \"nisi eiusmod\",\n" +
            "  \"description\": \"adipisicing\",\n" +
            "  \"releaseDate\": \"1967-03-25\",\n" +
            "  \"duration\": 100,\n" +
            "  \"likes\":[]\n" +
            "}";
    String film2 = "{\n" +
            "  \"name\": \"nisis eiusmod\",\n" +
            "  \"description\": \"adipisicisng\",\n" +
            "  \"releaseDate\": \"1967-02-25\",\n" +
            "  \"duration\": 100\n" +
            "}";
    String film3 = "{\n" +
            "  \"name\": \"nisi eiussmod\",\n" +
            "  \"description\": \"adipisicinsg\",\n" +
            "  \"releaseDate\": \"1967-03-21\",\n" +
            "  \"duration\": 100\n" +
            "}";
    String film4 = "{\n" +
            "  \"name\": \"nisssi eiusssmod\",\n" +
            "  \"description\": \"adipsisicinsg\",\n" +
            "  \"releaseDate\": \"1997-03-21\",\n" +
            "  \"duration\": 100\n" +
            "}";
    String user1 = "{\n" +
            "  \"login\": \"dolore\",\n" +
            "  \"name\": \"Nick Name\",\n" +
            "  \"email\": \"mail@mail.ru\",\n" +
            "  \"birthday\": \"1946-08-20\"\n" +
            "}";
    String user1new = "{\n" +
            "  \"id\":\"1\",\n" +
            "  \"login\": \"dolore\",\n" +
            "  \"name\": \"Nick Name\",\n" +
            "  \"email\": \"mail@mail.ru\",\n" +
            "  \"birthday\": \"1946-08-20\"\n" +
            "}";
    String user2 = "{\n" +
            "  \"login\": \"dolores\",\n" +
            "  \"name\": \"Nick Names\",\n" +
            "  \"email\": \"mails@mail.ru\",\n" +
            "  \"birthday\": \"1956-08-20\"\n" +
            "}";
    String user3 = "{\n" +
            "  \"login\": \"dolsore\",\n" +
            "  \"name\": \"Nicks Name\",\n" +
            "  \"email\": \"mail@mails.ru\",\n" +
            "  \"birthday\": \"1945-08-20\"\n" +
            "}";
    Long id1;
    Long id2;
    Long id3;

    @BeforeAll
    public void beforeAll() {
        service = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        service.create(gson.fromJson(film1, Film.class));
        service.create(gson.fromJson(film2, Film.class));
        service.create(gson.fromJson(film3, Film.class));
        id1 = service.userStorage.create(gson.fromJson(user1, User.class)).getId();
        id2 = service.userStorage.create(gson.fromJson(user2, User.class)).getId();
        id3 = service.userStorage.create(gson.fromJson(user3, User.class)).getId();
    }

    @Test
    void findAll() {
        assertEquals(service.findAll().size(), 4);
    }

    @Test
    void create() {
        assertEquals(service.create(gson.fromJson(film4, Film.class)), service.getFilm(4L));
    }

    @Test
    void put() {
        assertEquals(service.put(gson.fromJson(film1New, Film.class)), gson.fromJson(film1New, Film.class));
    }

    @Test
    void addLike() {
        Set<Long> id = new HashSet<>();
        id.add(id2);
        assertEquals(service.addLike(3L, id2).getLikes().toString(), id.toString());
        service.deleteLike(3L, id2);
    }

    @Test
    void deleteLike() {
        Set<Long> id = new HashSet<>();
        id.add(id2);
        assertEquals(service.addLike(2L, id2).getLikes().toString(), id.toString());
        assertEquals(service.deleteLike(2L, id2).getLikes().toString(), "[]");
    }

    @Test
    void getPopularFilm() {
        service.addLike(2L, id2);
        service.addLike(2L, id1);
        service.addLike(3L, id1);
        assertEquals(service.getPopularFilm(2).size(), 2);
        assertEquals(service.getPopularFilm(2).stream().findFirst().get(), service.getFilm(2L));
        service.deleteLike(2L, id2);
        service.deleteLike(2L, id1);
        service.deleteLike(3L, id1);
    }

    @Test
    void getFilm() {
        assertEquals(service.findAll().stream().findFirst().get(), service.getFilm(1L));
    }
}
