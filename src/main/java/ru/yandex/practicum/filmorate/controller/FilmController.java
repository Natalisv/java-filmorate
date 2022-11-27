package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")

public class FilmController {

    FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //метод добавляет фильм
    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        filmService.addFilm(film);
        return film;
    }

    //метод изменяет фильм
    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        filmService.updateFilm(film);
        return film;
    }

    //метод возвращает список фильмов
    @GetMapping
    public List<Film> getListFilms() {
        return filmService.getListFilms();
    }

    //метод возвращает фильм по Id
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    //метод удаляет фильм по Id
    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable Integer id) {
        filmService.deleteFilmById(id);
    }

    //метод добавляет лайк фильму по его Id
    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
    }

    //метод удаляет лайк фильма по его Id
    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteLike(id, userId);
    }

    //метод возвращает список популярных фильмов
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false) Integer count){
        return filmService.getPopularFilms(count);
    }

}
