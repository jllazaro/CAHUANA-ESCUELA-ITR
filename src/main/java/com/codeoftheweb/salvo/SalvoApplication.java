package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
        return (args) -> {
            /**##########################*/
            /**INSTANCE PLAYERS*/
            /**##########################*/
            Player player1 = new Player("j.bauer@ctu.gov", passwordEncoder().encode("123456"));
            Player player2 = new Player("c.obrian@ctu.gov", passwordEncoder().encode("123456"));
            Player player3 = new Player("kim_bauer@gmail.com", passwordEncoder().encode("123456"));
            Player player4 = new Player("t.almeida@ctu.gov", passwordEncoder().encode("123456"));
            /**##########################*/
            /**INSTANCE GAMES*/
            /**##########################*/
            Game game1 = new Game();
            Game game2 = new Game();
            Game game3 = new Game();
            Game game4 = new Game();
            Game game5 = new Game();
            game2.setCreationDate(LocalDateTime.now().plusHours(1));
            game3.setCreationDate(LocalDateTime.now().plusHours(2));
            game4.setCreationDate(LocalDateTime.now().plusHours(3));
            game5.setCreationDate(LocalDateTime.now().plusHours(4));
            /**##########################*/
            /**INSTANCE GAMEPLAYERS*/
            /**##########################*/
            GamePlayer gamePlayer1 = new GamePlayer(game1, player1);
            GamePlayer gamePlayer2 = new GamePlayer(game1, player2);
            GamePlayer gamePlayer3 = new GamePlayer(game2, player3);
            GamePlayer gamePlayer4 = new GamePlayer(game2, player4);
            GamePlayer gamePlayer5 = new GamePlayer(game3, player1);
            GamePlayer gamePlayer6 = new GamePlayer(game3, player2);
            GamePlayer gamePlayer7 = new GamePlayer(game4, player3);
            GamePlayer gamePlayer8 = new GamePlayer(game4, player4);
            GamePlayer gamePlayer9 = new GamePlayer(game5, player1);
            GamePlayer gamePlayer10 = new GamePlayer(game5, player2);
            game1.getGamePlayers().add(gamePlayer1);
            game1.getGamePlayers().add(gamePlayer2);
            game2.getGamePlayers().add(gamePlayer3);
            game2.getGamePlayers().add(gamePlayer4);
            game3.getGamePlayers().add(gamePlayer5);
            game3.getGamePlayers().add(gamePlayer6);
            game4.getGamePlayers().add(gamePlayer7);
            game4.getGamePlayers().add(gamePlayer8);
            game5.getGamePlayers().add(gamePlayer9);
            game5.getGamePlayers().add(gamePlayer10);
            /**##########################*/
            /**INSTANCE SHIPS*/
            /**##########################*/
            Ship ship1 = new Ship("carrier", Arrays.asList("A8", "A9", "A10"));
            Ship ship2 = new Ship("destroyer", Arrays.asList("D3", "E3", "F3"));
            Ship ship3 = new Ship("battleship", Arrays.asList("C1", "C2", "C3"));
            Ship ship4 = new Ship("carrier", Arrays.asList("G8", "G9", "G10"));
            Ship ship5 = new Ship("destroyer", Arrays.asList("H3", "I3", "J3"));
            Ship ship6 = new Ship("battleship", Arrays.asList("D1", "D2", "D3"));
            Ship ship7 = new Ship("carrier", Arrays.asList("F8", "F9", "F10"));
            Ship ship8 = new Ship("destroyer", Arrays.asList("A3", "B3", "C3"));
            gamePlayer1.addShip(ship1);
            gamePlayer1.addShip(ship2);
            gamePlayer2.addShip(ship3);
            gamePlayer2.addShip(ship4);
            gamePlayer3.addShip(ship5);
            gamePlayer3.addShip(ship6);
            gamePlayer4.addShip(ship7);
            gamePlayer4.addShip(ship8);
            /**##########################*/
            /**INSTANCE SALVO*/
            /**##########################*/
            Salvo salvo1 = new Salvo(1, Arrays.asList("A8", "A9", "A10"));
            Salvo salvo2 = new Salvo(2, Arrays.asList("D3", "E3", "A10"));
            Salvo salvo3 = new Salvo(1, Arrays.asList("C1", "C2"));
            Salvo salvo4 = new Salvo(2, Arrays.asList("G8", "G9"));
            Salvo salvo5 = new Salvo(1, Arrays.asList("H3", "I3"));
            Salvo salvo6 = new Salvo(2, Arrays.asList("D1", "D2", "D3"));
            Salvo salvo7 = new Salvo(1, Arrays.asList("F8", "F9", "F10"));
            Salvo salvo8 = new Salvo(2, Arrays.asList("A3", "B3", "C3"));
            gamePlayer1.addSalvo(salvo3);
            gamePlayer1.addSalvo(salvo4);
            gamePlayer2.addSalvo(salvo1);
            gamePlayer2.addSalvo(salvo2);
            gamePlayer3.addSalvo(salvo7);
            gamePlayer3.addSalvo(salvo8);
            gamePlayer4.addSalvo(salvo5);
            gamePlayer4.addSalvo(salvo6);
            /**##########################*/
            /**INSTANCE SCORE*/
            /**##########################*/
            Score score1 = new Score(gamePlayer1, 1.00);
            Score score2 = new Score(gamePlayer2, 0.00);
            Score score3 = new Score(gamePlayer3, 0.50);
            Score score4 = new Score(gamePlayer4, 0.50);
            Score score5 = new Score(gamePlayer5, 1.00);
            Score score6 = new Score(gamePlayer6, 0.00);
            Score score7 = new Score(gamePlayer7, 1.00);
            Score score8 = new Score(gamePlayer8, 0.00);
            /**##########################*/
            /**PERSISTING PLAYERS*/
            /**##########################*/
            playerRepository.save(player1);
            playerRepository.save(player2);
            playerRepository.save(player3);
            playerRepository.save(player4);
            /**##########################*/
            /**PERSISTING GAMES*/
            /**##########################*/
            gameRepository.save(game1);
            gameRepository.save(game2);
            gameRepository.save(game3);
            gameRepository.save(game4);
            gameRepository.save(game5);
            /**##########################*/
            /**PERSISTING GAMEPLAYERS*/
            /**##########################*/
            gamePlayerRepository.save(gamePlayer1);
            gamePlayerRepository.save(gamePlayer2);
            gamePlayerRepository.save(gamePlayer3);
            gamePlayerRepository.save(gamePlayer4);
            gamePlayerRepository.save(gamePlayer5);
            gamePlayerRepository.save(gamePlayer6);
            gamePlayerRepository.save(gamePlayer7);
            gamePlayerRepository.save(gamePlayer8);
            gamePlayerRepository.save(gamePlayer9);
            gamePlayerRepository.save(gamePlayer10);

            /**##########################*/
            /**PERSISTING SHIPS*/
            /**##########################*/
            shipRepository.save(ship1);
            shipRepository.save(ship2);
            shipRepository.save(ship3);
            shipRepository.save(ship4);
            shipRepository.save(ship5);
            shipRepository.save(ship6);
            shipRepository.save(ship7);
            shipRepository.save(ship8);
            /**##########################*/
            /**PERSISTING SALVOES*/
            /**##########################*/
            salvoRepository.save(salvo1);
            salvoRepository.save(salvo2);
            salvoRepository.save(salvo3);
            salvoRepository.save(salvo4);
            salvoRepository.save(salvo5);
            salvoRepository.save(salvo6);
            salvoRepository.save(salvo7);
            salvoRepository.save(salvo8);
            /**##########################*/
            /**PERSISTING SCORES*/
            /**##########################*/
            scoreRepository.save(score1);
            scoreRepository.save(score2);
            scoreRepository.save(score3);
            scoreRepository.save(score4);
            scoreRepository.save(score5);
            scoreRepository.save(score6);
            scoreRepository.save(score7);
            scoreRepository.save(score8);
        };
    }
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputUserName -> {
            Player player = playerRepository.findByUserName(inputUserName);
            if (player != null) {
                return new User(player.getUserName(), player.getPassword(),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputUserName);
            }
        });
    }
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers("/api/players").permitAll()
                .antMatchers("/api/games").permitAll()
                .antMatchers("/console/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/web/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()//allow h2 console access to admins only
                .anyRequest().authenticated()//all other urls can be access by any authenticated role
                .antMatchers("/rest/*").hasAuthority("USER")
                .and().csrf().ignoringAntMatchers("/h2-console/**")//don't apply CSRF protection to /h2-console
                .and().headers().frameOptions().sameOrigin();//allow use of frame to same origin urls

        http.formLogin()
                .usernameParameter("name")
                .passwordParameter("pwd")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");
        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
