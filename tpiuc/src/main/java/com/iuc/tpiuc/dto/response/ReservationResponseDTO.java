package com.iuc.tpiuc.dto.response;


import com.iuc.tpiuc.enums.ReservationStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDTO {

    private Long id;

    private LocalDate dateReservation;

    private LocalTime heureDebut;

    private LocalTime heureFin;

    private ReservationStatus status;

    private UtilisateurResponseDTO professeur;

    private SalleResponseDTO salle;

    private List<MaterielResponseDTO> materiels;

}