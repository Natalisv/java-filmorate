package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final LocalDate dateNow = LocalDate.now();

    @Autowired
    public UserService(UserStorage userStorageImpl) {
        this.userStorage = userStorageImpl;
    }

    public User addUser(User user){
        if (this.isValid(user)) {
            return userStorage.addUser(user);
        } else {
            log.error("User doesn't pass validation");
            throw new ValidationException("Validation error");
        }
    }


    public User updateUser(User user){
        if (this.isValid(user)) {
            return userStorage.updateUser(user);
        } else {
            log.error("User doesn't pass validation");
            throw new ValidationException("Validation error");
        }
    }

    public List<User> findUsers() {
        return userStorage.findAllUsers();
    }

    public User findUserById(Long id){
        return userStorage.findUserById(id);
    }

    public void addFriendship(Long user1Id, Long user2Id){
        if(isContains(user1Id)) {
            if(isContains(user2Id)) {
                userStorage.addFriendship(user1Id, user2Id);
            } else {
                log.error(String.format("User with Id = %s doesn't exist",user2Id));
                throw new NotFoundException(String.format("User with Id = %s doesn't exist",user2Id));
            }
        } else {
            log.error(String.format("User with Id = %s doesn't exist",user1Id));
            throw new NotFoundException(String.format("User with Id = %s doesn't exist",user1Id));
        }
    }

    private boolean isValid(User user) {
        return (!user.getEmail().isEmpty() && user.getEmail().contains("@")) && (!user.getLogin().isEmpty() && !user.getLogin().contains(" "))
                && LocalDate.parse(user.getBirthday(), formatter).isBefore(dateNow);
    }

    public List<User> getFriends(Long userId){
        if(isContains(userId)) {
            List<User> friends = userStorage.getFriends(userId);
            log.debug("Number of friends: " + friends.size());
            return friends;
        } else{
            log.error(String.format("User with Id = %s doesn't exist",userId));
            throw new NotFoundException(String.format("User with Id = %s doesn't exist",userId));
        }
    }

    public void deleteFriend(Long user1Id, Long user2Id){
        if(isContains(user1Id)) {
            if(isContains(user2Id)) {
                userStorage.deleteFriend(user1Id, user2Id);
            } else {
                log.error(String.format("User with Id = %s doesn't exist",user2Id));
                throw new NotFoundException(String.format("User with Id = %s doesn't exist",user2Id));
            }
        } else {
            log.error(String.format("User with Id = %s doesn't exist",user1Id));
            throw new NotFoundException(String.format("User with Id = %s doesn't exist",user1Id));
        }
    }

    public List<User> getCommonFriends(Long user1Id, Long user2Id){
        if(isContains(user1Id)) {
            if(isContains(user2Id)) {
                List<User> mutualFriends = userStorage.getCommonFriends(user1Id,user2Id);
                log.debug("Number of mutualFriends {}", mutualFriends.size());
                return mutualFriends;
            } else {
                log.error(String.format("User with Id = %s doesn't exist",user2Id));
                throw new NotFoundException(String.format("User with Id = %s doesn't exist",user2Id));
            }
        } else {
            log.error(String.format("User with Id = %s doesn't exist",user1Id));
            throw new NotFoundException(String.format("User with Id = %s doesn't exist",user1Id));
        }
    }

    public void removeUser(Long id){
        userStorage.removeUser(id);
    }

    private boolean isContains(Long userId){
        return userId != null && userId > 0  && userStorage.findUserById(userId) != null;
    }
}


