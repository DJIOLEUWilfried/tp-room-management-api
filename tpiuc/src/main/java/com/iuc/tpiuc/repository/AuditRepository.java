package com.iuc.tpiuc.repository;

import com.iuc.tpiuc.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditRepository extends JpaRepository<Audit, Long> {

    List<Audit> findByEntite(String entite);

}
