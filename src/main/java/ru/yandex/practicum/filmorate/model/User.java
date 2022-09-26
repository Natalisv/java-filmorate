package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;

@Data
public class User {
        Integer id;
        String name;
        @Email
        String email;
        @NotNull
        String login;
        String birthday;

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
}
