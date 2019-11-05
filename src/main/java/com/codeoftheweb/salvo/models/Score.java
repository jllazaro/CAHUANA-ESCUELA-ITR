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
    public Score(GamePlayer gamePlayer, Double score) {
        this.game = gamePlayer.getGame();
        this.player = gamePlayer.getPlayer();
        this.score = score;
        this.finishDate = LocalDateTime.now();
    }
    public Score(Game game, Player player, Double score, LocalDateTime finishDate) {
        this.game = game;
        this.player = player;
        this.score = score;
        this.finishDate = finishDate;
//        this.game.addScore(this);
//        this.player.addScore(this);
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

    public void setId(long id) {
        this.id = id;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }

    public boolean isOfGamePlayer(GamePlayer gamePlayer) {
        return gamePlayer.getGame().getId() == this.getGame().getId() && gamePlayer.getPlayer().getId() == this.getPlayer().getId();
    }
}

