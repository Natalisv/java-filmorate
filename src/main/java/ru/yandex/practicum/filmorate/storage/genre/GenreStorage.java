package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    List<Genre> findFilmGenres(Long Id);

    Genre findGenreById(Long id);

    List<Genre> findAllGenre();
}
