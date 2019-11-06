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
    private List<String> locations = new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;
    private int missed;

    public Hit() {
    }

    public Hit(Salvo salvo, GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        this.turn = salvo.getTurn();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void loadHitLocationsBySalvo(Salvo salvo) {
        salvo.getSalvoLocations().stream().forEach(
                position ->
                {
                    gamePlayer.getShips().stream().forEach(
                            ship ->
                            {
                                ship.getLocations().stream().forEach(
                                        positionShip ->
                                        {
                                            if (position.equals(positionShip)) {
                                                getLocations().add(position);
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
        dto.put("hitLocations", this.getLocations());
        dto.put("damages", this.damages());
        dto.put("missed", missed);
        return dto;
    }

    public Integer missed(Integer turn) {
        return gamePlayer.shipMissedByHitTurn(turn);
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

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public List<String> getLocations() {
        return locations;
    }


    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public int compareTo(Hit hit2) {
        if (turn < hit2.getTurn()) {
            return -1;
        }
        if (turn > hit2.getTurn()) {
            return 1;
        }
        return 0;
    }

    public void setMissed(int i) {
        missed = i;
    }
}



