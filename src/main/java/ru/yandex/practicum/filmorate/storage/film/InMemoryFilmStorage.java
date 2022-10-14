package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExistsException;
import ru.yandex.practicum.filmorate.exception.IdExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private Integer generateId = 1;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final LocalDate STARTRELEASE = LocalDate.parse("1895-12-28", formatter);

    @Override
    public Film addFilm(Film film){
        if (isValid(film)) {
            generatedId(film);
            if(films.containsKey(film.getId())){
                log.error("Film with Id= " + film.getId()+ " already in exists");
                throw new IdExistsException("Film with Id= " + film.getId()+ " already in exists");
            }
            if(isContains(film)){
                log.error(String.format("Film = %s already exists", film.toString()));
                throw new ExistsException("Film already exists");
            }

                films.put(film.getId(), film);
                log.debug("Add film: {} ", film);
                return film;
        } else {
            log.error("Film doesn't pass validation");
            throw new ValidationException("Validation error");
        }
    }

    @Override
    public Film updateFilm(Film film){
        if(films.containsKey(film.getId())) {
            if (isValid(film)) {
                films.put(film.getId(), film);
                log.debug("Update film: {}", film);
                return film;
            } else {
                log.error("Film doesn't pass validation");
                throw new ValidationException("Validation error");
            }
        }else{
            log.error(String.format("Film with Id = %d doesn't exist",film.getId()));
            throw new NotFoundException(String.format("Film with Id = %d doesn't exist",film.getId()));
        }
    }

    @Override
    public List<Film> getListFilms(){
        log.debug("Number of movies: "+ films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public void deleteFilm(Integer id){
        if(films.containsKey(id)){
            films.remove(id);
            log.debug(String.format("Film with Id = %d was deleted", id));
        } else {
            log.error(String.format("Film with Id = %d doesn't exist",id));
            throw new NotFoundException(String.format("Film with Id = %d doesn't exist",id));
        }
    }

    @Override
    public Map<Integer, Film> getFilms(){
        log.debug("Number of movies: " + films.size());
        return films;
    }

    public Film getFilm(Integer id){
        if(films.containsKey(id)){
            log.debug("Received film: " + films.get(id).toString());
            return films.get(id);
        } else{
            log.error(String.format("Film with Id = %s doesn't exist",id));
            throw new NotFoundException(String.format("Film with Id = %s doesn't exist",id));
        }
    }

    public boolean isValid(Film film) {
        return !film.getName().isEmpty() && film.getDescription().length() <= 200 &&
                (LocalDate.parse(film.getReleaseDate(), formatter).isAfter(STARTRELEASE) ||
                        LocalDate.parse(film.getReleaseDate(), formatter).equals(STARTRELEASE)) &&
                film.getDuration() > 0 ;
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
