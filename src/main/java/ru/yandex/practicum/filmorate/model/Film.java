package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.*;
import lombok.Data;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

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
    Set<Long> likeUsers = new HashSet<>();
    int like = 0;

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

    public void addLike(){
        like++;
    }

    public void deleteLike(){
        like--;
    }

    public int getLike(){
        return like;
    }

    public void addLikeUser(Long id){
        likeUsers.add(id);
    }

    public void deleteLikeUser(Long id){
        likeUsers.remove(id);
    }

    public Set<Long> getLikeUsers(){
        return likeUsers;
    }
}
