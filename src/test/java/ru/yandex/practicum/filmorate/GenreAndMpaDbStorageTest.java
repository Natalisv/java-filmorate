package ru.yandex.practicum.filmorate;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GenreAndMpaDbStorageTest {

    JdbcTemplate jdbcTemplate;
    GenreDbStorage genreDbStorage;
    MpaDbStorage mpaDbStorage;
    FilmDbStorage filmDbStorage;

    @AfterEach
    public void updateDB(){
        jdbcTemplate.update("DELETE FROM FILM_GENRE");
        jdbcTemplate.update("DELETE FROM FILMS");
    }

    @Test
    public void findGenreByIdTest(){
        Genre genre = genreDbStorage.findGenreById(1L);
        assertEquals(genre.getId(), 1);
        assertEquals(genre.getName(), "Комедия");
    }

    @Test
    public void findAllGenreTest(){
        List<Genre> genres = genreDbStorage.findAllGenre();
        assertEquals(genres.size(), 6);
    }

    @Test
    public void findFilmGenresTest(){
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1L, "Комедия"));
        genres.add(new Genre(2L, "Драма"));
        Film film = Film.builder().id(1L).name("name").description("description").releaseDate("1967-03-25").duration(100L)
                .rate(5).mpa(new Mpa("G",1L)).likes(0).genres(genres).build();
        filmDbStorage.addFilm(film);
        List<Genre> filmGenre = genreDbStorage.findFilmGenres(film.getId());
        assertEquals(filmGenre.size(), 2);
    }

    @Test
    public void findMpaByIdTest(){
        Mpa mpa = mpaDbStorage.findMpaById(1L);
        assertEquals(mpa.getName(), "G");
    }

    @Test
    public void findAllMpaTest(){
        List<Mpa> listMpa = mpaDbStorage.findAllMpa();
        assertEquals(listMpa.size(), 5);
    }
}
