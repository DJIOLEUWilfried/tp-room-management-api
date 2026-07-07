package com.iuc.tpiuc.service;

import com.iuc.tpiuc.dto.request.SignalementDescriptionRequestDTO;
import com.iuc.tpiuc.dto.request.SignalementRequestDTO;
import com.iuc.tpiuc.dto.response.SignalementResponseDTO;
import com.iuc.tpiuc.model.Signalement;

import java.time.LocalDateTime;
import java.util.List;

public interface SignalementService {

    SignalementResponseDTO create( SignalementRequestDTO dto );

    SignalementResponseDTO update( Long id, SignalementDescriptionRequestDTO dto );

    SignalementResponseDTO getById( Long id );

    List<SignalementResponseDTO> getAll();

    List<SignalementResponseDTO> getByCreateur( Long createurId );

    List<SignalementResponseDTO> getByMateriel( Long materielId );

    boolean delete( Long id );

    List<SignalementResponseDTO> getByCreateurAndMateriel( Long createurId, Long materielId );

    List<SignalementResponseDTO> getByDateBetween(LocalDateTime debut, LocalDateTime fin );


}
