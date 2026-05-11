package com.iuc.tpiuc.repository;

import com.iuc.tpiuc.model.Salle;
import com.iuc.tpiuc.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalleRepository extends JpaRepository<Salle, Long> {

    List<Salle> findByDisponibleTrue();

    boolean findByNom(String nom);

}
