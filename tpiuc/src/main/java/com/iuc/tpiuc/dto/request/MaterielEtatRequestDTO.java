package com.iuc.tpiuc.dto.request;

import com.iuc.tpiuc.enums.MaterielEtat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterielEtatRequestDTO {


    @Schema(
            description = "Nouvel état du matériel",
            example = "DISPONIBLE"
    )
    @NotNull(message = "L'état du matériel est obligatoire")
    private MaterielEtat etat;

}
