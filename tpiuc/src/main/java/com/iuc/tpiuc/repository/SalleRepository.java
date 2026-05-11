package com.iuc.tpiuc.repository;

import com.iuc.tpiuc.model.Salle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalleRepository extends JpaRepository<Salle, Long> {

    List<Salle> findByDisponibleTrue();
}
