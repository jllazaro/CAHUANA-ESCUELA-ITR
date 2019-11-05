package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.repositories.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;


public class ControllerInit {

    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected ShipRepository shipRepository;
    @Autowired
    protected PlayerRepository playerRepository;
    @Autowired
    protected GameRepository gameRepository;
    @Autowired
    protected GamePlayerRepository gamePlayerRepository;
    @Autowired
    protected SalvoRepository salvoRepository;
    @Autowired
    protected HitRepository hitRepository;
    @Autowired
    ScoreRepository scoreRepository;

    protected boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    protected Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
