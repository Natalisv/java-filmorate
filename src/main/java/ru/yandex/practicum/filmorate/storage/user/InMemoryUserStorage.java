package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExistsException;
import ru.yandex.practicum.filmorate.exception.IdExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    private Integer generateId = 1;
    private final Map<Integer, User> users = new HashMap<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final LocalDate dateNow = LocalDate.now();

    @Override
    public User addUser(User user){
        if (isValid(user)) {
            generatedId(user);
            if (users.containsKey(user.getId())) {
                log.error("User with id= " + user.getId() + " already in exists");
                throw new IdExistsException("User with Id= " + user.getId() + " already in exists");
            }
            if (isContains(user)) {
                throw new ExistsException("User already exists");
            }
            if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
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

    @Override
    public User updateUser(User user){
        if(users.containsKey(user.getId())) {
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
        } else {
            throw new NotFoundException(String.format("User with Id = %s doesn't exist", user.getId()));
        }
    }

    @Override
    public List<User> getListUsers(){
        log.debug("Number of users: "+ users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUser(Integer id){
        if(users.containsKey(id)){
            users.remove(id);
            log.debug(String.format("User with Id = %s was deleted",id));
        } else {
            throw new NotFoundException(String.format("User with Id = %s doesn't exist", id));
        }
    }

    @Override
    public Map<Integer, User> getUsers(){
        return users;
    }

    public User getUser(Integer id){
        if(users.containsKey(id)){
            return users.get(id);
        } else{
            throw new NotFoundException(String.format("User with Id = %s doesn't exist",id));
        }
    }

    public boolean isValid(User user) {
        return (!user.getEmail().isEmpty() && user.getEmail().contains("@")) && (!user.getLogin().isEmpty() && !user.getLogin().contains(" "))
                && LocalDate.parse(user.getBirthday(), formatter).isBefore(dateNow) ;
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
