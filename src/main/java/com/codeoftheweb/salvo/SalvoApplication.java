package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository) {
        return (args) -> {
            // save a couple of customers
            Player player1 = new Player("j.bauer@ctu.gov");
            Player player2 = new Player("c.obrian@ctu.gov");
            Player player3 = new Player("kim_bauer@gmail.com");
            Player player4 = new Player("t.almeida@ctu.gov");

            Game game1 = new Game();
            Game game2 = new Game();
            Game game3 = new Game();
            game2.setCreationDate(LocalDateTime.now().plusHours(1));
            game3.setCreationDate(LocalDateTime.now().plusHours(2));

            Ship ship1 = new Ship("cruiser", Arrays.asList("A8", "A9", "A10"));
            Ship ship2 = new Ship("destroyer", Arrays.asList("D3", "E3", "F3"));
            Ship ship3 = new Ship("battleship", Arrays.asList("C1", "C2", "C3"));
            Ship ship4 = new Ship("cruiser", Arrays.asList("G8", "G9", "G10"));
            Ship ship5 = new Ship("destroyer", Arrays.asList("H3", "I3", "J3"));
            Ship ship6 = new Ship("battleship", Arrays.asList("D1", "D2", "D3"));
            Ship ship7 = new Ship("cruiser", Arrays.asList("F8", "F9", "F10"));
            Ship ship8 = new Ship("destroyer", Arrays.asList("A3", "B3", "C3"));

            Salvo salvo1 = new Salvo(1, Arrays.asList("A8", "A9"));
            Salvo salvo2 = new Salvo(2, Arrays.asList("D3", "E3"));
            Salvo salvo3 = new Salvo(3, Arrays.asList("C1", "C2"));
            Salvo salvo4 = new Salvo(4, Arrays.asList("G8", "G9"));
            Salvo salvo5 = new Salvo(1, Arrays.asList("H3", "I3"));
            Salvo salvo6 = new Salvo(2, Arrays.asList("D1", "D2", "D3"));
            Salvo salvo7 = new Salvo(3, Arrays.asList("F8", "F9", "F10"));
            Salvo salvo8 = new Salvo(4, Arrays.asList("A3", "B3", "C3"));

            GamePlayer gamePlayer1 = new GamePlayer(game1, player1);
            GamePlayer gamePlayer2 = new GamePlayer(game1, player2);
            GamePlayer gamePlayer3 = new GamePlayer(game2, player3);
            GamePlayer gamePlayer4 = new GamePlayer(game2, player4);
            gamePlayer1.addShip(ship1);
            gamePlayer1.addShip(ship2);
            gamePlayer2.addShip(ship3);
            gamePlayer2.addShip(ship4);
            gamePlayer3.addShip(ship5);
            gamePlayer3.addShip(ship6);
            gamePlayer4.addShip(ship7);
            gamePlayer4.addShip(ship8);

            gamePlayer1.addSalvo(salvo3);
            gamePlayer1.addSalvo(salvo4);
            gamePlayer2.addSalvo(salvo1);
            gamePlayer2.addSalvo(salvo2);
            gamePlayer3.addSalvo(salvo7);
            gamePlayer3.addSalvo(salvo8);
            gamePlayer4.addSalvo(salvo6);
            gamePlayer4.addSalvo(salvo5);

            playerRepository.save(player1);
            playerRepository.save(player2);
            playerRepository.save(player3);
            playerRepository.save(player4);
            gameRepository.save(game1);
            gameRepository.save(game2);
            gameRepository.save(game3);

            gamePlayerRepository.save(gamePlayer1);
            gamePlayerRepository.save(gamePlayer2);
            gamePlayerRepository.save(gamePlayer3);
            gamePlayerRepository.save(gamePlayer4);
            shipRepository.save(ship1);
            shipRepository.save(ship2);
            shipRepository.save(ship3);
            shipRepository.save(ship4);
            shipRepository.save(ship5);
            shipRepository.save(ship6);
            shipRepository.save(ship7);
            shipRepository.save(ship8);
            salvoRepository.save(salvo1);
            salvoRepository.save(salvo2);
            salvoRepository.save(salvo3);
            salvoRepository.save(salvo4);
            salvoRepository.save(salvo5);
            salvoRepository.save(salvo6);
            salvoRepository.save(salvo7);
            salvoRepository.save(salvo8);

        };
    }
}
