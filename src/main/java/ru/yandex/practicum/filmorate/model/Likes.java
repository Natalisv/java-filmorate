package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Likes {
    Long filmId;
    Long userId;

    public Likes(){
    }

    public Likes(Long filmId, Long userId) {
        this.filmId = filmId;
        this.userId = userId;
    }
}
