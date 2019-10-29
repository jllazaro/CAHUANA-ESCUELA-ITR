package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Salvo;
import com.codeoftheweb.salvo.models.Ship;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
public class GamePlayerControllerInit extends ControllerInit {

    @RequestMapping(path = "api/games/players/{gamePlayerId}/salvoes", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> salvoesOfGamePlayer(@PathVariable Long gamePlayerId, Authentication authentication) {
        GamePlayer gamePLayer = gamePlayerRepository.findById(gamePlayerId).get();
        if (isGuest(authentication) || authentication.getName() != gamePLayer.getPlayer().getUserName() || gamePLayer.equals(null)) {
            return new ResponseEntity<>(makeMap("error", "ERROR DE VALIDACION DE DATOS"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(makeMap("salvoes", gamePLayer.getSalvoes().stream().map(ship -> ship.makeSalvoDTO())), HttpStatus.ACCEPTED);
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
    public ResponseEntity<Map<String, Object>> setShipsOfGamePlayer(@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody List<Ship> ships) {
        GamePlayer gamePLayer = gamePlayerRepository.findById(gamePlayerId).get();
        if (isGuest(authentication) || authentication.getName() != gamePLayer.getPlayer().getUserName() || gamePLayer.equals(null)) {
            return new ResponseEntity<>(makeMap("error", "ERROR DE VALIDACION DE DATOS"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePLayer.getShips().size() > 0) {
            return new ResponseEntity<>(makeMap("error", "YA TIENE NAVES COLOCADAS"), HttpStatus.FORBIDDEN);
        }
        ships.stream().forEach(ship -> shipRepository.save(ship));
        ships.stream().forEach(ship -> gamePLayer.addShip(ship));
        gamePlayerRepository.save(gamePLayer);
        return new ResponseEntity<>(makeMap("OK", "OK"), HttpStatus.CREATED);
    }

    @RequestMapping(path = "api/games/players/{gamePlayerId}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> setSalvoesOfGamePlayer(@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody Salvo salvo) {
        GamePlayer gamePLayer = gamePlayerRepository.findById(gamePlayerId).get();
        if (isGuest(authentication) || authentication.getName() != gamePLayer.getPlayer().getUserName() || gamePLayer.equals(null)) {
            return new ResponseEntity<>(makeMap("error", "ERROR DE VALIDACION DE DATOS"), HttpStatus.UNAUTHORIZED);
        }

        if (gamePLayer.haveSalvoWithTurn(salvo.getTurn())) {
            return new ResponseEntity<>(makeMap("error", "YA TIENE salvos para ese turno"), HttpStatus.FORBIDDEN);
        }
        salvoRepository.save(salvo);
        gamePLayer.addSalvo(salvo);
        gamePlayerRepository.save(gamePLayer);
        return new ResponseEntity<>(makeMap("OK", "OK"), HttpStatus.CREATED);
    }

}
