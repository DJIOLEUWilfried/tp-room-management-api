package com.iuc.tpiuc.repository;

import com.iuc.tpiuc.model.Salle;
import com.iuc.tpiuc.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SalleRepository extends JpaRepository<Salle, Long> {

    List<Salle> findByDisponibleTrue();

    boolean existsByNom(String nom);

    List<Salle> findByDisponible(Boolean disponible);
}
