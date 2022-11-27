package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class User {
        Long id;
        String name;
        @Email
        String email;
        @NotNull
        @NotBlank
        String login;
        String birthday;
        List<Long> friends = new ArrayList<>();

        public User(){
        };

        public User(Long id, String name, String email, String login, String birthday) {
                this.id = id;
                this.name = name;
                this.email = email;
                this.login = login;
                this.birthday = birthday;
        }

        public User(Long id, String name, String email, String login, String birthday, List<Long> friends) {
                this.id = id;
                this.name = name;
                this.email = email;
                this.login = login;
                this.birthday = birthday;
                this.friends = friends;
        }

        public User(String name, String email, String login, String birthday) {
                this.name = name;
                this.email = email;
                this.login = login;
                this.birthday = birthday;
        }
}
