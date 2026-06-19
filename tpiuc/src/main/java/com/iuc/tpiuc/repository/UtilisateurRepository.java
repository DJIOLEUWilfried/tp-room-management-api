package com.iuc.tpiuc.repository;

import com.iuc.tpiuc.enums.Role;
import com.iuc.tpiuc.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    boolean existsByEmail(String email);

    Optional<Utilisateur> findByCode(String code);

    List<Utilisateur> findByRole(Role role);

    Optional<Utilisateur> findByEmail(String email);

}
