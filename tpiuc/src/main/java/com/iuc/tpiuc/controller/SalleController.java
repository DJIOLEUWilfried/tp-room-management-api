package com.iuc.tpiuc.controller;


import com.iuc.tpiuc.dto.request.SalleRequestDTO;
import com.iuc.tpiuc.dto.response.SalleResponseDTO;
import com.iuc.tpiuc.service.SalleService;
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
        name = "Gestion des salles",
        description = """
                API permettant la gestion des salles de travaux pratiques.

                Fonctionnalités :
                - Création d'une salle
                - Modification d'une salle
                - Consultation d'une salle
                - Consultation de toutes les salles
                - Gestion de la disponibilité
                - Recherche par disponibilité
                - Suppression logique
                """
)
@RestController
@RequestMapping("/api/v1/salles")
@RequiredArgsConstructor
@Slf4j
public class SalleController {

    private final SalleService salleService;


    @Operation(
            summary = "Créer une salle",
            description = """
                Permet d'ajouter une nouvelle salle de TP.

                Le nom de la salle doit être unique.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Salle créée avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Une salle portant ce nom existe déjà"
            )
    })
    @PostMapping
    public ResponseEntity<SalleResponseDTO> create(
            @Valid @RequestBody SalleRequestDTO dto
    ) {

        log.info("Requête création salle reçue");

        SalleResponseDTO response =
                salleService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @Operation(
            summary = "Modifier une salle",
            description = """
                Permet de modifier les informations
                d'une salle existante.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Salle modifiée avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Salle introuvable"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<SalleResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody SalleRequestDTO dto
    ) {

        log.info( "Requête modification salle : {}", id );

        SalleResponseDTO response =
                salleService.update(id, dto);

        return ResponseEntity.ok(response);
    }



    @Operation(
            summary = "Consulter une salle",
            description = """
                Retourne les informations détaillées
                d'une salle à partir de son identifiant.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Salle trouvée"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Salle introuvable"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<SalleResponseDTO> getById(
            @PathVariable Long id
    ) {

        log.info( "Requête consultation salle : {}", id );

        return ResponseEntity.ok(
                salleService.getById(id)
        );
    }



    @Operation(
            summary = "Lister toutes les salles",
            description = """
                Retourne la liste complète
                des salles enregistrées.
                """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste récupérée avec succès"
    )
    @GetMapping
    public ResponseEntity<List<SalleResponseDTO>> getAll() {

        log.info( "Requête liste des salles" );

        return ResponseEntity.ok(
                salleService.getAll()
        );
    }



    @Operation(
            summary = "Supprimer une salle",
            description = """
                Effectue une suppression logique
                d'une salle.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Salle supprimée avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Salle introuvable"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(
            @PathVariable Long id
    ) {

        log.info( "Requête suppression salle : {}", id );

        return ResponseEntity.ok(salleService.delete(id));
    }



    @Operation(
            summary = "Modifier la disponibilité d'une salle",
            description = """
                Permet d'activer ou désactiver
                la disponibilité d'une salle.

                true  = disponible

                false = indisponible
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Disponibilité modifiée avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Salle introuvable"
            )
    })
    @PatchMapping("/{id}/disponibilite")
    public ResponseEntity<SalleResponseDTO> changerDisponibilite(
            @PathVariable Long id,
            @RequestParam Boolean disponible
    ) {

        log.info("Requête changement disponibilité salle {}", id);

        return ResponseEntity.ok( salleService.updateDisponibilite(id, disponible )  );
    }


    @Operation(
            summary = "Rechercher les salles par disponibilité",
            description = """
                Retourne les salles selon leur état
                de disponibilité.

                true  = disponible

                false = indisponible
                """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste récupérée avec succès"
    )
    @GetMapping("/disponibilite/{disponible}")
    public ResponseEntity<List<SalleResponseDTO>>
    getByDisponibilite(
            @PathVariable Boolean disponible
    ) {

        log.info("Requête recherche salles disponibilité : {}", disponible );

        return ResponseEntity.ok(
                salleService.getByDisponibilite(
                        disponible
                )
        );
    }

}
