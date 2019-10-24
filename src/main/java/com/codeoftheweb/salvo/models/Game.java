package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private LocalDateTime creationDate = LocalDateTime.now();
    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> players;
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

    public Set<GamePlayer> getPlayers() {
        return players;
    }

    public Map<String, Object> makeGameDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("created", this.getCreationDate());
        dto.put("gamePlayers", this.getPlayers().stream().map(gp -> gp.makeGamePlayerDTO()));
        dto.put("scores", this.getPlayers().stream().map(gp -> gp.getScore().makeDTO()));
        return dto;
    }

    public Map<String, Object> makeGameDTO_gameViewWithSalvoes(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("created", this.getCreationDate());
        dto.put("gamePlayers", this.getPlayers().stream().map(gp -> gp.makeGamePlayerDTO()));
        dto.put("ships", gamePlayer.getShips().stream().map(ship -> ship.makeShipDTO()));
        dto.put("salvoes", getPlayers().stream().flatMap(gp -> gp.getSalvoes().stream().map(salvo -> salvo.makeSalvoDTO())));
        return dto;
    }

    public void addScore(Score score) {
        this.scores.add(score);
    }

}