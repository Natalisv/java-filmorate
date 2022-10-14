package ru.yandex.practicum.filmorate.exception;

public class ExistsException extends RuntimeException{
    public ExistsException(final String message){
        super(message);
    }
}
