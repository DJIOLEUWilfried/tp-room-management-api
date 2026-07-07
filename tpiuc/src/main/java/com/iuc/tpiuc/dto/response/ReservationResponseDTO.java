package com.iuc.tpiuc.dto.response;


import com.iuc.tpiuc.enums.ReservationStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDTO {

    private Long id;

    private LocalDateTime dateCreation;

    private LocalDate dateCours;

    private LocalTime heureDebut;

    private LocalTime heureFin;

    private ReservationStatus status;

    private Boolean coursEffectue;

    private UtilisateurResponseDTO professeur;

    private SalleResponseDTO salle;

    private List<MaterielResponseDTO> materiels;
}