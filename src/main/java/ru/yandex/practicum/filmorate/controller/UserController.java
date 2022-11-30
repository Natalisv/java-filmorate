package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

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
        return userService.addUser(user);
    }

    //метод обновляет пользователя
    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    //метод возвращает список пользователей
    @GetMapping
    public List<User> findUsers() {
        return userService.findUsers();
    }

    //метод возвращает пользователя по Id
    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id){
        return userService.findUserById(id);
    }

    //метод добавляет друга в список друзей по Id
    @PutMapping("{id}/friends/{friendId}")
    public void addFriendship(@PathVariable Long id, @PathVariable Long friendId){
        userService.addFriendship(id, friendId);
    }

    //метод удаляет друга из списка друзей по Id
    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId){
        userService.deleteFriend(id, friendId);
    }

    //метод возвращает список друзей конкретного пользователя по Id
    @GetMapping("{id}/friends")
    public List<User> getFriends(@PathVariable Long id){
        return userService.getFriends(id);
    }

    //метод возвращает список общих друзей двух пользователей по их Id
    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Long id, @PathVariable Long otherId){
        return userService.getCommonFriends(id, otherId);
    }


}
