package ru.yandex.practicum.filmorate.exception;

public class IdExistsException extends RuntimeException {
    public IdExistsException(final String message){
        super(message);
    }
}
