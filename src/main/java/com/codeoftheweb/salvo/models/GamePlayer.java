package com.codeoftheweb.salvo.models;

import com.codeoftheweb.salvo.repositories.HitRepository;
import com.codeoftheweb.salvo.repositories.SalvoRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
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
//        mergeGameAndPlayer();
    }

//    public void mergeGameAndPlayer() {
////        System.out.println(game.makeGameDTO());
//        game.addGamePlayer(this);
//        player.addGamePlayer(this);
//    }

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
        ship.setGamePlayer(this);
        ships.add(ship);

    }

    public void addSalvo(Salvo salvo) {
        salvo.setGamePlayer(this);
        salvoes.add(salvo);

        this.gamePlayerOpponent().collectSalvo(salvo);
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


    public List<List<String>> locationsOfShipType(String type) {
        return ships.stream().filter(ship -> ship.getType().equals(type))
                .map(ship -> ship.getLocations())
                .collect(Collectors.toList());

    }

    public GamePlayer gamePlayerOpponent() {
        return game.getGamePlayers().stream().filter(gp -> (gp.getPlayer().getUserName() != this.getPlayer().getUserName())).findFirst().orElse(null);
    }

    public void collectSalvo(Salvo salvo) {
        Hit aux = new Hit(salvo, this);
        hits.add(aux);
//        hitRepository.save(aux);
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

}
