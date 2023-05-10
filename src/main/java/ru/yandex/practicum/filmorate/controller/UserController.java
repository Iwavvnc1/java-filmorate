package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        return userService.put(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@Valid @RequestBody @PathVariable("id") Long userId,
                          @PathVariable("friendId") Long friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@Valid @RequestBody @PathVariable("id") Long userId,
                             @PathVariable("friendId") Long friendId) {
        return userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@Valid @RequestBody @PathVariable("id") Long userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> commonFriends(@Valid @RequestBody @PathVariable("id") Long userId,
                                          @PathVariable("otherId") Long otherId) {
        return userService.getCommonFriends(userId, otherId);
    }


    @GetMapping("/{id}")
    public User getUser(@Valid @RequestBody @PathVariable("id") Long userId) {
        return userService.getUserById(userId);
    }

}
