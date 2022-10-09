package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;

import java.time.Duration;

@Data
public class Film {
    Integer id;
    @NotNull
    @NotBlank
    String name;
    String description;
    String releaseDate;
    @Positive
    long duration;

    public Film() {
    };

    public Film(Integer id, String name, String description, String releaseDate, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(Integer id, String description, String releaseDate, long duration) {
        this.id = id;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
