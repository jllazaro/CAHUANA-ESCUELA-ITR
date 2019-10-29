package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Ship;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class GamePlayerShipsController extends ControllerInit {

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
        GamePlayer gamePLayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);
        if (isGuest(authentication) || gamePLayer == null || authentication.getName() != gamePLayer.getPlayer().getUserName()) {
            return new ResponseEntity<>(makeMap("error", "ERROR DE VALIDACION DE DATOS"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePLayer.getShips().size() > 0) {
            return new ResponseEntity<>(makeMap("error", "YA TIENE NAVES COLOCADAS"), HttpStatus.FORBIDDEN);
        }
        ships.stream().forEach(ship -> {
                    gamePLayer.addShip(ship);
                    shipRepository.save(ship);
                }
        );
        gamePlayerRepository.save(gamePLayer);
        return new ResponseEntity<>(makeMap("OK", "OK"), HttpStatus.CREATED);
    }
}
