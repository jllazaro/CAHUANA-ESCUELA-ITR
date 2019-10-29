package com.codeoftheweb.salvo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Ship> ships = new HashSet<>();
    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvo> salvoes = new HashSet<>();

    public GamePlayer() {
    }

    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public Map<String, Object> makeGamePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
//        dto.put("gpid", this.getId());
        dto.put("id", this.getPlayer().getId());
        dto.put("player", this.getPlayer().makePlayerDTO());
        return dto;
    }

    public long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public void addShip(Ship ship) {
        ships.add(ship);
        ship.setGamePlayer(this);
    }

    public void addSalvo(Salvo salvo) {
        salvoes.add(salvo);
        salvo.setGamePlayer(this);
    }

    public void setShipses(List<Ship> ships) {
        this.ships.addAll(ships);
    }

    public Set<Salvo> getSalvoes() {
        return salvoes ;
    }

    public Score getScore() {
        return
                player.getScore(this.getGame());
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }

    public boolean haveSalvoWithTurn(Integer turn) {
        return this.salvoes.stream().anyMatch(salvo -> salvo.getTurn() == turn);
    }

}
