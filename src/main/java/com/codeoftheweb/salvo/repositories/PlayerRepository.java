package com.codeoftheweb.salvo.repositories;

import com.codeoftheweb.salvo.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long>{
}
