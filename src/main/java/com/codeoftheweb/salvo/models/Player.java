package com.codeoftheweb.salvo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String userName;
    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<GamePlayer> games;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Score> scores;

    public Player() {
    }

    public Player(String userName) {
        this.userName = userName;

    }

    @JsonProperty("email")
    public String getUserName() {
        return userName;
    }

    public Long getId() {
        return id;
    }

    public Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", player.getId());
        dto.put("email", player.getUserName());
        return dto;
    }

    public Double getScore(Game game) {
        return this.scores.stream()
                .filter(score -> score.getGame().equals(game))
                .findFirst().get().getScore();
    }
}
