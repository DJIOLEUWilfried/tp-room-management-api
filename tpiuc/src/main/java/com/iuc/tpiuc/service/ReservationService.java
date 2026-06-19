package com.iuc.tpiuc.service;

import com.iuc.tpiuc.dto.request.ReservationRequestDTO;
import com.iuc.tpiuc.dto.response.ReservationResponseDTO;
import com.iuc.tpiuc.enums.ReservationStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


public interface ReservationService {

    ReservationResponseDTO create(ReservationRequestDTO dto);

    ReservationResponseDTO updateStatus(Long reservationId,
                                        ReservationStatus status,
                                        Long responsableId);

    ReservationResponseDTO getById(Long id);

    List<ReservationResponseDTO> getAll();

    List<ReservationResponseDTO> getByProfesseur(Long professeurId);

    List<ReservationResponseDTO> getBySalle( Long salleId );

    List<ReservationResponseDTO> getByStatus( ReservationStatus status );

    List<ReservationResponseDTO> getByDateCours( LocalDate dateCours );

    List<ReservationResponseDTO> getByPeriode(
            LocalDate debut,
            LocalDate fin
    );

    boolean delete(Long id);

    ReservationResponseDTO demarrerCours(Long id);

    ReservationResponseDTO validerCahierTexte(Long id);

}
