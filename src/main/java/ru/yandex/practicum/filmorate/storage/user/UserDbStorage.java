package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private Long generateId = 1L;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        String sqlQuery = "insert into users(name, email, login, birthday) " +
                "values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday());
        this.generatedId(user);
        log.debug("Add user: {}", user);
        return findUserById(user.getId());
    }

    @Override
    public User updateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User updatedUser = this.findUserById(user.getId());
        if(updatedUser != null) {
            String sqlQuery = "update users set name = ?, email = ?, login = ?, birthday = ? where id = ?";
            jdbcTemplate.update(sqlQuery, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
            log.debug("Update user: {}", user);
        }
        return findUserById(user.getId());
    }

    @Override
    public User findUserById(Long Id) {
        String sqlQuery = "select * from users where id = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, Id);
        } catch (Exception e){
            throw new NotFoundException(String.format("User with Id = %s doesn't exist", Id));
        }
        return user;
    }

    @Override
    public List<User> findAllUsers(){
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public void addFriendship(Long user1Id, Long user2Id){
        String sqlQuery = "insert into friendship(user_id, friend_id)" + "values(?, ?)";
        jdbcTemplate.update(sqlQuery, user1Id, user2Id);
        log.debug("Add friendship: {}, {}", user1Id, user2Id);
    }

    @Override
    public List<User> getFriends(Long Id){
        String sqlQuery = "select * from users where id in(select friend_id from friendship where user_id = ?)";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, Id);
    }

    @Override
    public void deleteFriend(Long user1Id, Long user2Id){
        String sqlQuery = "delete from friendship where user_id =? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, user1Id, user2Id);
        log.debug("Friendship was deleted");
    }

    @Override
    public List<User> getCommonFriends(Long user1Id, Long user2Id){
        String sqlQuery = "select * from users where id in(select friend_id from friendship where user_id in (?, ?)" +
                "group by friend_id having count(*) = ?)";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, user1Id, user2Id, 2);
    }

    private void generatedId(User user) {
        if (user.getId() == null) {
            user.setId(generateId++);
        }
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .birthday(rs.getString("birthday"))
                .build();
    }
}

