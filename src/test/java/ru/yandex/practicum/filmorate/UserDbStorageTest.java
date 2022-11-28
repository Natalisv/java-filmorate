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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.DAO.UserDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserDbStorageTest {

    JdbcTemplate jdbcTemplate;
    UserDbStorage userDbStorage;

    @AfterEach
    public void updateDB(){
        jdbcTemplate.update("DELETE FROM FRIENDSHIP");
        jdbcTemplate.update("DELETE FROM USERS");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1");
    }

    private User createUser(){
        return User.builder().id(1L).name("name").email("mail@mail.ru").login("login").birthday("1946-08-20").build();
    }

    @Test
    public void addUserTest(){
        User user = createUser();
        User savedUser = userDbStorage.addUser(user);
        assertEquals(user, savedUser);
    }

    @Test
    public void updateUserTest(){
        User user = createUser();
        User savedUser = userDbStorage.addUser(user);
        user.setName("name1");
        User updatedUser = userDbStorage.updateUser(user);
        assertEquals(user,updatedUser);
        assertNotEquals(savedUser, updatedUser);
    }

    @Test
    public void findUserByIdTest(){
        User user = createUser();
        User savedUser = userDbStorage.addUser(user);
        Long id = savedUser.getId();
        User existUser = userDbStorage.findUserById(id);
        assertEquals(savedUser, existUser);
    }

    @Test
    public void findAllUsersTest(){
        User user = createUser();
        User savedUser = userDbStorage.addUser(user);
        List<User> usersList = userDbStorage.findAllUsers();
        assertEquals(usersList.size(), 1);
        assertEquals(savedUser, usersList.get(0));
    }

    @Test
    public void addFriendshipTest(){
        User user1 = createUser();
        User user2 = createUser();
        user2.setId(2L);
        User savedUser1 = userDbStorage.addUser(user1);
        User savedUser2 = userDbStorage.addUser(user2);
        userDbStorage.addFriendship(savedUser1.getId(), savedUser2.getId());

        List<User> friends = userDbStorage.getFriends(savedUser1.getId());
        assertEquals(friends.size(), 1);
        assertEquals(friends.get(0),savedUser2);
    }

    @Test
    public void deleteFriendTest(){
        User user1 = createUser();
        User user2 = createUser();
        user2.setId(2L);
        User savedUser1 = userDbStorage.addUser(user1);
        User savedUser2 = userDbStorage.addUser(user2);
        userDbStorage.addFriendship(savedUser1.getId(), savedUser2.getId());
        userDbStorage.deleteFriend(savedUser1.getId(), savedUser2.getId());

        List<User> friends = userDbStorage.getFriends(savedUser1.getId());
        assertEquals(friends.size(), 0);
    }

    @Test
    public void getCommonFriendsTest(){
        User user1 = createUser();
        User user2 = createUser();
        user2.setId(2L);
        User user3 = createUser();
        user3.setId(3L);
        User savedUser1 = userDbStorage.addUser(user1);
        User savedUser2 = userDbStorage.addUser(user2);
        User savedUser3 = userDbStorage.addUser(user3);
        userDbStorage.addFriendship(savedUser1.getId(), savedUser3.getId());
        userDbStorage.addFriendship(savedUser2.getId(), savedUser3.getId());
        List<User> commonFriend = userDbStorage.getCommonFriends(savedUser1.getId(), savedUser2.getId());

        assertEquals(commonFriend.get(0), savedUser3);
    }

    @Test
    public void removeUserTest(){
        User user = createUser();
        userDbStorage.addUser(user);
        userDbStorage.removeUser(user.getId());
        List<User> usersList = userDbStorage.findAllUsers();
        assertEquals(usersList.size(), 0);
    }
}
