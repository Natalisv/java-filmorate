package ru.yandex.practicum.filmorate.controller;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistsException;
import ru.yandex.practicum.filmorate.exception.IdExistsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/users")
@RestController
public class UserController {

    private Integer generateId = 1;
    private final Map<Integer, User> users = new HashMap<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final LocalDate dateNow = LocalDate.now();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws Exception {
            generatedId(user);
            if(users.containsKey(user.getId())){
                log.error("User with id= " + user.getId()+ " already in exists");
                throw new IdExistsException("User with id= " + user.getId()+ " already in exists");
            }
            if(isContains(user)){
                throw new ExistsException("User already exists");
            }
            if (isValid(user)) {
                    if (user.getName() == null) {
                        user.setName(user.getLogin());
                    }
                    users.put(user.getId(), user);
                    log.debug("Add user: {}", user);
                    return user;
            } else {
                log.error("User doesn't pass validation");
                throw new ValidationException("Validation error");
            }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
            generatedId(user);
            if (isValid(user)) {
                if (user.getName() == null) {
                    user.setName(user.getLogin());
                }
                users.put(user.getId(), user);
                log.debug("Update user: {}", user);
                return user;
            } else {
                log.error("User doesn't pass validation");
                throw new ValidationException("Validation error");
            }
    }

    @GetMapping
    public List<User> getUsers() {
        log.debug("Number of users: "+ users.size());
        return new ArrayList<>(users.values());
    }

    public boolean isValid(User user) {
        return (!user.getEmail().isEmpty() && user.getEmail().contains("@")) && (!user.getLogin().isEmpty() && !user.getLogin().contains(" "))
                && LocalDate.parse(user.getBirthday(), formatter).isBefore(dateNow) && user.getId() > 0;
    }

    public boolean isContains(User user){
        boolean isContains = false;
        for(User savedUser :  users.values()) {
            if(savedUser.equals(user)){
                isContains = true;
                break;
            }
        }
        return isContains;
    }

    public void generatedId(User user) {
        if (user.getId() == null){
            user.setId(generateId++);
        }
    }
}
