package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;

@Data
public class Film {
    private Integer id;
    private String name;
    private String description;
    private String releaseDate;
    private long duration;

    public Film() {
    };

    public Film(Integer id, String name, String description, String releaseDate, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
