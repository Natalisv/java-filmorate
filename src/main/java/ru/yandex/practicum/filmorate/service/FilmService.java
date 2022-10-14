package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final InMemoryFilmStorage filmStorage;
    private final UserStorage userStorage;


    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addFilm(Film film){
        filmStorage.addFilm(film);
    }

    public void updateFilm(Film film){
        filmStorage.updateFilm(film);
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilm(id);
    }

    public List<Film> getListFilms() {
        return filmStorage.getListFilms();
    }

    public void deleteFilmById(Integer id) {
        filmStorage.deleteFilm(id);
    }

    public void addLike(Integer filmId, Integer userId){
        if(isContainsFilm(filmId)) {
            if (isContainsUser(userId)) {
                Film film = filmStorage.getFilms().get(filmId);
                film.addLike();
                film.addLikeUser(Long.valueOf(userId));
                log.debug("Like was added to film {} ", film);
            } else {
                log.error(String.format("User with Id = %s doesn't exist", userId));
                throw new ExistsException(String.format("User with Id = %s doesn't exist", userId));
            }
        }else {
            log.error(String.format("Film with Id = %s doesn't exist",filmId));
            throw new ExistsException(String.format("Film with Id = %s doesn't exist",filmId));
        }
    }

    public void deleteLike(Integer filmId, Integer userId){
        if(isContainsFilm(filmId)) {
            if (isContainsUser(userId)) {
                Film film = filmStorage.getFilms().get(filmId);
                film.deleteLike();
                film.deleteLikeUser(Long.valueOf(userId));
                log.debug("Like was deleted to film {} ", film);
            } else {
                log.error(String.format("User with Id = %s doesn't exist", userId));
                throw new ExistsException(String.format("User with Id = %s doesn't exist", userId));
            }
        } else {
            log.error(String.format("Film with Id = %s doesn't exist",filmId));
            throw new ExistsException(String.format("Film with Id = %s doesn't exist",filmId));
        }
    }

    public List<Film> getPopularFilms(Integer count){
        if(count == null){
            count = 10;
        }
        List<Film> list = filmStorage.getListFilms();
        if(list == null){
            log.error("Movie list is empty");
            throw new NotFoundException("Movie list is empty");
        }
        log.debug("Number of movies: " + list.size());
        return list.stream()
                .sorted(Comparator.comparing(Film::getLike).reversed())
                .limit(count).collect(Collectors.toList());
    }

    public boolean isContainsFilm(Integer filmId){
        return filmId != null &&  filmId > 0 && filmStorage.getFilms().containsKey(filmId);
    }
    public boolean isContainsUser(Integer userId){
        return userId != null && userId > 0  && userStorage.getUsers().containsKey(userId);
    }
}
