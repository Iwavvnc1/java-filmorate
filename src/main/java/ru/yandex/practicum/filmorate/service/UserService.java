package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User put(User user) {
        return userStorage.put(user);
    }

    public User addFriend(Long userId, Long friendId) {
        if (!userId.equals(friendId)) {
            if (userStorage.findAll().stream().anyMatch(users -> users.getId().equals(userId))) {
                if (userStorage.findAll().stream().anyMatch(friends -> friends.getId().equals(friendId))) {
                    User user = userStorage.findAll().stream()
                            .filter(users -> users.getId().equals(userId))
                            .findFirst().get();
                    if (user.getFriends().stream().noneMatch(usersId -> usersId.equals(userId))) {
                        user.getFriends().add(friendId);
                        User friend = userStorage.findAll().stream()
                                .filter(users -> users.getId().equals(friendId))
                                .findFirst().get();
                        friend.getFriends().add(user.getId());
                        return user;
                    } else {
                        throw new IncorrectParameterException("Пользователь с id:" + friendId
                                + "уже добавлен в список друзей пользователя с id:" + userId);
                    }
                } else {
                    throw new IncorrectParameterException("Пользователь с id:" + friendId + " не найден");
                }
            } else {
                throw new IncorrectParameterException("Пользователь с id:" + userId + " не найден");
            }
        } else {
            throw new IncorrectParameterException("Невозможно добавить себя к себе в друзья");
        }
    }

    public User deleteFriend(Long userId, Long friendId) {
        if (!userId.equals(friendId)) {
            if (userStorage.findAll().stream().anyMatch(users -> users.getId().equals(userId))) {
                if (userStorage.findAll().stream().anyMatch(users -> users.getId().equals(friendId))) {
                    User user = userStorage.findAll().stream()
                            .filter(users -> users.getId().equals(userId))
                            .findFirst().get();
                    if (user.getFriends().stream().anyMatch(usersId -> usersId.equals(friendId))) {
                        user.getFriends().remove(friendId);
                        User friend = userStorage.findAll().stream()
                                .filter(users -> users.getId().equals(friendId))
                                .findFirst().get();
                        friend.getFriends().remove(user.getId());
                        return user;
                    } else {
                        throw new IncorrectParameterException("Пользователь с id:" + friendId
                                + "не добавлен в список друзей пользователя с id:" + userId);
                    }
                } else {
                    throw new IncorrectParameterException("Пользователь с id:" + friendId + " не найден");
                }
            } else {
                throw new IncorrectParameterException("Пользователь с id:" + userId + " не найден");
            }
        } else {
            throw new IncorrectParameterException("Невозможно удалить себя из своего списка друзья");
        }
    }

    public Collection<User> getFriends(Long userId) {
        if (userStorage.findAll().stream().anyMatch(users -> users.getId().equals(userId))) {
            User user = userStorage.findAll().stream()
                    .filter(users -> users.getId().equals(userId))
                    .findFirst().get();
            Set<User> friends = new HashSet<>();
            user.getFriends().forEach(usersId -> {
                if (!user.getFriends().isEmpty()) {
                    User friend = userStorage.findAll().stream()
                            .filter(users -> users.getId().equals(usersId))
                            .findFirst().get();
                    friends.add(friend);
                }
            });
            return friends;
        } else {
            throw new IncorrectParameterException("Пользователь с id:" + userId + " не найден");
        }
    }

    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        if (!userId.equals(otherId)) {
            if (userStorage.findAll().stream().anyMatch(users -> users.getId().equals(userId))) {
                if (userStorage.findAll().stream().anyMatch(friends -> friends.getId().equals(otherId))) {
                    User user = userStorage.findAll().stream()
                            .filter(users -> users.getId().equals(userId))
                            .findFirst().get();
                    User other = userStorage.findAll().stream()
                            .filter(users -> users.getId().equals(otherId))
                            .findFirst().get();
                    return getFriends(user.getId()).stream()
                            .filter(getFriends(otherId)::contains)
                            .collect(Collectors.toSet());
                } else {
                    throw new IncorrectParameterException("Пользователь с id:" + otherId + " не найден");
                }
            } else {
                throw new IncorrectParameterException("Пользователь с id:" + userId + " не найден");
            }
        } else {
            throw new IncorrectParameterException("Невозможно добавить себя к себе в друзья");
        }
    }

    public User getUser(Long userId) {
        if (userStorage.findAll().stream().anyMatch(users -> users.getId().equals(userId))) {
            return userStorage.findAll().stream()
                    .filter(users -> users.getId().equals(userId))
                    .findFirst().get();
        } else {
            throw new IncorrectParameterException("Пользователь с id:" + userId + " не найден");
        }
    }
}
