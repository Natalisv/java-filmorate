package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Mpa {
    String name;
    Long id;

    public Mpa(){
    }

    public Mpa(String name, Long id) {
        this.name = name;
        this.id = id;
    }
}

