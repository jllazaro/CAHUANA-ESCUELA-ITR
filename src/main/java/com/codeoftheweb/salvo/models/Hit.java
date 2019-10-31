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
    //    private Map<String, Integer> damage = new HashMap<>();
    //    private Integer missed;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    public Hit() {
    }

    public Hit(Salvo salvo, GamePlayer gamePlayer) {
//        System.out.println("llega al constructor hit");
        this.gamePlayer = gamePlayer;
        this.turn = salvo.getTurn();
//        this.loadHitLocationsBySalvo(salvo.getLocations());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void loadHitLocationsBySalvo(Salvo salvo) {
        salvo.getLocations().stream().forEach(
                position ->
                {
                    gamePlayer.getShips().stream().forEach(
                            ship ->
                            {
                                ship.getLocations().stream().forEach(
                                        positionShip ->
                                        {
                                            System.out.println(position);
                                            System.out.println(positionShip);
                                            System.out.println(position.equals(positionShip));
                                            if (position.equals(positionShip)) {
                                                getLocations().add(position);
                                                System.out.println(getLocations().size());
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
        dto.put("missed", this.missed());
        return dto;
    }

    public Integer missed() {
        return 0;
    }

    public Map<String, Object> damages() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("carrierHits", gamePlayer.hitsByTypeShip(this, "carrier"));
        dto.put("battleshipHits", gamePlayer.hitsByTypeShip(this, "battleship"));
        dto.put("submarineHits", gamePlayer.hitsByTypeShip(this, "submarine"));
        dto.put("destroyerHits", gamePlayer.hitsByTypeShip(this, "destroyer"));
        dto.put("patrolboatHits", gamePlayer.hitsByTypeShip(this, "patrolboat"));
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

    public List<String> getLocations() {
        System.out.println(locations);
        return locations;
    }


    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
}
