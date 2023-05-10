package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.getAllUsers();
    }

    public User create(User user) {
        return userStorage.createUser(user);
    }

    public User put(User user) {
        checkValidUser(user.getId());
        return userStorage.updateUser(user);
    }

    public User addFriend(Long userId, Long friendId) {
        if (!userId.equals(friendId)) {
            checkValidUser(userId);
            checkValidUser(friendId);
            return userStorage.addFriend(userId, friendId);
        } else {
            log.warn("Невозможно добавить себя к себе в друзья" + "User id = " + userId);
            throw new IncorrectParameterException("Невозможно добавить себя к себе в друзья");
        }
    }

    public User deleteFriend(Long userId, Long friendId) {
        if (!userId.equals(friendId)) {
            checkValidUser(userId);
            checkValidUser(friendId);
            return userStorage.removeFriend(userId, friendId);
        } else {
            log.warn("Невозможно удалить себя из своего списка друзья" + "User id = " + userId);
            throw new IncorrectParameterException("Невозможно удалить себя из своего списка друзья");
        }
    }

    public List<User> getFriends(Long userId) {
        checkValidUser(userId);
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        if (!userId.equals(otherId)) {
            return userStorage.getCommonFriends(userId, otherId);
        } else {
            log.warn("Невозможно отобразить список общих друзей у пользователя с Id "
                    + userId + " и другого пользователя с Id " + otherId);
            throw new IncorrectParameterException("Невозможно отобразить список общих друзей у пользователя с Id "
                    + userId + " и другого пользователя с Id " + otherId);
        }
    }

    public User getUserById(Long userId) {
        return userStorage.getUserById(userId);
    }

    private void checkValidUser(Long userId) {
        userStorage.getUserById(userId);
    }
}
