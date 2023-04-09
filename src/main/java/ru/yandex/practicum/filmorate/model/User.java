package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Value
@Builder(toBuilder = true)
public class User {
    Set<Long> friends = new HashSet<>();
    Long id;
    @NotBlank
    @Email
    String email;
    @NotBlank
    @Pattern(regexp = "\\S+")
    String login;
    String name;
    @NotNull
    @Past
    LocalDate birthday;

    public Set<Long> getFriends() {
        return friends;
    }
}
