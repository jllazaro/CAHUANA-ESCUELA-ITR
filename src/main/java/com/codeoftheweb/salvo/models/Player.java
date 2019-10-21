package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Null;
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
    private Set<GamePlayer> games;
    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Score> scores;

    public Player() {
    }

    public Player(String userName) {
        this.userName = userName;

    }

    public String getUserName() {
        return userName;
    }

    public Long getId() {
        return id;
    }

    public Map<String, Object> makePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", this.getId());
        dto.put("email", this.getUserName());
        return dto;
    }

    public Score getScore(Game game) {
        return scores.stream()
                .filter(score -> score.getGame().getId() == game.getId())
                .findFirst().orElse(null);
    }
}
