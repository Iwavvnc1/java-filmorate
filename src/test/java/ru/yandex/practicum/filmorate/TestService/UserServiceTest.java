package ru.yandex.practicum.filmorate.TestService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.yandex.practicum.filmorate.adapter.LocalDateAdapter;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {


    UserService service;
    String user1 = "{\n" +
            "  \"login\": \"dolore\",\n" +
            "  \"name\": \"Nick Name\",\n" +
            "  \"email\": \"mail@mail.ru\",\n" +
            "  \"birthday\": \"1946-08-20\"\n" +
            "}";
    String user1new;
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
    String user4 = "{\n" +
            "  \"login\": \"dolsores\",\n" +
            "  \"name\": \"Nicks Names\",\n" +
            "  \"email\": \"mails@mails.ru\",\n" +
            "  \"birthday\": \"1945-08-22\"\n" +
            "}";
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();
    Long id1;
    Long id2;
    Long id3;
    Long id4;

    @BeforeAll
    public void beforeAll() {
        service = new UserService(new InMemoryUserStorage());
        id1 = service.create(gson.fromJson(user1, User.class)).getId();
        id2 = service.create(gson.fromJson(user2, User.class)).getId();
        id3 = service.create(gson.fromJson(user3, User.class)).getId();
        user1new = "{\n" +
                "  \"id\":\"" + id1 + "\",\n" +
                "  \"login\": \"dolore\",\n" +
                "  \"name\": \"Nick Name\",\n" +
                "  \"email\": \"mail@mail.ru\",\n" +
                "  \"birthday\": \"1946-08-20\"\n" +
                "}";
    }

    @Test
    void create() {
        id4 = service.create(gson.fromJson(user4, User.class)).getId();
        assertNotNull(service.getUser(id4));
    }

    @Test
    void findAll() {
        assertEquals(service.findAll().size(), 4);
    }

    @Test
    void put() {
        assertEquals(service.put(gson.fromJson(user1new, User.class)), gson.fromJson(user1new, User.class));
    }

    @Test
    void addFriend() {
        assertEquals(service.addFriend(1L, 2L).getFriends().toString(), "[2]");
    }

    @Test
    void deleteFriend() {
        assertEquals(service.addFriend(1L, 2L).getFriends().toString(), "[2]");
        assertEquals(service.deleteFriend(1L, 2L).getFriends().toString(), "[]");
    }

    @Test
    void getFriends() {
        if (service.getFriends(3L) == null || service.getFriends(3L).isEmpty()) {
            service.addFriend(3L, 2L);
        }
        assertEquals(service.getUser(2L), service.getFriends(3L).stream().findFirst().get());
    }

    @Test
    void getCommonFriends() {
        service.addFriend(1L, 2L);
        service.addFriend(3L
                , 2L);
        assertEquals(service.getUser(2L), service.getCommonFriends(1L
                , 3L).stream().findFirst().get());
    }

    @Test
    void getUser() {
        assertEquals(User.class, service.getUser(1L).getClass());
    }
}