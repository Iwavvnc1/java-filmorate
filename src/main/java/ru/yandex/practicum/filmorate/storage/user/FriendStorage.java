package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    User addFriend(Long userID, Long friendId);

    User removeFriend(Long userID, Long friendId);

    List<User> getFriends(Long userId);

    List<User> getCommonFriends(Long friend1, Long friend2);
}
