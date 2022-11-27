package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    //метод получает рейтинг по id
    @GetMapping("{id}")
    public Mpa findMpaById(@PathVariable Long id){
        return mpaService.findMpaById(id);
    }

    //метод возвращает все рейтинги
    @GetMapping
    public List<Mpa> findAllMpa(){
        return mpaService.findAllMpa();
    }
}
