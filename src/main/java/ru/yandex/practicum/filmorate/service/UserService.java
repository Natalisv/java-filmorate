package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addUser(User user){
        userStorage.addUser(user);
    }

    public void updateUser(User user){
        userStorage.updateUser(user);
    }

    public List<User> getListUsers() {
        return userStorage.getListUsers();
    }

    public User getUserById(Integer id){
        return userStorage.getUser(id);
    }

    public void deleteUserById(Integer id){
        userStorage.deleteUser(id);
    }

    public void addFriend(Integer user1Id, Integer user2Id){
        if(isContains(user1Id)) {
            if(isContains(user2Id)) {
                User user1 = userStorage.getUser(user1Id);
                user1.addFriend(Long.valueOf(user2Id));
                System.out.println(user1.getFriends());

                User user2 = userStorage.getUser(user2Id);
                user2.addFriend(Long.valueOf(user1Id));
                System.out.println(user2.getFriends());
            } else {
                log.error(String.format("User with Id = %s doesn't exist",user2Id));
                throw new NotFoundException(String.format("User with Id = %s doesn't exist",user2Id));
            }
        } else {
            log.error(String.format("User with Id = %s doesn't exist",user1Id));
            throw new NotFoundException(String.format("User with Id = %s doesn't exist",user1Id));
        }
    }

    public List<User> getFriends(Integer userId){
        if(isContains(userId)) {
            List<User> friends = new ArrayList<>();
            User user = userStorage.getUser(userId);
            Set<Long> friendsId= user.getFriends();
            for(Long id : friendsId){
                friends.add(userStorage.getUser(Math.toIntExact(id)));
            }
            log.debug("Number of friends: " + friends.size());
            return friends;
        } else{
            log.error(String.format("User with Id = %s doesn't exist",userId));
            throw new NotFoundException(String.format("User with Id = %s doesn't exist",userId));
        }
    }

    public void deleteFriend(Integer user1Id, Integer user2Id){
        if(isContains(user1Id)) {
            if(isContains(user2Id)) {
                User user1 = userStorage.getUser(user1Id);
                user1.deleteFriend(Long.valueOf(user2Id));

                User user2 = userStorage.getUser(user2Id);
                user2.deleteFriend(Long.valueOf(user1Id));
                log.debug("Friends were deleted {}{} ", user1, user2);
            } else {
                log.error(String.format("User with Id = %s doesn't exist",user2Id));
                throw new NotFoundException(String.format("User with Id = %s doesn't exist",user2Id));
            }
        } else {
            log.error(String.format("User with Id = %s doesn't exist",user1Id));
            throw new NotFoundException(String.format("User with Id = %s doesn't exist",user1Id));
        }
    }

    public Set<User> getMutualFriends(Integer user1Id, Integer user2Id){
        if(isContains(user1Id)) {
            if(isContains(user2Id)) {
                Set<User> mutualFriends = new HashSet<>();
                User user1 = userStorage.getUser(user1Id);
                Set<Long> user1Friends = user1.getFriends();
                User user2 = userStorage.getUser(user2Id);
                Set<Long> user2Friends = user2.getFriends();

                for (Long i : user1Friends) {
                    if (user2Friends.contains(i)) {
                        mutualFriends.add(userStorage.getUser(Math.toIntExact(i)));
                    }
                }
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

    public boolean isContains(Integer userId){
        return userId != null && userId > 0  && userStorage.getUsers().containsKey(userId);
    }
}


