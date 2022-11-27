
package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDbStorage mpaDbStorage, GenreDbStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

    private Long generateId = 1L;

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into films(name, description, releaseDate, duration, rate, mpa) " +
                "values (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getRate(), film.getMpa().getId());
        this.generatedId(film);
        setGenre(film);
        log.debug("Add film: {} ", film);

        return findFilmById(film.getId());
    }

    @Override
    public Film findFilmById(Long Id) {
        String sqlQuery = "select * from films where id = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, Id);
        } catch (Exception e) {
            throw new NotFoundException(String.format("Film with Id = %s doesn't exist", Id));
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Film updatedFilm = this.findFilmById(film.getId());
        if (updatedFilm != null) {
            String sqlQuery = "update films set id = ?, name = ?, releaseDate = ?, description = ?, duration = ?, rate = ?, mpa = ? where id = ?";
            jdbcTemplate.update(sqlQuery, film.getId(), film.getName(), film.getReleaseDate(), film.getDescription(), film.getDuration(),
                    film.getRate(), film.getMpa().getId(), film.getId());
            setGenreUpdate(film);
            log.debug("Update film: {}", film);
        } else {
            log.error(String.format("Film with Id = %d doesn't exist", film.getId()));
            throw new NotFoundException(String.format("Film with Id = %d doesn't exist", film.getId()));
        }
        return findFilmById(updatedFilm.getId());
    }

    @Override
    public List<Film> findAllFilms() {
        String sqlQuery = "select * from films";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String sqlQuery = "insert into likes(film_id, user_id) values(?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        String sqlQuery = "delete from likes where film_id =? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    private void generatedId(Film film) {
        if (film.getId() == null) {
            film.setId(generateId);
            generateId++;
        }
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getString("releaseDate"))
                .duration(rs.getLong("duration"))
                .rate(rs.getInt("rate"))
                .mpa(mpaDbStorage.findMpaById(rs.getLong("mpa")))
                .genres(genreDbStorage.findFilmGenres(rs.getLong("id")))
                .likes(this.getLikes(rs.getLong("id")))
                .build();
    }

    public Integer getLikes(Long filmId) {
        String sqlQuery = "select count(*) from likes where film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId);
    }

    private void setGenre(Film film) {
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                String sqlQuery = "insert into film_genre values (?, ?)";
                jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
            }
        }
    }

    private void setGenreUpdate(Film film){
        String sqlQuery = "delete from film_genre where film_id = ? AND genre_id in(select genre_id from film_genre where film_id = ?)";
        jdbcTemplate.update(sqlQuery, film.getId(), film.getId());
        if(film.getGenres() != null && film.getGenres().size() > 0) {
            Set<Long> filmGenreId = new HashSet<>();
            for (Genre genre : film.getGenres()) {
                filmGenreId.add(genre.getId());
            }
            for (Long id : filmGenreId) {
                String sqlQuery1 = "insert into film_genre values (?, ?)";
                jdbcTemplate.update(sqlQuery1, film.getId(), id);
            }
        }
    }
}

