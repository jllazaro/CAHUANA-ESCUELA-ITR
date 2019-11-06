package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;
    private Integer turn;
    @ElementCollection
    private List<String> salvoLocations = new ArrayList<>();
    @ElementCollection
    private List<String> hitLocations = new ArrayList<>();

    public Salvo() {
    }

    public Salvo(Integer turn, List<String> salvoLocations) {
        this.turn = turn;
        this.salvoLocations = salvoLocations;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Integer getTurn() {
        return turn;
    }

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public Map<String, Object> makeSalvoDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", this.getTurn());
        dto.put("player", this.getGamePlayer().getPlayer().getId());
        dto.put("locations", this.getSalvoLocations());
        return dto;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public void setSalvoLocations(List<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
    }

    public Map<String, Object> makeDTOofHit() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", this.turn);
        dto.put("hitLocations", this.getHitLocations());
        dto.put("damages", this.damages());
        dto.put("missed", getSalvoLocations().size() - hitLocations.size());
        return dto;
    }

    public Map<String, Object> damages() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("carrierHits", gamePlayer.hitsByTypeShip(this, "carrier"));
        dto.put("battleshipHits", gamePlayer.hitsByTypeShip(this, "battleship"));
        dto.put("submarineHits", gamePlayer.hitsByTypeShip(this, "submarine"));
        dto.put("destroyerHits", gamePlayer.hitsByTypeShip(this, "destroyer"));
        dto.put("patrolboatHits", gamePlayer.hitsByTypeShip(this, "patrolboat"));
        dto.put("carrier", gamePlayer.totalHitsByTypeShip("carrier", this.getTurn()));
        dto.put("battleship", gamePlayer.totalHitsByTypeShip("battleship", this.getTurn()));
        dto.put("submarine", gamePlayer.totalHitsByTypeShip("submarine", this.getTurn()));
        dto.put("destroyer", gamePlayer.totalHitsByTypeShip("destroyer", this.getTurn()));
        dto.put("patrolboat", gamePlayer.totalHitsByTypeShip("patrolboat", this.getTurn()));
        return dto;
    }

    public void loadHitLocationsBySalvo() {
        this.getSalvoLocations().stream().forEach(
                position ->
                {
                    gamePlayer.opponent().getShips().stream().forEach(
                            ship ->
                            {
                                ship.getLocations().stream().forEach(
                                        positionShip ->
                                        {
                                            if (position.equals(positionShip)) {
                                                getHitLocations().add(position);
                                            }
                                        });
                            }
                    );
                }
        );
    }

    public List<String> getHitLocations() {
        return hitLocations;
    }

    public void setHitLocations(List<String> hitLocations) {
        this.hitLocations = hitLocations;
    }
}
