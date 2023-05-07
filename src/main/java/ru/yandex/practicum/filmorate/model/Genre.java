package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;

@Builder(toBuilder = true)
@Service
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @Id
    Long id;
    @NotBlank
    String name;
}
