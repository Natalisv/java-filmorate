package ru.yandex.practicum.filmorate.exception;

public class IdExistsException extends Exception {
    public IdExistsException(final String message){
        super(message);
    }
}
