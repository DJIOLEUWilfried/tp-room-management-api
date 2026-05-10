package com.iuc.tpiuc.dto.response;


import com.iuc.tpiuc.enums.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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


    private String professeurId;

    private String idSalle;

    private List<String> materiels;

}