package ru.yandex.practicum.filmorate.model;


import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Value;
import ru.yandex.practicum.filmorate.customValidator.ReleaseDateConstraint;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Value
@Builder(toBuilder = true)
public class Film {
    @NotNull
    Set<Long> likes = new HashSet<>();
    Long id;
    @NotBlank
    @Valid
    String name;
    @NotBlank
    @Size(max = 200)
    String description;
    @ReleaseDateConstraint
    LocalDate releaseDate;
    @Min(1)
    @NotNull
    int duration;

    public Set<Long> getLikes() {
        return likes;
    }
}
