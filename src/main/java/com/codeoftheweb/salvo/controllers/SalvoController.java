package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import com.codeoftheweb.salvo.repositories.SalvoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
public class SalvoController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private SalvoRepository salvoRepository;


    @RequestMapping("api/gamesIds")
    public List<Object> getAllGamesIds() {
        return gameRepository.findAll().stream()
                .map(game -> game.getId())
                .collect(Collectors.toList());
    }

    @RequestMapping("api/games")
    public Object getAllGames() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("player", "Guest");
        dto.put("games", gameRepository.findAll().stream()
                .map(game -> game.makeGameDTO())
                .collect(Collectors.toList()));
        return dto;
    }
    @RequestMapping("api/game_view/{gamePlayerId}")
    public Object getGamePlayerToPlayerId(@PathVariable Long gamePlayerId) {
        GamePlayer gamePLayerAux = gamePlayerRepository.findById(gamePlayerId).get();

        return gameRepository.findById(gamePLayerAux.getGame().getId()).get().makeGameDTO_gameViewWithSalvoes(gamePLayerAux);

    }
//    @RequestMapping(path = "/api/login", method = RequestMethod.POST)
//    public ResponseEntity<Object> login(
//            @RequestParam String userName, @RequestParam String password) {
//
//        if (userName.isEmpty() || password.isEmpty()) {
//            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
//        }
//
//        if (playerRepository.findByUserName(userName) !=  null) {
//            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
//        }
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
