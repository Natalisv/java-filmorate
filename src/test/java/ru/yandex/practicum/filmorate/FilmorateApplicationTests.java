package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FilmorateApplication.class)
class FilmorateApplicationTests {

	FilmController filmController = new FilmController();
	UserController userController = new UserController();
	Film film = new Film(1,"name", "description", "1967-03-25", 100);
	User user = new User(1,"name", "mail@mail.ru", "login", "1946-08-20");


	@Test
	void contextLoads() {
	}

	@Test
	public void shouldAddFilm() throws Exception {
		filmController.addFilm(film);
		assertNotNull(filmController.getFilms(), "Film wasn't added");
	}

	@Test
	public void shouldUpdateFilm() throws Exception {
		filmController.addFilm(film);
		Film updateFilm = new Film(1,"name2", "description2", "1967-03-25", 150);
		filmController.updateFilm(updateFilm);
		assertEquals(updateFilm, filmController.getFilms().get(0), "Film wasn't update");
	}

	@Test
	public void shouldGetFilms() throws Exception {
		filmController.addFilm(film);
		Film secondFilm = new Film(2,"name2", "description2", "1967-03-25", 150);
		filmController.addFilm(secondFilm);
		assertEquals(2, filmController.getFilms().size(), "Movies aren't comming back");
	}

	@Test
	public void shouldAddUser() throws Exception {
		userController.addUser(user);
		assertNotNull(userController.getUsers(),"User wasn't added" );
	}

	@Test
	public void shouldUpdateUser() throws Exception {
		userController.addUser(user);
		User secondUser = new User(1,"name1", "mail@mail.ru", "login2", "1946-08-25");
		userController.updateUser(secondUser);
		assertEquals(secondUser, userController.getUsers().get(0));
	}

	@Test
	public void shouldGetUsers() throws Exception {
		userController.addUser(user);
		User secondUser = new User(2,"name1", "mail@mail.ru", "login2", "1946-08-25");
		userController.addUser(secondUser);
		assertEquals(2, userController.getUsers().size());
	}

}
