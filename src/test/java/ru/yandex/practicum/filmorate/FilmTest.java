package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createIncorrectName() {
        Film film = Film.builder()
                .name(null)
                .description("exampleDescription")
                .releaseDate(LocalDate.now().minusYears(10))
                .duration(-180)
                .build();
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void createIncorrectDescription() {
        Film film = Film.builder()
                .name("exampleName")
                .description("Пятеро друзей ( комик-группа «Шарло»), " +
                        "приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, " +
                        "который задолжал им деньги, а именно 20 миллионов. о Куглов, " +
                        "который за время «своего отсутствия», стал кандидатом Коломбани.")
                .releaseDate(LocalDate.now().minusYears(10))
                .duration(120)
                .build();
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void createIncorrectDuration() {
        Film film = Film.builder()
                .name("exampleName")
                .description("exampleDescription")
                .releaseDate(LocalDate.now().minusYears(10))
                .duration(-120)
                .build();
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void updateIncorrectName() {
        Film film = Film.builder()
                .name("exampleName")
                .description("exampleDescription")
                .releaseDate(LocalDate.now().minusYears(10))
                .duration(120)
                .build();
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
        Film film2 = Film.builder()
                .name(null)
                .description("exampleDescription")
                .releaseDate(LocalDate.now().minusYears(10))
                .duration(120)
                .build();
        HttpEntity<Film> entity = new HttpEntity<>(film2);
        ResponseEntity<Film> response2 = restTemplate.exchange("/films", HttpMethod.PUT, entity, Film.class);

        assertEquals("400 BAD_REQUEST", response2.getStatusCode().toString());

        System.out.println(response2.getBody());
        System.out.println("hello");
    }

    @Test
    void updateIncorrectDescription() {
        Film film = Film.builder()
                .name("exampleName")
                .description("exampleDescription")
                .releaseDate(LocalDate.now().minusYears(10))
                .duration(120)
                .build();
        restTemplate.postForLocation("/films", film);
        Film film2 = Film.builder()
                .name("exampleName")
                .description("Пятеро друзей ( комик-группа «Шарло»), " +
                        "приезжают в город Бризуль. Здесь они хотят разыскать господина Огюста Куглова, " +
                        "который задолжал им деньги, а именно 20 миллионов. о Куглов, " +
                        "который за время «своего отсутствия», стал кандидатом Коломбани.")
                .releaseDate(LocalDate.now().minusYears(10))
                .duration(120)
                .build();
        HttpEntity<Film> entity = new HttpEntity<>(film2);
        ResponseEntity<Film> response2 = restTemplate.exchange("/films", HttpMethod.PUT, entity, Film.class);

        assertEquals("400 BAD_REQUEST", response2.getStatusCode().toString());
    }

    @Test
    void updateIncorrectDuration() {
        Film film = Film.builder()
                .name("exampleName")
                .description("exampleDescription")
                .releaseDate(LocalDate.now().minusYears(10))
                .duration(120)
                .build();
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
        Film film2 = Film.builder()
                .name("exampleName")
                .description("exampleDescription")
                .releaseDate(LocalDate.now().minusYears(10))
                .duration(-120)
                .build();
        HttpEntity<Film> entity = new HttpEntity<>(film2);
        ResponseEntity<Film> response2 = restTemplate.exchange("/films", HttpMethod.PUT, entity, Film.class);

        assertEquals("400 BAD_REQUEST", response2.getStatusCode().toString());
    }
}
