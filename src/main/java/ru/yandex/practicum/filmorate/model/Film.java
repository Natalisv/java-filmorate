package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Film {
    Long id;
    @NotNull
    @NotBlank
    String name;
    String description;
    String releaseDate;
    @Positive
    Long duration;
    Integer rate;
    Mpa mpa;
    Integer likes;

    List<Genre> genres;

    public Film() {
    }

    public Film(Long id, String name, String description, String releaseDate, Long duration, Integer rate, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
    }

    public Film(Long id, String name, String description, String releaseDate, Long duration, Integer rate, Mpa mpa, List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
    }

    public Film(Long id, String name, String description, String releaseDate, Long duration, Integer rate, Mpa mpa, Integer likes, List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.likes = likes;
        this.genres = genres;
    }
}

