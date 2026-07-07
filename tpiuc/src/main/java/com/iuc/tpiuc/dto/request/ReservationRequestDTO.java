package com.iuc.tpiuc.dto.request;

import com.iuc.tpiuc.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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


    @Schema(
            description = "Date prévue du cours",
            example = "2026-06-15",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "La date du cours est obligatoire")
    private LocalDate dateCours;

    @Schema(
            description = "Heure de début du cours",
            example = "08:00:00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "L'heure de début est obligatoire")
    private LocalTime heureDebut;

    @Schema(
            description = "Heure de fin du cours",
            example = "10:00:00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "L'heure de fin est obligatoire")
    private LocalTime heureFin;

    @Schema(
            description = "Statut de la réservation",
            example = "EN_ATTENTE",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private ReservationStatus status;

    @Schema(
            description = "Identifiant du professeur",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Le professeur est obligatoire")
    private Long professeurId;

    @Schema(
            description = "Identifiant de la salle",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "La salle est obligatoire")
    private Long salleId;

    @Schema(
            description = "Liste des matériels utilisés",
            example = "[1,2,3]",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "La liste des matériels est obligatoire")
    private List<Long> materielIds;

}