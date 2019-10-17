package com.codeoftheweb.salvo.controllers;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @RequestMapping("api/gamesIds")
    public List<Object> getAllGamesIds() {
        return gameRepository.findAll().stream()
                .map(game -> game.getId())
                .collect(Collectors.toList());
    }
    @RequestMapping("api/games")
    public List<Object> getAllGames() {
        return gameRepository.findAll().stream()
                .map(game -> game.makeGameDTO())
                .collect(Collectors.toList());
    }
    @RequestMapping("api/game_view/{playerId}")
    public Object getGamePlayerToPlayerId(@PathVariable Long playerId) {
        return gameRepository.findAll().stream()
                .map(game -> game.makeGameDTO())
                .collect(Collectors.toList());

    }
}
