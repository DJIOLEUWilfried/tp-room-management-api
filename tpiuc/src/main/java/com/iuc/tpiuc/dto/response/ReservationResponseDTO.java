package com.iuc.tpiuc.dto.response;


import com.iuc.tpiuc.enums.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ReservationResponseDTO {

    private Long id;

    private LocalDate dateReservation;

    private LocalTime heureDebut;

    private LocalTime heureFin;

    private ReservationStatus status;

    private String professeur;

    private String salle;
}