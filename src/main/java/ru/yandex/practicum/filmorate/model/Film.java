package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import ru.yandex.practicum.filmorate.customValidator.ReleaseDateConstraint;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {
    @Id
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
    @Builder.Default
    Set<Genre> genres = new HashSet<>();
    RatingMpa mpa;

    public void addGenre(Genre genre) {
        genres.add(genre);
    }
}
