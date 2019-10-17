package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    @ElementCollection
    private Set<String> locations = new HashSet<>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;
    private String type;

    public Ship() {
    }
    public Ship(String type, String location) {
        this.type= type;
        this.locations.add(location);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<String> getLocations() {
        return locations;
    }

    public void setLocations(Set<String> locations) {
        this.locations = locations;
    }

//    public GamePlayer getGamePlayer() {
//        return gamePlayer;
//    }
//
//    public void setGamePlayer(GamePlayer gamePlayer) {
//        this.gamePlayer = gamePlayer;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
