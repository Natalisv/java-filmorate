package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@Slf4j
@Service
public class GenreService {

    GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public Genre findGenreById(Long id) {
        if(id > 0) {
            return genreDbStorage.findGenreById(id);
        } else{
            log.error("Validation error");
            throw new NotFoundException(String.format("Genre with Id = %d doesn't exist",id));
        }
    }

    public List<Genre> findAllGenre(){
        return genreDbStorage.findAllGenre();
    }
}
