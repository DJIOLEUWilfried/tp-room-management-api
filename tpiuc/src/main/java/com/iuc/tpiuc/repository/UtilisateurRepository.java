package com.iuc.tpiuc.repository;

import com.iuc.tpiuc.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
}
