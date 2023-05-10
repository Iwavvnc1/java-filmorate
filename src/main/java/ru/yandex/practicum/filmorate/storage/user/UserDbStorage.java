package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User createUser(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", user.getEmail());
        parameters.put("login", user.getLogin());
        String name;
        if (user.getName() == null || user.getName().equals(" ") || user.getName().equals("")) {
            name = user.getLogin();
            parameters.put("user_name", name);
        } else {
            name = user.getName();
            parameters.put("user_name", name);
        }
        parameters.put("birthday", user.getBirthday());
        Number id = insert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        return User.builder()
                .id(id.longValue())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(name)
                .birthday(user.getBirthday())
                .build();
    }

    public User updateUser(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, USER_NAME = ?, birthday = ? WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM users";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery);
        List<User> users = new ArrayList<>();
        while (srs.next()) {
            users.add(userMap(srs));
        }
        return users;
    }

    public User addFriend(Long userId, Long friendId) {
        if (getFriends(userId).stream().noneMatch(user -> user.getId().equals(friendId))) {
            String sqlQuery = "INSERT INTO friends (user_id, friend_id, status) "
                    + "VALUES(?, ?, ?)";
            jdbcTemplate.update(sqlQuery, userId, friendId, true);
            return getUserById(userId);
        } else {
            throw new IncorrectParameterException("Пользователь с id:" + friendId
                    + "уже добавлен в список друзей пользователя с id:" + userId);
        }
    }

    public User removeFriend(Long userId, Long friendId) {
        if (getFriends(userId).stream().anyMatch(user -> user.getId().equals(friendId))) {
            String sqlQuery = "DELETE FROM friends "
                    + "WHERE user_id = ? AND friend_id = ?";
            User user = getUserById(userId);
            jdbcTemplate.update(sqlQuery, userId, friendId);
            return user;
        } else {
            throw new IncorrectParameterException("Пользователь с id:" + friendId
                    + "не добавлен в список друзей пользователя с id:" + userId);
        }
    }

    public List<User> getFriends(Long userId) {
        List<User> friends = new ArrayList<>();
        String sqlQuery = "SELECT * FROM users "
                + "WHERE users.user_id IN (SELECT friend_id from friends "
                + "WHERE user_id = ?)";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        while (srs.next()) {
            friends.add(userMap(srs));
        }
        return friends;
    }

    public List<User> getCommonFriends(Long user1, Long user2) {
        List<User> commonFriends = new ArrayList<>();
        String sqlQuery = "SELECT * FROM users "
                + "WHERE users.user_id IN (SELECT friend_id from friends "
                + "WHERE user_id IN (?, ?) "
                + "AND friend_id NOT IN (?, ?))";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, user1, user2, user1, user2);
        while (srs.next()) {
            commonFriends.add(userMap(srs));
        }
        return commonFriends;
    }

    public User getUserById(long userId) {
        String sql = "SELECT * FROM users WHERE USER_ID = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sql, userId);
        if (srs.next()) {
            return userMap(srs);
        } else {
            throw new IncorrectParameterException("Ползователь с ID= " + userId + " не найден!");
        }
    }

    private static User userMap(SqlRowSet srs) {
        Long id = srs.getLong("user_id");
        String name = srs.getString("user_name");
        String login = srs.getString("login");
        String email = srs.getString("email");
        LocalDate birthday = Objects.requireNonNull(srs.getTimestamp("birthday"))
                .toLocalDateTime().toLocalDate();
        return User.builder()
                .id(id)
                .name(name)
                .login(login)
                .email(email)
                .birthday(birthday)
                .build();
    }
}
