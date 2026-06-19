package com.iuc.tpiuc.repository;

import com.iuc.tpiuc.dto.response.SignalementResponseDTO;
import com.iuc.tpiuc.model.Signalement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface SignalementRepository extends JpaRepository<Signalement, Long> {

    List<Signalement> findByCreateurId(Long professeurId);

    List<Signalement> findByMaterielId(Long materielId);

    List<Signalement> findByCreateurIdAndMaterielId(Long createurId, Long materielId );

    List<Signalement> findByDateSignalementBetween( LocalDateTime debut, LocalDateTime fin );

}
