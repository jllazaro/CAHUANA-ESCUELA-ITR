package com.codeoftheweb.salvo;

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
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository) {
        return (args) -> {
            // save a couple of customers
            Player player1 =  new Player("j.bauer@ctu.gov");
            Player player2 = new Player("c.obrian@ctu.gov");
            Player player3 =new Player("kim_bauer@gmail.com");
            Player player4 =new Player("t.almeida@ctu.gov");

            playerRepository.save(player1);
            playerRepository.save(player2);
            playerRepository.save(player3);
            playerRepository.save(player4);
            Game game1 = new Game();
            Game game2 = new Game();
            Game game3 = new Game();
            game2.setCreationDate(LocalDateTime.now().plusHours(1));
            game3.setCreationDate(LocalDateTime.now().plusHours(2));
            gameRepository.save(game1);
            gameRepository.save(game2);
            gameRepository.save(game3);
            gamePlayerRepository.save(new GamePlayer(game1, player1));
            gamePlayerRepository.save(new GamePlayer(game1, player2));
            gamePlayerRepository.save(new GamePlayer(game2, player3));
            gamePlayerRepository.save(new GamePlayer(game2, player4));
            gamePlayerRepository.save(new GamePlayer(game3, player2));
            gamePlayerRepository.save(new GamePlayer(game3, player3));
        };
    }
}
