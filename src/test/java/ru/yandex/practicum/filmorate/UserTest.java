package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createIncorrectEmail() {
        User user = User.builder()
                .id(10L)
                .email("examplemail.com")
                .login("exampleLogin")
                .name("exampleName")
                .birthday(LocalDate.now().minusYears(35))
                .build();
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void createIncorrectLogin() {
        User user = User.builder()
                .id(10L)
                .email("example@mail.com")
                .login(" ")
                .name("exampleName")
                .birthday(LocalDate.now().minusYears(35))
                .build();
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void createIncorrectBirthUser() {
        User user = User.builder()
                .id(10L)
                .email("example@mail.com")
                .login("exampleLogin")
                .name("exampleName")
                .birthday(LocalDate.now().plusYears(35))
                .build();
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void updateIncorrectEmail() {
        User usr = User.builder()
                .id(10L)
                .email("example@mail.com")
                .login("exampleLogin")
                .name("exampleName")
                .birthday(LocalDate.now().minusYears(35))
                .build();
        ResponseEntity<User> response = restTemplate.postForEntity("/users", usr, User.class);

        User user2 = User.builder()
                .id(10L)
                .login("exampleLogin")
                .name("exampleName")
                .birthday(LocalDate.now().minusYears(35))
                .build();
        HttpEntity<User> entity = new HttpEntity<>(user2);
        ResponseEntity<User> response2 = restTemplate.exchange("/users", HttpMethod.PUT, entity, User.class);

        assertEquals("400 BAD_REQUEST", response2.getStatusCode().toString());
    }

    @Test
    void updateIncorrectLogin() {
        User usr = User.builder()
                .id(10L)
                .email("example@mail.com")
                .login("exampleLogin")
                .name("exampleName")
                .birthday(LocalDate.now().minusYears(35))
                .build();
        ResponseEntity<User> response = restTemplate.postForEntity("/users", usr, User.class);
        User user = User.builder()
                .id(10L)
                .email("example@mail.com")
                .login(" ")
                .name("exampleName")
                .birthday(LocalDate.now().minusYears(35))
                .build();
        HttpEntity<User> entity = new HttpEntity<>(user);
        ResponseEntity<User> response2 = restTemplate.exchange("/users", HttpMethod.PUT, entity, User.class);

        assertEquals("400 BAD_REQUEST", response2.getStatusCode().toString());
    }

    @Test
    void updateIncorrectBirthUser() {
        User usr = User.builder()
                .id(10L)
                .email("example@mail.com")
                .login("exampleLogin")
                .name("exampleName")
                .birthday(LocalDate.now().minusYears(35))
                .build();
        ResponseEntity<User> response = restTemplate.postForEntity("/users", usr, User.class);
        User user = User.builder()
                .id(10L)
                .email("example@mail.com")
                .login("exampleLogin")
                .name("exampleName")
                .birthday(LocalDate.now().plusYears(35))
                .build();
        HttpEntity<User> entity = new HttpEntity<>(user);
        ResponseEntity<User> response2 = restTemplate.exchange("/users", HttpMethod.PUT, entity, User.class);

        assertEquals("400 BAD_REQUEST", response2.getStatusCode().toString());
    }
}
