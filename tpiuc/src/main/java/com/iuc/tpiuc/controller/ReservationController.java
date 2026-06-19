package com.iuc.tpiuc.controller;


import com.iuc.tpiuc.dto.request.ReservationRequestDTO;
import com.iuc.tpiuc.dto.response.ReservationResponseDTO;
import com.iuc.tpiuc.enums.ReservationStatus;
import com.iuc.tpiuc.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@Tag(
        name = "Gestion des réservations",
        description = """
        Module de gestion des réservations des salles de travaux pratiques (TP)
        et du matériel didactique.

        Ce module permet :
        - de créer une demande de réservation de salle ;
        - d'associer un ou plusieurs matériels à une réservation ;
        - de consulter les réservations par professeur, salle, statut ou période ;
        - de valider ou refuser une réservation par le responsable ;
        - de suivre le cycle de vie complet d'une réservation
          (EN_ATTENTE, VALIDEE, REFUSEE, EN_COURS, TERMINEE, EXPIREE) ;
        - de démarrer automatiquement un cours réservé ;
        - de clôturer les cours à la fin de la période prévue ;
        - de valider le cahier de texte après la tenue du cours ;
        - de détecter les conflits de réservation ;
        - d'assurer une meilleure gestion des ressources pédagogiques
          grâce au suivi des salles et équipements réservés.
        """
)
@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {


    private final ReservationService reservationService;


    @Operation(
            summary = "Créer une réservation",
            description = """
                Permet à un professeur de réserver une salle de TP
                ainsi que les matériels nécessaires.

                La réservation est créée avec le statut EN_ATTENTE.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Réservation créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Salle, matériel ou professeur introuvable"),
            @ApiResponse(responseCode = "409", description = "Conflit horaire détecté")
    })
    @PostMapping
    public ResponseEntity<ReservationResponseDTO> create(
            @Valid @RequestBody ReservationRequestDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservationService.create(dto));
    }


    @Operation(
            summary = "Consulter une réservation",
            description = "Retourne les détails d'une réservation à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation trouvée"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                reservationService.getById(id)
        );
    }


    @Operation(
            summary = "Lister toutes les réservations",
            description = "Retourne toutes les réservations enregistrées."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste récupérée avec succès"
    )
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAll() {

        return ResponseEntity.ok(
                reservationService.getAll()
        );
    }


    @Operation(
            summary = "Réservations d'un professeur",
            description = "Retourne toutes les réservations effectuées par un professeur."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste récupérée"),
            @ApiResponse(responseCode = "404", description = "Professeur introuvable")
    })
    @GetMapping("/professeur/{professeurId}")
    public ResponseEntity<List<ReservationResponseDTO>> getByProfesseur(
            @PathVariable Long professeurId
    ) {

        return ResponseEntity.ok(
                reservationService.getByProfesseur(professeurId)
        );
    }


    @Operation(
            summary = "Réservations d'une salle",
            description = "Retourne toutes les réservations d'une salle."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste récupérée"),
            @ApiResponse(responseCode = "404", description = "Salle introuvable")
    })
    @GetMapping("/salle/{salleId}")
    public ResponseEntity<List<ReservationResponseDTO>> getBySalle(
            @PathVariable Long salleId
    ) {

        return ResponseEntity.ok(
                reservationService.getBySalle(salleId)
        );
    }


    @Operation(
            summary = "Recherche par statut",
            description = """
                Recherche les réservations selon leur statut.

                Valeurs possibles :
                - EN_ATTENTE
                - VALIDEE
                - REFUSEE
                - EN_COURS
                - TERMINEE
                - EXPIREE
                """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste récupérée avec succès"
    )
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReservationResponseDTO>> getByStatus(
            @PathVariable ReservationStatus status
    ) {

        return ResponseEntity.ok(
                reservationService.getByStatus(status)
        );
    }


    @Operation(
            summary = "Recherche par date de cours",
            description = "Retourne toutes les réservations prévues à une date donnée."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste récupérée avec succès"
    )
    @GetMapping("/date/{dateCours}")
    public ResponseEntity<List<ReservationResponseDTO>> getByDateCours(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateCours
    ) {

        return ResponseEntity.ok(
                reservationService.getByDateCours(dateCours)
        );
    }


    @Operation(
            summary = "Recherche par période",
            description = "Retourne toutes les réservations comprises entre deux dates."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Liste récupérée avec succès"
    )
    @GetMapping("/periode")
    public ResponseEntity<List<ReservationResponseDTO>> getByPeriode(

            @Parameter(
                    description = "Date de début",
                    example = "2026-06-01"
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate debut,

            @Parameter(
                    description = "Date de fin",
                    example = "2026-06-04"
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fin
    ) {

        return ResponseEntity.ok(
                reservationService.getByPeriode(
                        debut,
                        fin
                )
        );
    }



    @Operation(
            summary = "Valider ou refuser une réservation",
            description = """
                Permet au responsable de valider ou de refuser une réservation.

                Statuts autorisés :
                - VALIDEE
                - REFUSEE
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statut mis à jour"),
            @ApiResponse(responseCode = "404", description = "Réservation ou responsable introuvable"),
            @ApiResponse(responseCode = "403", description = "Utilisateur non autorisé")
    })

    @PutMapping("/{reservationId}/status")
    public ResponseEntity<ReservationResponseDTO> updateStatus(

            @Parameter(
                    description = "Identifiant de la réservation",
                    example = "1"
            )
            @PathVariable Long reservationId,

            @Parameter(
                    description = "Nouveau statut",
                    example = "VALIDEE"
            )
            @RequestParam ReservationStatus status,

            @Parameter(
                    description = "Identifiant du responsable",
                    example = "2"
            )
            @RequestParam Long responsableId
    ) {

        return ResponseEntity.ok(
                reservationService.updateStatus(
                        reservationId,
                        status,
                        responsableId
                )
        );
    }



    @Operation(
            summary = "Supprimer une réservation",
            description = "Effectue une suppression logique d'une réservation."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation supprimée"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(
            @PathVariable Long id
    ) {

        log.info( "Suppression réservation : {}", id );

        boolean response = reservationService.delete(id);

        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Démarrer un cours",
            description = """
                Permet de démarrer une réservation validée.

                Le statut passe automatiquement à EN_COURS.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cours démarré"),
            @ApiResponse(responseCode = "400", description = "Réservation non valide"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    @PostMapping("/{id}/demarrer")
    public ResponseEntity<ReservationResponseDTO> demarrerCours(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                reservationService.demarrerCours(id)
        );
    }



    @Operation(
            summary = "Valider le cahier de texte",
            description = """
                Permet de confirmer qu'un cours a effectivement été réalisé.

                Cette action permet au responsable
                de vérifier la tenue effective du TP.
                """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cahier validé"),
            @ApiResponse(responseCode = "400", description = "Cours non effectué"),
            @ApiResponse(responseCode = "404", description = "Réservation introuvable")
    })
    @PostMapping("/{id}/cahier-texte")
    public ResponseEntity<ReservationResponseDTO> validerCahierTexte(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                reservationService.validerCahierTexte(id)
        );
    }


}