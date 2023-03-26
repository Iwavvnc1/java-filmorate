package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    public static int idUser = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        user = validationUser(user);
        if (user.getId() == 0) {
            user = user.toBuilder().id(idUser++).build();
            while (users.containsKey(user.getId())) {
                user = user.toBuilder().id(idUser++).build();
            }
        }
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id " + user.getId() + " уже зарегистрирован.");
        }
        users.put(user.getId(), user);
        log.debug("Добавлен пользователь с id " + user.getId());
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        user = validationUser(user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id " + user.getId() + " еще не добавлен.");
        }
        users.put(user.getId(), user);
        log.debug("обновлены данные пользователя с id " + user.getId());
        return user;
    }

    private User validationUser(User user) {
        if (user.getId()== 0 && user.getEmail() == null && user.getLogin() == null && user.getBirthday() == null
                && user.getName() == null) {
            log.warn("Все данные пользователя пусты.");
            throw new ValidationException("Все данные пользователя пусты.");
        }
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Не верный email пользователя.");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || checkLogin(user.getLogin())) {
            log.warn("Не верный логин пользователя.");
            throw new ValidationException("логин не может быть пустым и содержать пробелы.");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Не верная дата рождения пользователя.");
            throw new ValidationException("дата рождения не может быть в будущем или пустой.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user = user.toBuilder().name(user.getLogin()).build();
        }
        return user;
    }

    private boolean checkLogin(String str) {
        StringBuilder stringBuilder = new StringBuilder(str);
        for (int i = 0; i < stringBuilder.length(); i++) {
            if (stringBuilder.charAt(i) == ' ') {
                return true;
            }
        }
        return false;
    }
}
