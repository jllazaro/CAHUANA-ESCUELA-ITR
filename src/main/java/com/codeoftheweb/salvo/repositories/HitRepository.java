package com.codeoftheweb.salvo.repositories;

import com.codeoftheweb.salvo.models.Hit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HitRepository extends JpaRepository<Hit, Long> {
}
