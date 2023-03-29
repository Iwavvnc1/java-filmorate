package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
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
    public User create(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user = user.toBuilder().name(user.getLogin()).build();
        }
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
    public User put(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user = user.toBuilder().name(user.getLogin()).build();
        }
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id " + user.getId() + " еще не добавлен.");
        }
        users.put(user.getId(), user);
        log.debug("обновлены данные пользователя с id " + user.getId());
        return user;
    }
}
