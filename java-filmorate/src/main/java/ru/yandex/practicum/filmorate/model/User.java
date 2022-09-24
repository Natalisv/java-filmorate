package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class User {
        private Integer id;
        private String name;
        private String email;
        private String login;
        private String birthday;

        public User(){
        };

        public User(Integer id, String name, String email, String login, String birthday) {
                this.id = id;
                this.name = name;
                this.email = email;
                this.login = login;
                this.birthday = birthday;
        }
}
