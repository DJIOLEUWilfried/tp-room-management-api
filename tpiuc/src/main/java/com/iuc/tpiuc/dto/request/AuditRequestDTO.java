package com.iuc.tpiuc.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditRequestDTO {

    @NotBlank(message = "L'action est obligatoire")
    private String action;


    @NotNull(message = "L'identifiant de l'utilisateur est obligatoire")
    private Long utilisateurId;

}
