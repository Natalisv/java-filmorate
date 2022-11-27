package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class User {
        Integer id;
        String name;
        @Email
        String email;
        @NotNull
        String login;
        String birthday;
        Set<Long> friends = new HashSet<>();

        public User(){
        };

        public User(Integer id, String name, String email, String login, String birthday) {
                this.id = id;
                this.name = name;
                this.email = email;
                this.login = login;
                this.birthday = birthday;
        }

        public User(Integer id, String name, String email, String birthday) {
                this.id = id;
                this.name = name;
                this.email = email;
                this.birthday = birthday;
        }

        public void addFriend(Long id){
                friends.add(id);
        }

        public void deleteFriend(Long id){
                friends.remove(id);
        }

        public Set<Long> getFriends(){
                return friends;
        }
}
