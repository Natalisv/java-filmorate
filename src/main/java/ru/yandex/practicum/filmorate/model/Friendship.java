package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friendship {
    Long friendId;
    Long userId;

    public Friendship(){
    }

    public Friendship(Long friendId, Long userId) {
        this.friendId = friendId;
        this.userId = userId;
    }
}
