package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private static Long idUser = 1L;
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user = user.toBuilder().name(user.getLogin()).build();
        }
        if (user.getId() == null || user.getId() == 0) {
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

    public User put(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user = user.toBuilder().name(user.getLogin()).build();
        }
        if (!users.containsKey(user.getId())) {
            throw new IncorrectParameterException("Пользователь с id " + user.getId() + " еще не добавлен.");
        }
        users.put(user.getId(), user);
        log.debug("обновлены данные пользователя с id " + user.getId());
        return user;
    }
}
