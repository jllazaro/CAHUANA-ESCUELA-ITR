package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collector;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private LocalDateTime creationDate = LocalDateTime.now();
    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers = new HashSet<>();
    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<Score> scores = new HashSet<>();

    public Game() {
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public long getId() {
        return id;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Map<String, Object> makeGameDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("created", this.getCreationDate());
        dto.put("gamePlayers", this.getGamePlayers().stream().map(gp -> gp.makeGamePlayerDTO()));
        dto.put("scores", this.getGamePlayers().stream().map(gp -> gp.getScore() != null ? gp.getScore().makeDTO() : ""));
        return dto;
    }


    public Map<String, Object> makeGameDTO_gameViewWithSalvoes(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("created", this.getCreationDate());
        dto.put("gameState", this.state(gamePlayer, gamePlayerOpponent(gamePlayer)));
        dto.put("gamePlayers", this.getGamePlayers().stream().map(gp -> gp.makeGamePlayerDTO()));
        dto.put("ships", gamePlayer.getShips().stream().map(ship -> ship.makeShipDTO()));
        dto.put("salvoes", getGamePlayers().stream().flatMap(aGamePlayer -> aGamePlayer.getSalvoes().stream().map(salvo -> salvo.makeSalvoDTO())));
        dto.put("hits", this.hits(gamePlayer));
        return dto;
    }

    public String state(GamePlayer gamePlayerLogged, GamePlayer gamePlayerOpponent) {
        Integer maxTurns = 101;
        Integer maxShipByPlayer = 5;
        if (gamePlayerLogged.getShips().isEmpty()

        ) {
            return "PLACESHIPS";
        }
        if (gamePlayerOpponent == null || gamePlayerOpponent.getShips().isEmpty()) {
            return "WAITINGFOROPP";
        }
        if (gamePlayerLogged.getSalvoes().isEmpty() || gamePlayerOpponent.getSalvoes().isEmpty()) {
            return "PLAY";
        }
        if (gamePlayerLogged.shipMissedByHitTurn(maxTurns) == maxShipByPlayer
                || gamePlayerOpponent.shipMissedByHitTurn(maxTurns) == maxShipByPlayer) {
            if (gamePlayerLogged.getShips().size() == gamePlayerLogged.shipMissedByHitTurn(maxTurns)
                    && gamePlayerOpponent.getShips().size() == gamePlayerOpponent.shipMissedByHitTurn(maxTurns)) {
                return "TIED";
            }
            if (gamePlayerLogged.shipMissedByHitTurn(maxTurns) == maxShipByPlayer) {
                return "LOST";
            }

            if (gamePlayerOpponent.shipMissedByHitTurn(maxTurns) == maxShipByPlayer) {
                return "WON";
            }
        }
        if (gamePlayerLogged.getSalvoes().size() == gamePlayerOpponent.getSalvoes().size()) {
            return "PLAY";
        }
        return "WAIT";
    }

    private Map<String, Object> hits(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("self", gamePlayer.getHits().stream().map(hit -> hit.makeDTO()));
        if (gamePlayerOpponent(gamePlayer) != null) {
            dto.put("opponent", gamePlayerOpponent(gamePlayer).getHits().stream().map(hit -> hit.makeDTO()));
        } else {
            dto.put("opponent", new ArrayList<>());
        }
        return dto;
    }

    public GamePlayer gamePlayerOpponent(GamePlayer gamePlayer) {
        return gamePlayers.size() > 1 ? this.getGamePlayers().stream().filter(gp -> (gp.getPlayer().getUserName() != gamePlayer.getPlayer().getUserName())).findFirst().get() : null;

    }

    public void addScore(Score score) {
        this.scores.add(score);
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
        getGamePlayers().add(gamePlayer);
    }
}