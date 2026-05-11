package com.iuc.tpiuc.dto.request;

import com.iuc.tpiuc.enums.ReservationStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequestDTO {

//    @NotNull(message = "La date est obligatoire")
//    @FutureOrPresent(message = "La date doit être aujourd'hui ou dans le futur")
//    private LocalDate dateReservation;

    @NotNull(message = "L'heure de début est obligatoire")
    private LocalTime heureDebut;

    @NotNull(message = "Le status de la reservation est obligatoire")
    private ReservationStatus status;

    @NotNull(message = "L'heure de fin est obligatoire")
    private LocalTime heureFin;

    @NotNull(message = "Le professeur est obligatoire")
    private Long professeurId;

    @NotNull(message = "La salle est obligatoire")
    private Long salleId;

    @NotNull(message = "Le materiel est obligatoire")
    private List<Long> materielIds;


}