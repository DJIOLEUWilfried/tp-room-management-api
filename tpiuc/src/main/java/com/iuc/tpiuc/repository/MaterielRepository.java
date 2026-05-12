package com.iuc.tpiuc.repository;

import com.iuc.tpiuc.enums.MaterielEtat;
import com.iuc.tpiuc.model.Materiel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterielRepository extends JpaRepository<Materiel, Long> {

    List<Materiel> findByEtat(MaterielEtat etat);

    boolean findByNom(String nom);

}
