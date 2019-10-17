package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.models.Ship;
import com.codeoftheweb.salvo.repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.repositories.GameRepository;
import com.codeoftheweb.salvo.repositories.PlayerRepository;
import com.codeoftheweb.salvo.repositories.ShipRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository) {
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

            Ship ship1 = new Ship("cruiser","A1");
            Ship ship2 = new Ship("destroyer","A2");
            Ship ship3 = new Ship("battleship","A3");

            GamePlayer gamePlayer1 = new GamePlayer(game1, player1);
            GamePlayer gamePlayer2 = new GamePlayer(game1, player2);
            GamePlayer gamePlayer3 = new GamePlayer(game2, player3);
            GamePlayer gamePlayer4 = new GamePlayer(game2, player4);
            GamePlayer gamePlayer5 = new GamePlayer(game3, player2);
            GamePlayer gamePlayer6 = new GamePlayer(game3, player3);
            gamePlayer1.addShip(ship1);
            gamePlayer1.addShip(ship2);
            gamePlayer1.addShip(ship3);
            gamePlayer2.addShip(ship1);


            playerRepository.save(player1);
            playerRepository.save(player2);
            playerRepository.save(player3);
            playerRepository.save(player4);
            gameRepository.save(game1);
            gameRepository.save(game2);
            gameRepository.save(game3);
            shipRepository.save(ship1);
            shipRepository.save(ship2);
            shipRepository.save(ship3);
            gamePlayerRepository.save(gamePlayer1);
            gamePlayerRepository.save(gamePlayer2);
            gamePlayerRepository.save(gamePlayer3);
            gamePlayerRepository.save(gamePlayer4);
            gamePlayerRepository.save(gamePlayer5);
            gamePlayerRepository.save(gamePlayer6);

        };
    }
}
