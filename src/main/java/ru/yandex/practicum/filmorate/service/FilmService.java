package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final LocalDate STARTRELEASE = LocalDate.parse("1895-12-28", formatter);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        if (isValid(film)) {
            return filmStorage.addFilm(film);
        } else {
            log.error("Film doesn't pass validation");
            throw new ValidationException("Validation error");
        }
    }

    public Film updateFilm(Film film) {
        if (isValid(film)) {
            return filmStorage.updateFilm(film);
        } else {
            log.error("Film doesn't pass validation");
            throw new ValidationException("Validation error");
        }
    }

    public Film findFilmById(Long id) {
        return filmStorage.findFilmById(id);
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public List<Film> getPopularFilms(Integer count) {
        if (count == null) {
            count = 10;
        }
        List<Film> list = filmStorage.findAllFilms();
        if (list == null) {
            log.error("Movie list is empty");
            throw new NotFoundException("Movie list is empty");
        }
        log.debug("Number of movies: " + list.size());
        return list.stream()
                .sorted(Comparator.comparing(Film::getLikes).reversed())
                .limit(count).collect(Collectors.toList());
    }

    public void addLike(Long filmId, Long userId) {
        if (isContainsFilm(filmId)) {
            if (isContainsUser(userId)) {
                filmStorage.addLike(filmId, userId);
                log.debug("Like was added");
            } else {
                log.error(String.format("User with Id = %s doesn't exist", userId));
                throw new ExistsException(String.format("User with Id = %s doesn't exist", userId));
            }
        } else {
            log.error(String.format("Film with Id = %s doesn't exist", filmId));
            throw new ExistsException(String.format("Film with Id = %s doesn't exist", filmId));
        }
    }

    public void removeLike(Long filmId, Long userId){
        if(isContainsFilm(filmId)) {
            if (isContainsUser(userId)) {
                filmStorage.removeLike(filmId, userId);
                log.debug("Like was deleted");
            } else {
                log.error(String.format("User with Id = %s doesn't exist", userId));
                throw new ExistsException(String.format("User with Id = %s doesn't exist", userId));
            }
        } else {
            log.error(String.format("Film with Id = %s doesn't exist",filmId));
            throw new ExistsException(String.format("Film with Id = %s doesn't exist",filmId));
        }
    }

    public void removeFilm(Long id){
        filmStorage.removeFilm(id);
    }

    private boolean isValid(Film film) {
        return !film.getName().isEmpty() && film.getDescription().length() <= 200 &&
                (LocalDate.parse(film.getReleaseDate(), formatter).isAfter(STARTRELEASE) ||
                        LocalDate.parse(film.getReleaseDate(), formatter).equals(STARTRELEASE)) &&
                film.getDuration() > 0 && film.getMpa() != null;
    }

    private boolean isContainsFilm(Long filmId) {
        return filmId != null && filmId > 0 && filmStorage.findFilmById(filmId) != null;
    }

    private boolean isContainsUser(Long userId) {
        return userId != null && userId > 0 && userStorage.findUserById(userId) != null;
    }
}