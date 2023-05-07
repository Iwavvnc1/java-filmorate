package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage extends FriendStorage {
    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(long id);
}
