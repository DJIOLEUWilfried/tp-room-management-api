package com.iuc.tpiuc.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalleRequestDTO {

    @Schema(
            description = "Nom unique de la salle",
            example = "Salle TP Réseaux",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Le nom unique de la salle est obligatoire")
    private String nom;


    @Schema(
            description = "Capacité maximale de la salle",
            example = "40",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "La capacité est obligatoire")
    @Min(value = 1, message = "La capacité doit être supérieure à 0")
    private Integer capacite;



    @Schema(
            description = """
                    Disponibilité de la salle.

                    true  : disponible

                    false : indisponible
                    """,
            example = "true"
    )
    private Boolean disponible;


    @Schema(
            description = "Localisation physique de la salle",
            example = "Bâtiment Informatique - Niveau 2",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "La localisation de la salle est obligatoire")
    private String localisation;

}