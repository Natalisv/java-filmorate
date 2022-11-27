package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Set;

@RequestMapping("/users")
@RestController
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //метод добавляет пользователя
    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        userService.addUser(user);
        return user;
    }

    //метод обновляет пользователя
    @PutMapping
    public User updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return user;
    }

    //метод возвращает список пользователей
    @GetMapping
    public List<User> getListUsers() {
        return userService.getListUsers();
    }

    //метод возвращает пользователя по Id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id){
        return userService.getUserById(id);
    }

    //метод удаляет пользователя
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Integer id){
        userService.deleteUserById(id);
    }

    //метод добавляет друга в список друзей по Id
    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId){
        userService.addFriend(id, friendId);
    }

    //метод удаляет друга из списка друзей по Id
    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId){
        userService.deleteFriend(id, friendId);
    }

    //метод возвращает список друзей конкретного пользователя по Id
    @GetMapping("{id}/friends")
    public List<User> getFriends(@PathVariable Integer id){
        return userService.getFriends(id);
    }

    //метод возвращает список общих друзей двух пользователей по их Id
    @GetMapping("{id}/friends/common/{otherId}")
    public Set<User> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId){
        return userService.getMutualFriends(id, otherId);
    }






}
