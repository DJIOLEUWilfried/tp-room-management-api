package com.iuc.tpiuc.controller;




import com.iuc.tpiuc.dto.request.MaterielEtatRequestDTO;
import com.iuc.tpiuc.dto.request.MaterielRequestDTO;
import com.iuc.tpiuc.dto.response.MaterielResponseDTO;
import com.iuc.tpiuc.enums.MaterielEtat;
import com.iuc.tpiuc.service.MaterielService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;



@Tag(
        name = "Gestion des matériels",
        description = """
        API dédiée à la gestion du matériel didactique utilisé dans les salles de travaux pratiques (TP).

        Fonctionnalités principales :
        - Création d'un matériel
        - Modification des informations d'un matériel
        - Consultation d'un matériel par identifiant
        - Consultation de la liste complète des matériels
        - Suppression logique d'un matériel
        - Mise à jour de l'état d'un matériel
          (DISPONIBLE, OCCUPE, EN_PANNE)
        - Recherche des matériels selon leur état
        - Suivi de l'inventaire des équipements pédagogiques
        - Gestion de la disponibilité des ressources matérielles
        """
)
@RestController
@RequestMapping("/api/v1/materiels")
@RequiredArgsConstructor
@Slf4j
public class MaterielController {

    private final MaterielService materielService;

    @Operation(
            summary = "Créer un matériel",
            description = "Permet d'ajouter un nouveau matériel dans l'inventaire."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Matériel créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "409", description = "Le matériel existe déjà")
    })
    @PostMapping
    public ResponseEntity<MaterielResponseDTO> create(
            @Valid @RequestBody MaterielRequestDTO dto
    ) {

        log.info("Requête création matériel reçue");

        MaterielResponseDTO response =
                materielService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Modifier un matériel",
            description = "Permet de modifier les informations d'un matériel existant."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matériel modifié avec succès"),
            @ApiResponse(responseCode = "404", description = "Matériel introuvable"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MaterielResponseDTO> update(

            @Parameter(
                    description = "Identifiant du matériel",
                    example = "1"
            )
            @PathVariable Long id,

            @Valid @RequestBody MaterielRequestDTO dto
    ) {

        log.info("Requête modification matériel : {}", id);

        MaterielResponseDTO response =
                materielService.update(id, dto);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Consulter un matériel",
            description = "Retourne les informations d'un matériel à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matériel trouvé"),
            @ApiResponse(responseCode = "404", description = "Matériel introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MaterielResponseDTO> getById(

            @Parameter(
                    description = "Identifiant du matériel",
                    example = "1"
            )
            @PathVariable Long id
    ) {

        log.info("Requête consultation matériel : {}", id);

        return ResponseEntity.ok(
                materielService.getById(id)
        );
    }

    @Operation(
            summary = "Lister tous les matériels",
            description = "Retourne la liste complète des matériels enregistrés."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste récupérée avec succès"
    )
    @GetMapping
    public ResponseEntity<List<MaterielResponseDTO>> getAll(Authentication authentication) {

        log.info("Utilisateur connecté : {}", authentication.getName());
        log.info("Authorities : {}", authentication.getAuthorities());

        log.info("Requête liste des matériels");

        return ResponseEntity.ok(
                materielService.getAll()
        );
    }

    @Operation(
            summary = "Supprimer un matériel",
            description = "Effectue une suppression logique d'un matériel."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matériel supprimé"),
            @ApiResponse(responseCode = "404", description = "Matériel introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(

            @Parameter(
                    description = "Identifiant du matériel",
                    example = "1"
            )
            @PathVariable Long id
    ) {

        log.info("Requête suppression matériel : {}", id);

        return ResponseEntity.ok(
                materielService.delete(id)
        );
    }

    @Operation(
            summary = "Changer l'état d'un matériel",
            description = """
                    Permet de modifier l'état d'un matériel.
                    
                    Etats possibles :
                    - DISPONIBLE
                    - EN_PANNE
                    - OCCUPE
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Etat modifié avec succès"),
            @ApiResponse(responseCode = "404", description = "Matériel introuvable"),
            @ApiResponse(responseCode = "400", description = "Etat invalide")
    })
    @PatchMapping("/{id}/etat")
    public ResponseEntity<MaterielResponseDTO> changerEtat(

            @Parameter(
                    description = "Identifiant du matériel",
                    example = "1"
            )
            @PathVariable Long id,

            @Valid
            @RequestBody
            MaterielEtatRequestDTO dto
    ) {

        log.info("Requête changement état matériel {}", id);

        return ResponseEntity.ok(
                materielService.updateEtat(
                        id,
                        dto.getEtat()
                )
        );
    }

    @Operation(
            summary = "Rechercher les matériels par état",
            description = """
                    Retourne tous les matériels correspondant à un état donné.
                    
                    Valeurs possibles :
                    - DISPONIBLE
                    - EN_PANNE
                    - OCCUPE
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "400", description = "Etat invalide")
    })
    @GetMapping("/etat/{etat}")
    public ResponseEntity<List<MaterielResponseDTO>>
    getByEtat(

            @Parameter(
                    description = "Etat du matériel",
                    example = "DISPONIBLE"
            )
            @PathVariable MaterielEtat etat
    ) {

        log.info(
                "Requête recherche matériels par état : {}",
                etat
        );

        return ResponseEntity.ok(
                materielService.getByEtat(etat)
        );
    }



}