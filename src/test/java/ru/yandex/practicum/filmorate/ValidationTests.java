package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTests {

    FilmController filmController = new FilmController();
    UserController userController = new UserController();
    User notValidUser = new User(1,"name", "mail@mail.ru", "login", "1946-08-20");

    @Test
    public void validationFilmTests() {
        Film notValidName = new Film(1,"", "description", "1995-10-28", 100);
        Film notValidDate = new Film(2,"name", "description", "1895-10-28", 100);
        Film notValidDuration = new Film(1,"name", "description", "1895-10-28", -10);

        final ValidationException exceptionInvalidName = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(notValidName)
        );
        assertEquals("Validation error", exceptionInvalidName.getMessage());

        final ValidationException exceptionInvalidDate = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(notValidDate)
        );
        assertEquals("Validation error", exceptionInvalidDate.getMessage());

        final ValidationException exceptionInvalidDuration = assertThrows(
                ValidationException.class,
                () -> filmController.updateFilm(notValidDuration)
        );
        assertEquals("Validation error", exceptionInvalidDuration.getMessage());
    }

    @Test
    public void validationUserTests() {
        User notValidEmail = new User(1,"name", "mailmail.ru", "login", "1946-08-20");
        User emptyEmail = new User(2,"name", "", "login", "1946-08-20");
        User notValidLogin = new User(3,"name", "mailmail@.ru", "logi n", "1946-08-20");
        User notValidBirthday = new User(1,"name", "mailmail@.ru", "login", "2022-09-30");

        final ValidationException exceptionInvalidEmail = assertThrows(
                ValidationException.class,
                () -> userController.addUser(notValidEmail)
        );
        assertEquals("Validation error", exceptionInvalidEmail.getMessage());

        final ValidationException exceptionEmptyEmail = assertThrows(
                ValidationException.class,
                () -> userController.addUser(emptyEmail)
        );
        assertEquals("Validation error", exceptionEmptyEmail.getMessage());

        final ValidationException exceptionInvalidLogin = assertThrows(
                ValidationException.class,
                () -> userController.addUser(notValidLogin)
        );
        assertEquals("Validation error", exceptionInvalidLogin.getMessage());

        final ValidationException exceptionInvalidBirthday = assertThrows(
                ValidationException.class,
                () -> userController.updateUser(notValidBirthday)
        );
        assertEquals("Validation error", exceptionInvalidBirthday.getMessage());

    }
}
