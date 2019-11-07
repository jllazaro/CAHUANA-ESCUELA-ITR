package com.codeoftheweb.salvo.models;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

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

    public String state(GamePlayer gamePlayerLogged) {
        Integer maxTurns = 101;

        if (gamePlayerLogged.getShips().isEmpty()
        ) {
            return "PLACESHIPS";
        }
        if (gamePlayerOpponent(gamePlayerLogged) == null || gamePlayerOpponent(gamePlayerLogged).getShips().isEmpty()) {
            return "WAITINGFOROPP";
        }
        if (gamePlayerLogged.getSalvoes().isEmpty() || gamePlayerOpponent(gamePlayerLogged).getSalvoes().isEmpty()) {
            return "PLAY";
        }
        if (gamePlayerLogged.getSalvoes().size() == gamePlayerOpponent(gamePlayerLogged).getSalvoes().size()
                && (gamePlayerLogged.shipMissedByHitTurn(maxTurns) == gamePlayerLogged.getShips().size()
                || gamePlayerOpponent(gamePlayerLogged).shipMissedByHitTurn(maxTurns) == gamePlayerOpponent(gamePlayerLogged).getShips().size())) {
            if (gamePlayerLogged.getShips().size() == gamePlayerLogged.shipMissedByHitTurn(maxTurns)
                    && gamePlayerOpponent(gamePlayerLogged).getShips().size() == gamePlayerOpponent(gamePlayerLogged).shipMissedByHitTurn(maxTurns)) {
                return "TIE";
            }
            if (gamePlayerLogged.shipMissedByHitTurn(maxTurns) == gamePlayerLogged.getShips().size()) {
                return "LOST";
            }

            if (gamePlayerOpponent(gamePlayerLogged).shipMissedByHitTurn(maxTurns) == gamePlayerOpponent(gamePlayerLogged).getShips().size()) {
                return "WON";
            }
        }
        if (gamePlayerLogged.getSalvoes().size() <= gamePlayerOpponent(gamePlayerLogged).getSalvoes().size()) {
            return "PLAY";
        }
        return "WAIT";
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

    public Score getScore(Game game) {
        return scores.stream()
                .filter(score -> score.getGame().getId() == game.getId())
                .findFirst().orElse(null);
    }

    public Map<String, Object> makeGameDTO_gameViewWithSalvoes(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("created", this.getCreationDate());
        dto.put("gameState", this.state(gamePlayer));
        dto.put("gamePlayers", this.getGamePlayers().stream().map(gp -> gp.makeGamePlayerDTO()));
        dto.put("ships", gamePlayer.getShips().stream().map(ship -> ship.makeShipDTO()));
        dto.put("salvoes", getGamePlayers().stream().flatMap(aGamePlayer -> aGamePlayer.getSalvoes().stream().map(salvo -> salvo.makeSalvoDTO())));
        dto.put("hits", this.hits(gamePlayer));
        return dto;
    }

    private Map<String, Object> hits(GamePlayer logged) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        if (gamePlayerOpponent(logged) != null) {
            dto.put("self", logged.opponent().getSalvoes().stream().sorted(Comparator.comparingInt(Salvo::getTurn)).map(salvoOpp -> salvoOpp.makeDTOofHit()));

            dto.put("opponent", logged.getSalvoes().stream().sorted(Comparator.comparingInt(Salvo::getTurn)).map(salvo -> salvo.makeDTOofHit()));
        } else {
            dto.put("self", new ArrayList<>());
            dto.put("opponent", new ArrayList<>());
        }
        return dto;
    }

    public GamePlayer gamePlayerOpponent(GamePlayer gamePlayer) {
        return gamePlayers.size() > 1 ? this.getGamePlayers().stream().filter(gp -> (gp.getPlayer().getUserName() != gamePlayer.getPlayer().getUserName())).findFirst().get() : null;
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

    public void addScore(Score score) {
        scores.add(score);
    }
}