package ru.yandex.practicum.filmorate;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmDbStorageTest {

    JdbcTemplate jdbcTemplate;
    FilmDbStorage filmDbStorage;
    UserDbStorage userDbStorage;

    @AfterEach
    public void updateDB(){
        jdbcTemplate.update("DELETE FROM LIKES;");
        jdbcTemplate.update("DELETE FROM USERS");
        jdbcTemplate.update("DELETE FROM FILMS");
        jdbcTemplate.update("ALTER TABLE FILMS ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1");
    }

    private Film createFilm(){
        return Film.builder().id(1L).name("name").description("description").releaseDate("1967-03-25").duration(100L)
                .rate(5).mpa(new Mpa("G",1L)).likes(0).genres(new ArrayList<>()).build();
    }

    private User createUser(){
        return User.builder().id(1L).name("name").email("mail@mail.ru").login("login").birthday("1946-08-20").build();
    }

    @Test
    public void findFilmByIdTest(){
        Film film = createFilm();
        filmDbStorage.addFilm(film);
        Film savedFilm = filmDbStorage.findFilmById(film.getId());
        assertEquals(film, savedFilm);
    }

    @Test
    public void addFilmTest(){
        Film film = createFilm();
        Film savedFilm = filmDbStorage.addFilm(film);
        assertEquals(film, savedFilm);
    }

    // не понимаю почему, но если запустить все тесты этот тест не проходит, а если запустить только FilmDbStorageTest то проходит
    @Test
    public void updateFilmTest(){
        Film film = createFilm();
        filmDbStorage.addFilm(film);
        film.setName("newName");
        Film updatedFilm = filmDbStorage.updateFilm(film);
        assertEquals(film, updatedFilm);
    }

    @Test
    public void findAllFilmsTest(){
        Film film1 = createFilm();
        filmDbStorage.addFilm(film1);
        Film film2 = createFilm();
        film2.setId(2L);
        filmDbStorage.addFilm(film2);
        List<Film> films = filmDbStorage.findAllFilms();

        assertEquals(films.size(), 2);
        assertEquals(films.get(0),film1);
    }

    @Test
    public void addLikeTest(){
        Film film = createFilm();
        filmDbStorage.addFilm(film);
        User user = createUser();
        userDbStorage.addUser(user);
        filmDbStorage.addLike(film.getId(), user.getId());
        Integer filmLikes = filmDbStorage.getLikes(film.getId());
        assertEquals(filmLikes, 1);
    }

    @Test
    public void removeLikeTest(){
        Film film = createFilm();
        filmDbStorage.addFilm(film);
        User user = createUser();
        userDbStorage.addUser(user);
        filmDbStorage.addLike(film.getId(), user.getId());
        filmDbStorage.removeLike(film.getId(), user.getId());
        Integer filmLikes = filmDbStorage.getLikes(film.getId());
        assertEquals(filmLikes, 0);
    }
}
