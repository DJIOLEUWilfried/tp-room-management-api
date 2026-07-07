package com.iuc.tpiuc.dto.request;


import com.iuc.tpiuc.enums.MaterielEtat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterielRequestDTO {

    @Schema(
            description = "Nom unique du matériel",
            example = "Ordinateur Dell OptiPlex"
    )
    @NotBlank(message = "Le nom unique du matériel est obligatoire")
    private String nom;


    @Schema(
            description = "Quantité disponible",
            example = "15"
    )
    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être supérieure à 0")
    private Integer quantite;

    // @NotNull(message = "L'état est obligatoire")
    private MaterielEtat etat;

}