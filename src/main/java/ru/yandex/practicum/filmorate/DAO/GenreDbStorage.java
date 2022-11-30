package ru.yandex.practicum.filmorate.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> findFilmGenres(Long Id){
        String sqlQuery = "select * from genres where id in(select genre_id from film_genre where film_id = ?)";
        return jdbcTemplate.query(sqlQuery, this::mapToRowGenre, Id);
    }

    public Genre findGenreById(Long id){
        String sqlQuery = "select * from genres where id = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sqlQuery, this::mapToRowGenre, id);
        }catch (Exception e){
            throw new NotFoundException(String.format("Genre with Id = %d doesn't exist",id));
        }
        if(genre != null){
            return genre;
        } else {
            throw new NotFoundException(String.format("Genre with Id = %d doesn't exist",id));
        }
    }

    public List<Genre> findAllGenre(){
        String sqlQuery = "select * from genres";
        return jdbcTemplate.query(sqlQuery, this::mapToRowGenre);
    }

    private Genre mapToRowGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }
}

