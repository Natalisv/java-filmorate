package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    List<User> getListUsers();

    void deleteUser(Integer id);
    User getUser(Integer id);
    Map<Integer, User> getUsers();

}
