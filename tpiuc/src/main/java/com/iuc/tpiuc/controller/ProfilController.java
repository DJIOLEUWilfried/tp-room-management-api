package com.iuc.tpiuc.controller;




import com.iuc.tpiuc.dto.request.ChangePasswordUtilisateurRequestDTO;
import com.iuc.tpiuc.dto.request.UtilisateurProfileRequestDTO;
import com.iuc.tpiuc.dto.response.UtilisateurResponseDTO;
import com.iuc.tpiuc.service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profil")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Gestion du profil",
        description = "Endpoints permettant à un utilisateur de consulter et modifier son profil."
)
public class ProfilController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/{utilisateurId}")
    @Operation(
            summary = "Consulter un profil utilisateur",
            description = """
                    Permet de récupérer les informations de profil d'un utilisateur
                    à partir de son identifiant.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profil récupéré avec succès",
                    content = @Content(
                            schema = @Schema(implementation = UtilisateurResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur interne du serveur"
            )
    })
    public ResponseEntity<UtilisateurResponseDTO> getProfile(

            @Parameter(
                    description = "Identifiant unique de l'utilisateur",
                    example = "1",
                    required = true
            )
            @PathVariable Long utilisateurId
    ) {

        log.info("Consultation du profil utilisateur : {}", utilisateurId);

        UtilisateurResponseDTO response =
                utilisateurService.getProfile(utilisateurId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{utilisateurId}")
    @Operation(
            summary = "Modifier un profil utilisateur",
            description = """
                    Permet de mettre à jour les informations du profil
                    d'un utilisateur.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profil mis à jour avec succès",
                    content = @Content(
                            schema = @Schema(implementation = UtilisateurResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erreur interne du serveur"
            )
    })
    public ResponseEntity<UtilisateurResponseDTO> updateProfile(

            @Parameter(
                    description = "Identifiant unique de l'utilisateur",
                    example = "1",
                    required = true
            )
            // @PathVariable Long utilisateurId,

            @Valid
            @RequestBody UtilisateurProfileRequestDTO dto
    ) {

        log.info("Modification du profil utilisateur : {}", dto.getEmail());

        UtilisateurResponseDTO response =
                utilisateurService.updateProfile(dto);

        return ResponseEntity.ok(response);
    }


    @PutMapping("/changerPassword")
    public ResponseEntity<UtilisateurResponseDTO> changePassword(
            @Valid @RequestBody ChangePasswordUtilisateurRequestDTO dto
    ) {

        log.info( "Modification du mot de passe" );

        UtilisateurResponseDTO response =
                utilisateurService.changePassword(dto);

        return ResponseEntity.ok(response);
    }


}