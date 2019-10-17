package com.codeoftheweb.salvo.controllers;
import com.codeoftheweb.salvo.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @RequestMapping("api/gamesIds")
    public List<Object> getAllGamesIds() {
        return gameRepository.findAll().stream()
                .map(game -> game.getId())
                .collect(Collectors.toList());
    }
    @RequestMapping("api/games")
    public List<Object> getAllGames() {
        return gameRepository.findAll().stream()
                .map(game -> game.makeGameDTO(game))
                .collect(Collectors.toList());
    }
}
