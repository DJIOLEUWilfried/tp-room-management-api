package com.iuc.tpiuc.mapper;

import com.iuc.tpiuc.dto.request.ReservationRequestDTO;
import com.iuc.tpiuc.dto.response.ReservationResponseDTO;
import com.iuc.tpiuc.enums.ReservationStatus;
import com.iuc.tpiuc.model.Materiel;
import com.iuc.tpiuc.model.Reservation;
import com.iuc.tpiuc.model.Salle;
import com.iuc.tpiuc.model.Utilisateur;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ReservationMapper {

    public static Reservation toEntity(ReservationRequestDTO dto) {

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(dto.getProfesseurId());

        Salle salle = new Salle();
        salle.setId(dto.getSalleId());

        return Reservation.builder()
                .dateReservation(dto.getDateReservation())
                .heureDebut(dto.getHeureDebut())
                .heureFin(dto.getHeureFin())
                .status(ReservationStatus.EN_ATTENTE)
                .professeur(utilisateur)
                .salle(salle)
                .build();
    }

    public static ReservationResponseDTO toDTO(Reservation reservation) {

        return ReservationResponseDTO.builder()
                .id(reservation.getId())
                .dateReservation(LocalDateTime.now())
                .heureDebut(reservation.getHeureDebut())
                .heureFin(reservation.getHeureFin())
                .status(reservation.getStatus())
                .professeur(reservation.getProfesseur().getNom() + reservation.getProfesseur().getPrenom())
                .salle(reservation.getSalle().getNom())
                .materiels(
                        reservation.getMateriels()
                        .stream()
                        .map(Materiel::getNom)
                        .toList()
                )
                .build();
    }
}
