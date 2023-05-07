package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class RatingMpa {
    @Id
    Long id;
    String name;
}
