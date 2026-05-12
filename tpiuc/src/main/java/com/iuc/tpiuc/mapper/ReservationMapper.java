package com.iuc.tpiuc.mapper;

import com.iuc.tpiuc.dto.request.ReservationRequestDTO;
import com.iuc.tpiuc.dto.response.MaterielResponseDTO;
import com.iuc.tpiuc.dto.response.ReservationResponseDTO;
import com.iuc.tpiuc.dto.response.SalleResponseDTO;
import com.iuc.tpiuc.dto.response.UtilisateurResponseDTO;
import com.iuc.tpiuc.enums.ReservationStatus;
import com.iuc.tpiuc.model.Materiel;
import com.iuc.tpiuc.model.Reservation;
import com.iuc.tpiuc.model.Salle;
import com.iuc.tpiuc.model.Utilisateur;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {



    public static Reservation toEntity( ReservationRequestDTO dto,
                                 Utilisateur professeur,
                                 Salle salle,
                                 List<Materiel> materiels)
    {
        return Reservation.builder()
                .heureDebut(dto.getHeureDebut())
                .heureFin(dto.getHeureFin())
                .status(dto.getStatus()) // ReservationStatus.EN_ATTENTE
                .professeur(professeur)
                .salle(salle)
                .materiels(materiels)
                .build();

    }



    public static ReservationResponseDTO toResponseDTO(Reservation r) {

        return ReservationResponseDTO.builder()
                .id(r.getId())
                .dateReservation(r.getDateReservation())
                .heureDebut(r.getHeureDebut())
                .heureFin(r.getHeureFin())
                .status(r.getStatus())

                .professeur(UtilisateurResponseDTO.builder()
                        .id(r.getProfesseur().getId())
                        .nom(r.getProfesseur().getNom())
                        .prenom(r.getProfesseur().getPrenom())
                        .email(r.getProfesseur().getEmail())
                        .build())

                .salle(SalleResponseDTO.builder()
                        .id(r.getSalle().getId())
                        .nom(r.getSalle().getNom())
                        .capacite(r.getSalle().getCapacite())
                        .build())

                .materiels(
                        r.getMateriels().stream()
                                .map(m -> MaterielResponseDTO.builder()
                                        .id(m.getId())
                                        .nom(m.getNom())
                                        .quantite(m.getQuantite())
                                        .build())
                                .toList()
                )
                .build();
    }


}
