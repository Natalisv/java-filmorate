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

public class UserControllerTests {

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
	public void shouldAddUser(){
		userController.addUser(user);
		assertNotNull(userController.getListUsers(),"User wasn't added" );
	}

	@Test
	public void shouldUpdateUser(){
		userController.addUser(user);
		User secondUser = new User(1,"name1", "mail@mail.ru", "login2", "1946-08-25");
		userController.updateUser(secondUser);
		assertEquals(secondUser, userController.getListUsers().get(0),"User wasn't updated");
	}

	@Test
	public void shouldGetUsers(){
		userController.addUser(user);
		User secondUser = new User(2,"name1", "mail@mail.ru", "login2", "1946-08-25");
		userController.addUser(secondUser);
		assertEquals(2, userController.getListUsers().size(), "Users aren't comming back");
	}

    @Test
    public void shouldGetUserById(){
        userController.addUser(user);
        User secondUser = new User(2,"name1", "mail@mail.ru", "login2", "1946-08-25");
        Integer id = 1;
        User expectedUser = userController.getUserById(id);
        assertEquals(user, expectedUser,"Failed to get user");

        Integer invalidId = -1;
        final NotFoundException notFoundException = assertThrows(
                NotFoundException.class,
                () -> userController.getUserById(invalidId)
        );
        assertEquals(notFoundException.getMessage(), String.format("User with Id = %s doesn't exist",invalidId));
    }

    @Test
    public void shouldDeleteUserById(){
        userController.addUser(user);
        Integer id =1;
        userController.deleteUserById(id);
        assertEquals(userController.getListUsers().size(), 0, "User wasn't deleted");

        Integer invalidId = -1;
        final NotFoundException notFoundException = assertThrows(
                NotFoundException.class,
                () -> userController.deleteUserById(invalidId)
        );
        assertEquals(notFoundException.getMessage(), String.format("User with Id = %s doesn't exist",invalidId));
    }

    @Test
    public void shouldAddFriend(){
        userController.addUser(user);
        User secondUser = new User(2,"name1", "mail@mail.ru", "login2", "1946-08-25");
        userController.addUser(secondUser);

        userController.addFriend(user.getId(), secondUser.getId());
        List<User> list = userController.getFriends(user.getId());
        assertEquals(list.size(),1,"Friend wasn't added");
    }

    @Test
    public void shouldDeleteFriend(){
        userController.addUser(user);
        User secondUser = new User(2,"name1", "mail@mail.ru", "login2", "1946-08-25");
        userController.addUser(secondUser);

        userController.addFriend(user.getId(), secondUser.getId());
        userController.deleteFriend(user.getId(), secondUser.getId());
        List<User> list = userController.getFriends(user.getId());
        assertEquals(list.size(),0,"Friend wasn't deleted");
    }

    @Test
    public void shouldGetFriends(){
        userController.addUser(user);
        User secondUser = new User(2,"name1", "mail@mail.ru", "login2", "1946-08-25");
        userController.addUser(secondUser);
        User nextUser = new User(3,"name1", "mail@mail.ru", "login2", "1946-08-25");
        userController.addUser(nextUser);

        userController.addFriend(user.getId(), secondUser.getId());
        userController.addFriend(user.getId(), nextUser.getId());

        List<User> list = userController.getFriends(user.getId());
        assertEquals(list.size(),2,"Friend wasn't deleted");
        assertEquals(list.get(0), secondUser, "Friend wasn't deleted");
    }

    @Test
    public void shouldgetMutualFriends(){
        userController.addUser(user);
        User secondUser = new User(2,"name1", "mail@mail.ru", "login2", "1946-08-25");
        userController.addUser(secondUser);
        User nextUser = new User(3,"name1", "mail@mail.ru", "login2", "1946-08-25");
        userController.addUser(nextUser);

        userController.addFriend(user.getId(), secondUser.getId());
        userController.addFriend(nextUser.getId(), secondUser.getId());

        Set<User> list = userController.getMutualFriends(user.getId(), nextUser.getId());

        assertEquals(list.size(),1,"MutualFriends failed");
        assertTrue(list.contains(secondUser), "MutualFriends failed");
    }
}
