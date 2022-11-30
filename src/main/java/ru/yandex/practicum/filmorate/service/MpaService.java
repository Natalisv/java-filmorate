package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.DAO.MpaDbStorage;

import java.util.List;

@Slf4j
@Service
public class MpaService {

    MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public Mpa findMpaById(Long id){
        if(id > 0){
            return mpaDbStorage.findMpaById(id);
        }else {
            log.error("Validation error");
            throw new NotFoundException(String.format("Mpa with Id = %d doesn't exist",id));
        }
    }

    public List<Mpa> findAllMpa(){
        return mpaDbStorage.findAllMpa();
    }
}
