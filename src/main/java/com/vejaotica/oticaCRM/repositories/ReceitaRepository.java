package com.vejaotica.oticaCRM.repositories;

import com.vejaotica.oticaCRM.entities.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {
}

