package com.codeoftheweb.salvo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Hit> hits = new HashSet<>();

    public GamePlayer() {
    }

    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
    }


    public Map<String, Object> makeGamePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
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
        ship.setGamePlayer(this);
        ships.add(ship);

    }

    public void addSalvo(Salvo salvo) {
        salvo.setGamePlayer(this);
        salvoes.add(salvo);

    }

    public void setShipses(List<Ship> ships) {
        this.ships.addAll(ships);
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
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

    public Set<Hit> getHits() {
        return hits;
    }

    public void setHits(Set<Hit> hits) {
        this.hits = hits;
    }

    public int totalHitsByTypeShip(String type, Integer turn) {

        AtomicReference<Integer> count = new AtomicReference<>(0);
        this.hits.stream().forEach(
                hit -> {
                    if (hit.getTurn() <= turn) {

                        count.updateAndGet(v -> v + (hitsByTypeShip(hit, type)));
                    }
                }
        );
        return count.get();

    }

    public int hitsByTypeShip(Hit hit, String type) {

        AtomicInteger count = new AtomicInteger();
        this.shipsOfType(type).stream().forEach(
                ship -> {
                    ship.getLocations().stream().forEach(
                            shipLocation -> {
                                hit.getLocations().stream().forEach(
                                        hitLocation -> {
                                            if (shipLocation.equals(hitLocation)) {
                                                count.getAndIncrement();
                                            }
                                        }
                                );

                            }
                    );
                }
        );
        return count.get();
    }

    public Set<Ship> shipsOfType(String type) {
        return ships.stream().filter(ship -> ship.getType().equals(type)).collect(Collectors.toSet());
    }

    public Integer shipMissedByHitTurn(Integer turn) {
        AtomicInteger count = new AtomicInteger();
        ships.stream().forEach(
                ship -> {
                    if (totalHitsByTypeShip(ship.getType(), turn) >= ship.getLocations().size()) {
                        count.getAndIncrement();
                    }
                }


        );
        return count.get();
    }

    public List<List<String>> shipsLocations() {
        return this.ships.stream().map(ship -> ship.getLocations()).collect(Collectors.toList());
    }

    public void addHit(Hit hit) {
        hits.add(hit);
    }
    public GamePlayer opponent(){
        return getGame().gamePlayerOpponent(this);
    }
}
