package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTests {

    static Film film;
    static User user;

    FilmController filmController = new FilmController(new FilmService(
            new InMemoryFilmStorage(), new InMemoryUserStorage()));

    UserController userController = new UserController(new UserService(new InMemoryUserStorage()));

    @BeforeEach
    public void create() {
        film = new Film(1, "name", "description", "1967-03-25", 100);
        user = new User(1, "name", "mail@mail.ru", "login", "1946-08-20");
    }

    @Test
    public void shouldAddFilm(){
        filmController.addFilm(film);
        assertEquals(filmController.getListFilms().size(), 1, "Film wasn't added");
    }

    @Test
    public void shouldUpdateFilm(){
        filmController.addFilm(film);
        Film updateFilm = new Film(1,"name2", "description2", "1967-03-25", 150);
        filmController.updateFilm(updateFilm);
        assertEquals(updateFilm, filmController.getListFilms().get(0), "Film wasn't updated");
    }

    @Test
    public void shouldGetFilms(){
        filmController.addFilm(film);
        Film secondFilm = new Film(2,"name2", "description2", "1967-03-25", 150);
        filmController.addFilm(secondFilm);
        assertEquals(2, filmController.getListFilms().size(), "Movies aren't comming back");
    }

    @Test
    public void shouldGetFilmById(){
        Integer id = 1;
        filmController.addFilm(film);
        Film expectedFilm = filmController.getFilmById(id);
        assertEquals(film, expectedFilm, "Movies don't equals");

        Integer invalidId = -1;
        final NotFoundException notFoundException = assertThrows(
                NotFoundException.class,
                () -> filmController.getFilmById(invalidId)
        );
        assertEquals(notFoundException.getMessage(), String.format("Film with Id = %s doesn't exist",invalidId));
    }

    @Test
    public void shouldDeleteFilmById(){
        Integer id = 1;
        filmController.addFilm(film);
        filmController.deleteFilmById(id);

        assertEquals(filmController.getListFilms().size(), 0 ,"Film wasn't deleted");

        Integer invalidId = -1;
        final NotFoundException notFoundException = assertThrows(
                NotFoundException.class,
                () -> filmController.deleteFilmById(invalidId)
        );
        assertEquals(notFoundException.getMessage(), String.format("Film with Id = %s doesn't exist",invalidId));
    }
}
