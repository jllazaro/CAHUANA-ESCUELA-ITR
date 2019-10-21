package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;
    private Double score;
    private LocalDateTime finishDate;

    public Score() {
    }

    public Score(Game game, Player player, Double score) {
        this.game = game;
        this.player = player;
        this.score = score;
        this.game.addScore(this);
    }

    public Score(Game game, Player player, Double score, LocalDateTime finishDate) {
        this.game = game;
        this.player = player;
        this.score = score;
        this.finishDate = finishDate;
        this.game.addScore(this);
    }

    public Map<String, Object> makeDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("player", this.getPlayer().getId());
        dto.put("score", this.getScore());
        dto.put("finishDate", this.getFinishDate());
        return dto;
    }

    public long getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }

    public Double getScore() {
        return score;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }
}

