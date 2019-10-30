package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private Integer turn;
    @ElementCollection
    private Set<String> locations = new HashSet<>();
    //    private Map<String, Integer> damage = new HashMap<>();
    //    private Integer missed;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    public Hit() {
    }

    public Hit(Salvo salvo, GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        this.turn = salvo.getTurn();
        this.loadHitLocationsBySalvo(salvo.getLocations());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void loadHitLocationsBySalvo(List<String> locations) {
        locations.stream().forEach(
                position ->
                {
                    gamePlayer.getShips().stream().forEach(
                            ship ->
                            {
                                ship.getLocations().stream().forEach(
                                        positionShip ->
                                        {
                                            if (position.equals(positionShip)) {
                                                this.locations.add(position);
                                            }
                                        });
                            }
                    );
                }
        );
    }

    public Map<String, Object> makeDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
//        dto.put("turn", "esta vacio");
        dto.put("turn", this.turn);
        dto.put("hitLocations", this.locations);
        dto.put("damages", this.damages());
        dto.put("missed", this.missed());
        return dto;
    }

    public Integer missed() {
        return 0;
    }

    public Map<String, Object> damages() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("carrierHits", gamePlayer.hitsByTypeShip(this, "carrierHits"));
        dto.put("battleshipHits", gamePlayer.hitsByTypeShip(this, "battleshipHits"));
        dto.put("submarineHits", gamePlayer.hitsByTypeShip(this, "submarineHits"));
        dto.put("destroyerHits", gamePlayer.hitsByTypeShip(this, "destroyerHits"));
        dto.put("patrolboatHits", gamePlayer.hitsByTypeShip(this, "patrolboatHits"));
        dto.put("carrier",  gamePlayer.hitsByTypeShip(this, "carrier"));
        dto.put("battleship", gamePlayer.hitsByTypeShip(this, "battleship"));
        dto.put("submarine", gamePlayer.hitsByTypeShip(this, "submarine"));
        dto.put("destroyer",gamePlayer.hitsByTypeShip(this, "destroyer"));
        dto.put("patrolboat", gamePlayer.hitsByTypeShip(this, "patrolboat"));
        return dto;
    }

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public Set<String> getLocations() {
        return locations;
    }

    public void setLocations(Set<String> locations) {
        this.locations = locations;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
}
