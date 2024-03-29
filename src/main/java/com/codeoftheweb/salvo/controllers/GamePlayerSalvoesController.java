package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Hit;
import com.codeoftheweb.salvo.models.Salvo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class GamePlayerSalvoesController extends ControllerInit {

    @RequestMapping(path = "api/games/players/{gamePlayerId}/salvoes", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> salvoesOfGamePlayer(@PathVariable Long gamePlayerId, Authentication authentication) {
        GamePlayer gamePLayer = gamePlayerRepository.findById(gamePlayerId).get();
        if (isGuest(authentication) || authentication.getName() != gamePLayer.getPlayer().getUserName() || gamePLayer.equals(null)) {
            return new ResponseEntity<>(makeMap("error", "ERROR DE VALIDACION DE DATOS"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(makeMap("salvoes", gamePLayer.getSalvoes().stream().map(ship -> ship.makeSalvoDTO())), HttpStatus.ACCEPTED);
    }


    @RequestMapping(path = "api/games/players/{gamePlayerId}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> setSalvoesOfGamePlayer(@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody Salvo salvo) {
        GamePlayer gamePLayer = gamePlayerRepository.findById(gamePlayerId).get();

        salvo.setGamePlayer(gamePLayer);
        salvo.setTurn(gamePLayer.getSalvoes().size() + 1);

        if (isGuest(authentication) || authentication.getName() != gamePLayer.getPlayer().getUserName() || gamePLayer.equals(null)) {
            return new ResponseEntity<>(makeMap("error", "ERROR DE VALIDACION DE DATOS"), HttpStatus.UNAUTHORIZED);
        }

        if (gamePLayer.haveSalvoWithTurn(salvo.getTurn())) {
            return new ResponseEntity<>(makeMap("error", "YA TIENE salvos para ese turno"), HttpStatus.FORBIDDEN);
        }
        Hit hit = new Hit(salvo,gamePLayer.opponent());
        hit.loadHitLocationsBySalvo(salvo);
        salvoRepository.save(salvo);
        hitRepository.save(hit);

        return new ResponseEntity<>(makeMap("OK", "OK"), HttpStatus.CREATED);
    }

}
