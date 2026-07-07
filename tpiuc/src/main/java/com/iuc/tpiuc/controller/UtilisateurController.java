package com.iuc.tpiuc.controller;


import com.iuc.tpiuc.dto.request.ChangePasswordUtilisateurRequestDTO;
import com.iuc.tpiuc.dto.request.UtilisateurRequestDTO;
import com.iuc.tpiuc.dto.response.UtilisateurResponseDTO;
import com.iuc.tpiuc.enums.Role;
import com.iuc.tpiuc.service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(
        name = "Gestion des utilisateurs",
        description = """
                API permettant la gestion des utilisateurs du système.

                Fonctionnalités :
                - Création d'un utilisateur
                - Consultation d'un utilisateur
                - Consultation de tous les utilisateurs
                - Modification d'un utilisateur
                - Activation d'un utilisateur
                - Désactivation d'un utilisateur
                - Recherche par rôle
                """
)
@RestController
@RequestMapping("/api/v1/utilisateurs")
@RequiredArgsConstructor
@Slf4j
public class UtilisateurController {

    private final UtilisateurService utilisateurService;



    @Operation(
            summary = "Créer un utilisateur",
            description = """
                Permet de créer un nouvel utilisateur.

                Deux rôles sont autorisés :
                - PROFESSEUR
                - RESPONSABLE
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Utilisateur créé avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email ou code déjà utilisé"
            )
    })
    @PostMapping
    public ResponseEntity<UtilisateurResponseDTO> create(
            @Valid @RequestBody UtilisateurRequestDTO dto
    ) {

        log.info("Requête création utilisateur reçue");

        UtilisateurResponseDTO response =
                utilisateurService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }



    @Operation(
            summary = "Consulter un utilisateur",
            description = """
                Retourne les informations d'un utilisateur
                à partir de son identifiant.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur trouvé"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurResponseDTO> getById(
            @PathVariable Long id
    ) {

        log.info( "Consultation utilisateur : {}", id );

        return ResponseEntity.ok(
                utilisateurService.getById(id)
        );
    }


    @Operation(
            summary = "Lister tous les utilisateurs",
            description = """
                Retourne la liste complète
                des utilisateurs enregistrés.
                """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste récupérée avec succès"
    )
    @GetMapping
    public ResponseEntity<List<UtilisateurResponseDTO>> getAll() {

        log.info( "Liste des utilisateurs" );

        return ResponseEntity.ok(
                utilisateurService.getAll()
        );
    }



    @Operation(
            summary = "Modifier un utilisateur",
            description = """
                Permet de modifier les informations
                d'un utilisateur existant.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur modifié avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UtilisateurRequestDTO dto
    ) {

        log.info( "Modification utilisateur : {}", id );

        UtilisateurResponseDTO response =
                utilisateurService.update(id, dto);

        return ResponseEntity.ok(response);
    }




    @Operation(
            summary = "Désactiver un utilisateur",
            description = """
                Permet de désactiver un utilisateur.

                L'utilisateur reste présent dans le système
                mais ne peut plus accéder aux fonctionnalités.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur désactivé avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            )
    })
    @PatchMapping("/{id}/disable")
    public ResponseEntity<UtilisateurResponseDTO> disableUser(@PathVariable Long id) {

        log.warn("\n ========  REST request to delete User : {}  ========", id);

        UtilisateurResponseDTO response = utilisateurService.disableUser(id);
        return ResponseEntity.ok(response);
    }




    @Operation(
            summary = "Activer un utilisateur",
            description = """
                Permet de réactiver un utilisateur
                précédemment désactivé.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur activé avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utilisateur introuvable"
            )
    })
    @PatchMapping("/{id}/enable")
    public ResponseEntity<UtilisateurResponseDTO> enableUser(@PathVariable Long id) {

        log.info("\n ========  REST request to enable User : {}  ========", id);

        UtilisateurResponseDTO response = utilisateurService.enableUser(id);
        return ResponseEntity.ok(response);
    }



    @Operation(
            summary = "Rechercher les utilisateurs par rôle",
            description = """
                Retourne tous les utilisateurs
                possédant le rôle demandé.

                Valeurs possibles :
                - PROFESSEUR
                - RESPONSABLE
                """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste récupérée avec succès"
    )
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UtilisateurResponseDTO>>
    getByRole(
            @PathVariable Role role
    ) {

        log.info( "Requête recherche utilisateurs par rôle : {}", role );

        return ResponseEntity.ok(
                utilisateurService.getByRole(role)
        );
    }








}