package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController controller = new UserController();
    User user1 = User.builder()
            .id(1)
            .login("loginTest")
            .email("test@mai.ru")
            .name("nameTest")
            .birthday(LocalDate.of(1010, 10, 10))
            .build();
    User user1Update = User.builder()
            .id(1)
            .login("NewLoginTest")
            .email("NewTest@mai.ru")
            .name("NewNameTest")
            .birthday(LocalDate.of(2010, 10, 10))
            .build();
    User userNoName = User.builder()
            .id(2)
            .login("loginTest")
            .email("test@mai.ru")
            .birthday(LocalDate.of(1010, 10, 10))
            .build();
    User userNoNameUpdate = User.builder()
            .id(2)
            .login("loginTestNew")
            .email("test@mai.ruNew")
            .birthday(LocalDate.of(1020, 10, 10))
            .build();
    User userNoLogin = User.builder()
            .email("test@mai.ru")
            .name("nameTest")
            .birthday(LocalDate.of(1010, 10, 10))
            .build();
    User userNoEmail = User.builder()
            .login("loginTest")
            .name("nameTest")
            .birthday(LocalDate.of(1010, 10, 10))
            .build();
    User userNoBirthday = User.builder()
            .login("loginTest")
            .email("test@mai.ru")
            .name("nameTest")
            .build();

    User userIncorrectLogin = User.builder()
            .login("login Test")
            .email("test@mai.ru")
            .name("nameTest")
            .birthday(LocalDate.of(1010, 10, 10))
            .build();
    User userIncorrectEmail = User.builder()
            .login("loginTest")
            .email("testMain.ru")
            .name("nameTest")
            .birthday(LocalDate.of(1010, 10, 10))
            .build();
    User userIncorrectBirthday = User.builder()
            .login("loginTest")
            .email("test@mai.ru")
            .name("nameTest")
            .birthday(LocalDate.of(3010, 10, 10))
            .build();
    User userIncorrect = User.builder()
            .build();


    @Test
    void put() {
        controller.create(user1Update);
        assertEquals(controller.put(user1Update), user1Update);
        assertNotEquals(controller.put(user1Update), user1);
        controller.create(userNoNameUpdate);
        assertEquals(controller.put(userNoNameUpdate).getName(), userNoNameUpdate.getLogin());
        assertNotEquals(controller.put(userNoNameUpdate).getName(), userNoName.getLogin());
        try {
            controller.put(userNoEmail);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "электронная почта не может быть пустой и должна содержать символ @");
        }
        try {
            controller.put(userNoLogin);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "логин не может быть пустым и содержать пробелы.");
        }
        try {
            controller.put(userNoBirthday);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "дата рождения не может быть в будущем или пустой.");
        }
        try {
            controller.put(userIncorrectEmail);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "электронная почта не может быть пустой и должна содержать символ @");
        }
        try {
            controller.put(userIncorrectLogin);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "логин не может быть пустым и содержать пробелы.");
        }
        try {
            controller.put(userIncorrectBirthday);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "дата рождения не может быть в будущем или пустой.");
        }
        try {
            controller.put(userIncorrect);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Все данные пользователя пусты.");
        }
        assertEquals(controller.findAll().size(),2);
    }

    @Test
    void create() {
        assertEquals(controller.create(user1), user1);
        assertEquals(controller.create(userNoName).getName(), userNoName.getLogin());
        try {
            controller.create(userNoEmail);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "электронная почта не может быть пустой и должна содержать символ @");
        }
        try {
            controller.create(userNoLogin);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "логин не может быть пустым и содержать пробелы.");
        }
        try {
            controller.create(userNoBirthday);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "дата рождения не может быть в будущем или пустой.");
        }
        try {
            controller.create(userIncorrectEmail);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "электронная почта не может быть пустой и должна содержать символ @");
        }
        try {
            controller.create(userIncorrectLogin);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "логин не может быть пустым и содержать пробелы.");
        }
        try {
            controller.create(userIncorrectBirthday);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "дата рождения не может быть в будущем или пустой.");
        }
        try {
            controller.create(userIncorrect);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Все данные пользователя пусты.");
        }
        assertEquals(controller.findAll().size(),2);
    }
}