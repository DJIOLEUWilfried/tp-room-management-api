package com.iuc.tpiuc.service;

import com.iuc.tpiuc.dto.request.ReservationRequestDTO;
import com.iuc.tpiuc.dto.response.ReservationResponseDTO;
import com.iuc.tpiuc.enums.ReservationStatus;

import java.util.List;

public interface ReservationService {

    ReservationResponseDTO create(ReservationRequestDTO dto);

    ReservationResponseDTO updateStatus(Long reservationId,
                                        ReservationStatus status,
                                        Long responsableId);

    ReservationResponseDTO getById(Long id);

    List<ReservationResponseDTO> getAll();

    List<ReservationResponseDTO> getByProfesseur(Long professeurId);

    boolean delete(Long id);
}
