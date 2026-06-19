package com.iuc.tpiuc.dto.request;


import com.iuc.tpiuc.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilisateurRequestDTO {


    @Schema(
            description = "Nom de famille de l'utilisateur",
            example = "DJIOLEU",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;


    @Schema(
            description = "Prénom de l'utilisateur",
            example = "Wilfried",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    // @NotBlank(message = "Le code est obligatoire")
    private String code;


    @Schema(
            description = "Adresse email de l'utilisateur",
            example = "djioleuwilfried@gmail.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    // @NotBlank(message = "Le mot de passe est obligatoire")
    private String motDePasse;


    @Schema(
            description = "Rôle attribué à l'utilisateur",
            example = "PROFESSEUR",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Le rôle est obligatoire")
    private Role role;


}