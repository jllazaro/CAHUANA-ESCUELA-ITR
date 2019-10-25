package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.models.Ship;
import com.codeoftheweb.salvo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


@RestController
public class SalvoController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ShipRepository shipRepository;
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

    @RequestMapping(path = "api/games")
    public Map<String, Object> getAllGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        if (isGuest(authentication)) {
            dto.put("player", "Guest");
        } else {
            Player player = playerRepository.findByUserName(authentication.getName());
            dto.put("player", player != null ? player.makePlayerDTO() : null);
        }
        dto.put("games", gameRepository.findAll().stream()
                .map(game -> game.makeGameDTO())
                .collect(Collectors.toList()));
        return dto;
    }

    @RequestMapping("api/game_view/{gamePlayerId}")
    public Map<String, Object> getGamePlayerToPlayerId(@PathVariable Long gamePlayerId) {
        GamePlayer gamePLayerAux = gamePlayerRepository.findById(gamePlayerId).get();
        Game gameAux = gameRepository.findById(gamePLayerAux.getGame().getId()).get();
        return gameAux.makeGameDTO_gameViewWithSalvoes(gamePLayerAux);

    }

    @RequestMapping(path = "api/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "Enter a password "), HttpStatus.UNAUTHORIZED);
        }
        Player logged = playerRepository.findByUserName(authentication.getName());
        Game gameNew = new Game();
        GamePlayer gamePlayerNew = new GamePlayer(gameNew, logged);
        gameRepository.save(gameNew);
        gamePlayerRepository.save(gamePlayerNew);

        return new ResponseEntity<>(makeMap("gpid", gamePlayerNew.getId()), HttpStatus.CREATED);
    }

    @RequestMapping(path = "api/game/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> gameOfPlayer(@PathVariable Long gameId, Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "Enter a password "), HttpStatus.FORBIDDEN);
        }
        Game game = gameRepository.findById(gameId).get();
        if (game.equals(null)) {
            return new ResponseEntity<>(makeMap("error", "No such game"), HttpStatus.FORBIDDEN);
        }
        if (game.getPlayers().size() > 1) {
            return new ResponseEntity<>(makeMap("error", "Game is full"), HttpStatus.FORBIDDEN);
        }
        Player logged = playerRepository.findByUserName(authentication.getName());

        GamePlayer gamePlayerNew = new GamePlayer(game, logged);
        gamePlayerRepository.save(gamePlayerNew);

        return new ResponseEntity<>(makeMap("gpid", game.getId()), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/api/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam String email, @RequestParam String password) {

        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "Enter a password "), HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(email) != null) {
            return new ResponseEntity<>(makeMap("error", "Name in use"), HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "api/games/players/{gamePlayerId}/ships", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> shipsOfGamePlayer(@PathVariable Long gamePlayerId, Authentication authentication) {
        GamePlayer gamePLayer = gamePlayerRepository.findById(gamePlayerId).get();
        if (isGuest(authentication) || authentication.getName() != gamePLayer.getPlayer().getUserName() || gamePLayer.equals(null)) {
            return new ResponseEntity<>(makeMap("error", "ERROR DE VALIDACION DE DATOS"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(makeMap("ships", gamePLayer.getShips().stream().map(ship -> ship.makeShipDTO())), HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "api/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> setShipsOfGamePlayer(@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody Set<Ship> ships) {
        GamePlayer gamePLayer = gamePlayerRepository.findById(gamePlayerId).get();
        if (isGuest(authentication) || authentication.getName() != gamePLayer.getPlayer().getUserName() || gamePLayer.equals(null)) {
            return new ResponseEntity<>(makeMap("error", "ERROR DE VALIDACION DE DATOS"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePLayer.getShips().size() > 0) {
            return new ResponseEntity<>(makeMap("error", "YA TIENE NAVES COLOCADAS"), HttpStatus.FORBIDDEN);
        }
        gamePLayer.getShips().addAll(ships);
        gamePlayerRepository.save(gamePLayer);
        ships.stream().forEach(ship -> shipRepository.save(ship));
        ships.stream().forEach(ship -> gamePLayer.addShip(ship));
        gamePlayerRepository.save(gamePLayer);
        return new ResponseEntity<>(makeMap("OK","OK"),HttpStatus.CREATED);
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
