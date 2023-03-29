package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class User {
    int id;
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
}
