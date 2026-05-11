package com.iuc.tpiuc.repository;

import com.iuc.tpiuc.model.Signalement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface SignalementRepository extends JpaRepository<Signalement, Long> {

    List<Signalement> findByCreateurId(Long professeurId);

    List<Signalement> findByMaterielId(Long materielId);

}
