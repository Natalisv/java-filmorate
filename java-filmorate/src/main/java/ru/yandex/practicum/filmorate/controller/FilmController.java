package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private Integer generateId = 1;
    private final Map<Integer, Film> films = new HashMap<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final LocalDate STARTRELEASE = LocalDate.parse("1895-12-28", formatter);

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws Exception {
        generatedId(film);
            if(films.containsKey(film.getId())){
                log.error("Film with id= " + film.getId()+ " already in exists");
                throw new Exception("Film with id= " + film.getId()+ " already in exists");
            }
            if(isContains(film)){
                throw new Exception("Film already exists");
            }
            if (isValid(film)) {
                films.put(film.getId(), film);
                log.debug("Add film");
                return film;
            } else {
                log.error("Validation error");
                throw new ValidationException("Validation error");
            }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        generatedId(film);
            if (isValid(film)) {
                films.put(film.getId(), film);
                log.debug("Update film");
                return film;
            } else {
                log.error("Validation error");
                throw new ValidationException("Validation error");
            }
    }

    @GetMapping
    public List<Film> getFilms() {
        log.debug("request films");
        return new ArrayList<>(films.values());
    }

    public boolean isValid(Film film) {
        return !film.getName().isEmpty() && film.getDescription().length() <= 200 &&
                (LocalDate.parse(film.getReleaseDate(), formatter).isAfter(STARTRELEASE) ||
                        LocalDate.parse(film.getReleaseDate(), formatter).equals(STARTRELEASE)) &&
                film.getDuration() > 0 && film.getId() > 0;
    }

    public boolean isContains(Film film){
        boolean isContains = false;
        for(Film savedFilm :  films.values()) {
            if(savedFilm.equals(film)){
                isContains = true;
                break;
            }
        }
        return isContains;
    }

    public void generatedId(Film film) {
        if (film.getId() == null){
            film.setId(generateId++);
        }
    }
}
