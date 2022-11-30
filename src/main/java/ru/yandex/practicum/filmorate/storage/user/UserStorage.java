package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    User findUserById(Long Id);

    List<User> findAllUsers();

    void addFriendship(Long user1Id, Long user2Id);

    void deleteFriend(Long user1Id, Long user2Id);

    List<User> getFriends(Long Id);

    List<User> getCommonFriends(Long user1Id, Long user2Id);

    void removeUser(Long id);

}
