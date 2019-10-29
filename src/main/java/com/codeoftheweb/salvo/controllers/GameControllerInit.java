package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class GameControllerInit extends ControllerInit {


    @RequestMapping(path = "api/games")
    public Map<String, Object> getAllGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        if (isGuest(authentication)) {
            dto.put("player", "Guest");
        } else {
            Player player = playerRepository.findByUserName(authentication.getName());
//            if (authentication.getName() == player.getUserName() && !player.equals(null)) {

                dto.put("player", player.makePlayerDTO());
//            }
//            dto.put("player", "Guest");
        }
        dto.put("games", gameRepository.findAll().stream()
                .map(game -> game.makeGameDTO())
                .collect(Collectors.toList()));
        return dto;
    }

    @RequestMapping(path = "api/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "NO SE ENCUENTRA LOGEADO"), HttpStatus.UNAUTHORIZED);
        }
        Player logged = playerRepository.findByUserName(authentication.getName());
        if (logged == null) {
            return new ResponseEntity<>(makeMap("error", "NO SE ENCUENTRA EMAIL "), HttpStatus.UNAUTHORIZED);
        }
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

        return new ResponseEntity<>(makeMap("gpid", gamePlayerNew.getId()), HttpStatus.CREATED);
    }

    @RequestMapping("api/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> getGamePlayerToPlayerId(@PathVariable Long gamePlayerId, Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();
        if (isGuest(authentication) || authentication.getName() != gamePlayer.getPlayer().getUserName() || gamePlayer.equals(null)) {
            return new ResponseEntity<>(makeMap("error", "NO TIENE PERMISO"), HttpStatus.FORBIDDEN);
        }
        Game gameAux = gameRepository.findById(gamePlayer.getGame().getId()).get();
        return new ResponseEntity<>(gameAux.makeGameDTO_gameViewWithSalvoes(gamePlayer), HttpStatus.ACCEPTED);

    }

}
