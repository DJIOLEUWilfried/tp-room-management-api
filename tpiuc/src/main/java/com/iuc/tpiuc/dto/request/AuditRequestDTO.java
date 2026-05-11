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

    @NotBlank(message = "Le nom de l'entité est obligatoire")
    private String entite;

    @NotNull(message = "L'identifiant de l'entité est obligatoire")
    private Long entiteId;

    @NotNull(message = "L'identifiant de l'utilisateur est obligatoire")
    private Long utilisateurId;

}
