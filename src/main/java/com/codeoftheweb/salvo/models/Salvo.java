package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
    private  Integer turn;
    @ElementCollection
    private List<String> locations;
    public Salvo (){}
    public Salvo (Integer turn, List <String> locations){
        this.turn= turn;
        this.locations= locations;
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


    public List<String> getLocations() {
        return locations;
    }

    public Map<String, Object> makeSalvoDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("player", this.getGamePlayer().getPlayer().getId());
        dto.put("turn", this.getTurn());
        dto.put("locations", this.getLocations());
        return dto;
    }
}
