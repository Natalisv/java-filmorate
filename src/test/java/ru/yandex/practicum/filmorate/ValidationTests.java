package ru.yandex.practicum.filmorate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTests {
    static Film film;
    static Film notValidName;
    static Film nullName;

    static Film notValidDate;
    static Film notValidDuration;
    static User user;
    static User notValidEmail;
    static User emptyEmail;
    static User notValidLogin;
    static User nullLogin;
    static User notValidBirthday;
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    FilmController filmController = new FilmController(new FilmService(
            new InMemoryFilmStorage(), new InMemoryUserStorage()));
    UserController userController = new UserController(new UserService(new InMemoryUserStorage()));

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @BeforeAll
    public static void createFilms(){
        film = new Film(1,"name", "description", "1967-03-25", 100);
        notValidName = new Film(1,"", "description", "1995-10-28", 100);
        nullName = new Film(1, "description", "1995-10-28", 100);
        notValidDate = new Film(2,"name", "description", "1895-10-28", 100);
        notValidDuration = new Film(4,"name", "description", "1895-10-28", -10);
    }

    @BeforeAll
    public static void createUsers(){
        user = new User(1,"name", "mail@mail.ru", "login", "1946-08-20");
        notValidEmail = new User(1,"name", "mailmail.ru", "login", "1946-08-20");
        emptyEmail = new User(2,"name", "", "login", "1946-08-20");
        notValidLogin = new User(3,"name", "mailmail@.ru", "logi n", "1946-08-20");
        nullLogin = new User(1,"name", "mail@mail.ru", "1946-08-20");
        notValidBirthday = new User(5,"name", "mailmail@.ru", "login", "2022-12-30");
    }

    @AfterAll
    public static void close(){
        validatorFactory.close();
    }


    @Test
    public void shouldHaveNoViolations() {

        Set<ConstraintViolation<Film>> violationsFilm
                = validator.validate(film);
        assertTrue(violationsFilm.isEmpty());

        Set<ConstraintViolation<User>> violationsUser
                = validator.validate(user);
        assertTrue(violationsUser.isEmpty());

    }
    @Test
    void shouldDetectIsBlankName(){
        Set<ConstraintViolation<Film>> violationsFilm
                = validator.validate(notValidName);
        assertEquals(violationsFilm.size(),1);

        ConstraintViolation<Film> violation
                = violationsFilm.iterator().next();

        assertEquals("must not be blank", violation.getMessage());
        assertEquals("name", violation.getPropertyPath().toString());
        assertEquals("", violation.getInvalidValue());
    }

    @Test
    void shouldDetectNullName(){
        Set<ConstraintViolation<Film>> violationsFilm
                = validator.validate(nullName);
        assertEquals(violationsFilm.size(),2);

        ConstraintViolation<Film> violation
                = violationsFilm.iterator().next();

        assertEquals("name", violation.getPropertyPath().toString());
        assertNull(violation.getInvalidValue());
    }

    @Test
    void shouldBePositiveDuration(){
        Set<ConstraintViolation<Film>> violationsFilm
                = validator.validate(notValidDuration);
        assertEquals(violationsFilm.size(),1);

        ConstraintViolation<Film> violation
                = violationsFilm.iterator().next();
        assertEquals("must be greater than 0", violation.getMessage());
        assertEquals(-10L,violation.getInvalidValue());
    }

    @Test
    void shouldDetectedInvalidEmail(){
        Set<ConstraintViolation<User>> violationsUser
                = validator.validate(notValidEmail);
        assertEquals(violationsUser.size(),1);

        ConstraintViolation<User> violation
                = violationsUser.iterator().next();

        assertEquals("must be a well-formed email address", violation.getMessage());
        assertEquals("mailmail.ru",violation.getInvalidValue());
    }

    @Test
    void shouldDetectedNullLogin(){
        Set<ConstraintViolation<User>> violationsUser
                = validator.validate(nullLogin);
        assertEquals(violationsUser.size(),1);

        ConstraintViolation<User> violation
                = violationsUser.iterator().next();

        assertEquals("must not be null", violation.getMessage());
        assertNull(violation.getInvalidValue());
    }


    @Test
    public void validationFilmTests() {
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
                () -> filmController.addFilm(notValidDuration)
        );
        assertEquals("Validation error", exceptionInvalidDuration.getMessage());
    }

    @Test
    public void validationUserTests() {
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
                () -> userController.addUser(notValidBirthday)
        );
        assertEquals("Validation error", exceptionInvalidBirthday.getMessage());
    }
}
