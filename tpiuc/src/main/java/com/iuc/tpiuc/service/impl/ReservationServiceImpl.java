package com.iuc.tpiuc.service.impl;

import com.iuc.tpiuc.audit.AuditActions;
import com.iuc.tpiuc.audit.AuditTrace;
import com.iuc.tpiuc.dto.request.AuditRequestDTO;
import com.iuc.tpiuc.dto.request.ReservationRequestDTO;
import com.iuc.tpiuc.dto.response.ReservationResponseDTO;
import com.iuc.tpiuc.enums.MaterielEtat;
import com.iuc.tpiuc.enums.ReservationStatus;
import com.iuc.tpiuc.enums.Role;
import com.iuc.tpiuc.exception.custom.*;
import com.iuc.tpiuc.mapper.AuditMapper;
import com.iuc.tpiuc.mapper.ReservationMapper;
import com.iuc.tpiuc.model.*;
import com.iuc.tpiuc.repository.*;
import com.iuc.tpiuc.service.ReservationScheduler;
import com.iuc.tpiuc.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final SalleRepository salleRepository;
    private final MaterielRepository materielRepository;
    private final ReservationScheduler reservationScheduler;



    // private final ReservationMapper reservationMapper;

    @Override
    @AuditTrace(action = AuditActions.CREATION_RESERVATION)
    public ReservationResponseDTO create(
            ReservationRequestDTO dto
    ) {

        log.info( "\n=========== Début création réservation ===========" );

        try {

            validateReservation(dto);

            Utilisateur professeur = getProfesseur(dto.getProfesseurId());

            Salle salle = getSalle(dto.getSalleId());

            List<Materiel> materiels = getMaterielsDisponibles( dto.getMaterielIds() );

            verifyConflitHoraire(
                    dto.getDateCours(),
                    dto.getHeureDebut()
            );


            dto.setStatus( ReservationStatus.EN_ATTENTE );

            Reservation reservation =
                    ReservationMapper.toEntity(
                            dto,
                            professeur,
                            salle,
                            materiels
                    );


            Reservation saved = reservationRepository.save( reservation );

            log.info( "\n=========== Réservation créée : {} ===========", saved.getId() );

            return ReservationMapper.toResponseDTO( saved );

        } catch (Exception e) {

            log.error("\nErreur création réservation", e );
            throw e;
        }


    }

    // =====================================================
    // UPDATE STATUS
    // =====================================================

    @Override
    @AuditTrace( action = AuditActions.MODIFICATION_STATUT_RESERVATION )
    public ReservationResponseDTO updateStatus(
            Long reservationId,
            ReservationStatus status,
            Long responsableId
    ) {

        log.info( "\n=========== Modification statut réservation {} ===========", reservationId );

        try {

            getResponsable(responsableId);

            Reservation reservation =
                    reservationRepository.findById(  reservationId )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException( "Réservation introuvable avec l'id : " + reservationId  )
                            );

            if (reservation.getStatus()
                    != ReservationStatus.EN_ATTENTE) {

                throw new BadRequestException(
                        "Cette réservation a déjà été traitée"
                );
            }

            reservation.setStatus(status);

            switch (status) {

                case VALIDEE -> {

                    reservation.getSalle()
                            .setDisponible(false);

                    reservation.getMateriels()
                            .forEach(m ->
                                    m.setEtat(
                                            MaterielEtat.OCCUPE
                                    ));
                }

                case REFUSEE -> {

                    reservation.getSalle()
                            .setDisponible(true);

                    reservation.getMateriels()
                            .forEach(m ->
                                    m.setEtat(
                                            MaterielEtat.DISPONIBLE
                                    ));
                }

                case EN_ATTENTE -> {
                    // rien
                }
            }

            Reservation updated = reservationRepository.save( reservation );

            log.info( "\n=========== {} : {} ===========", buildMessage(status), updated.getId() );

            return ReservationMapper.toResponseDTO( updated );

        } catch (Exception e) {

            log.error(
                    "\nErreur modification statut réservation",
                    e
            );

            throw e;
        }
    }


    @Override
    @Transactional(readOnly = true)
    @AuditTrace( action = AuditActions.RECHERCHE_RESERVATION_PAR_IDENTIFIANT )
    public ReservationResponseDTO getById(
            Long id
    ) {

        log.info("\nRecherche réservation {}", id );

        Reservation reservation =
                reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Réservation introuvable avec l'id : " + id
                                )
                        );

        return ReservationMapper.toResponseDTO( reservation );
    }



    @Override
    @Transactional(readOnly = true)
    @AuditTrace( action = AuditActions.LISTE_DES_RESERVATIONS )
    public List<ReservationResponseDTO> getAll() {

        log.info( "\nListe des réservations" );

        reservationScheduler.cloturerCoursAutomatiquement();
        reservationScheduler.verifierReservationsExpirees();

        return reservationRepository.findAll()
                .stream()
                .map( ReservationMapper::toResponseDTO )
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    @AuditTrace( action = AuditActions.RECHERCHE_RESERVATION_PAR_PROFESSEUR )
    public List<ReservationResponseDTO>
    getByProfesseur(
            Long professeurId
    ) {

        log.info( "\nRecherche réservations professeur {}", professeurId );

        return reservationRepository
                .findByProfesseurId( professeurId )
                .stream()
                .map( ReservationMapper::toResponseDTO )
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO>
    getBySalle(Long salleId) {

        log.info( "\nRecherche réservations salle {}", salleId );

        return reservationRepository
                .findBySalleId( salleId  )
                .stream()
                .map( ReservationMapper::toResponseDTO )
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO>
    getByStatus(
            ReservationStatus status
    ) {

        log.info( "\nRecherche réservations statut {}", status );

        return reservationRepository
                .findByStatus(status)
                .stream()
                .map( ReservationMapper::toResponseDTO )
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO>
    getByDateCours(
            LocalDate dateCours
    ) {

        log.info( "\nRecherche réservations date {}", dateCours );

        return reservationRepository
                .findByDateCours( dateCours )
                .stream()
                .map( ReservationMapper::toResponseDTO )
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO>
    getByPeriode(
            LocalDate debut,
            LocalDate fin
    ) {

        if (debut.isAfter(fin)) {

            throw new BadRequestException(
                    "La date de début doit être antérieure à la date de fin"
            );
        }

        log.info( "\nRecherche réservations entre {} et {}", debut, fin );

        return reservationRepository
                .findByDateCoursBetween(
                        debut,
                        fin
                )
                .stream()
                .map( ReservationMapper::toResponseDTO )
                .toList();
    }


    @Override
    @AuditTrace( action = AuditActions.SUPPRIMER_RESERVATION )
    public boolean delete(Long id) {

        log.info( "\nSuppression réservation {}", id );

        Reservation reservation =
                reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Réservation introuvable avec l'id : " + id
                                )
                        );

        reservationRepository.delete( reservation );

        log.info( "\nRéservation supprimée {}", id );

        return true;
    }

    // =====================================================
    // PRIVATE METHODS
    // =====================================================

    private void validateReservation(
            ReservationRequestDTO dto
    ) {

        // Vérifions que la date du cours n'est pas avant aujourd'hui
        if (dto.getDateCours().isBefore(LocalDate.now())) {
            throw new InvalidReservationTimeException(
                    "La date prévue pour réservation doit être aujourd'hui ou ultérieure."
            );
        }

        // Verifions si la date du cours est aujourd'hui, l'heure de début doit être après l'heure actuelle
        if (dto.getDateCours().isEqual(LocalDate.now()) && dto.getHeureDebut().isBefore(LocalTime.now())) {
            throw new InvalidReservationTimeException(
                    "L'heure de début doit être après ou égale à l'heure actuelle."
            );
        }

        // 3Vérifions que l'heure de début < heure de fin
        if (!dto.getHeureDebut().isBefore(dto.getHeureFin())) {
            throw new InvalidReservationTimeException(
                    "L'heure de début doit être strictement avant l'heure de fin."
            );
        }


    }

    private Utilisateur getProfesseur(
            Long professeurId
    ) {

        Utilisateur professeur =
                utilisateurRepository.findById(
                                professeurId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Professeur introuvable avec l'id : "
                                                + professeurId
                                )
                        );

        if (professeur.getRole()
                != Role.PROFESSEUR) {

            throw new UnauthorizedException(
                    "Seul un professeur peut effectuer une réservation"
            );
        }

        return professeur;
    }

    private Utilisateur getResponsable(
            Long responsableId
    ) {

        Utilisateur responsable =
                utilisateurRepository.findById(
                                responsableId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Responsable introuvable avec l'id : "
                                                + responsableId
                                )
                        );

        if (responsable.getRole()
                != Role.RESPONSABLE) {

            throw new UnauthorizedException(
                    "Seul un responsable peut valider une réservation"
            );
        }

        return responsable;
    }

    private Salle getSalle(
            Long salleId
    ) {

        Salle salle =
                salleRepository.findById(
                                salleId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Salle introuvable avec l'id : "
                                                + salleId
                                )
                        );

        if (!Boolean.TRUE.equals(
                salle.getDisponible()
        )) {

            throw new BadRequestException(
                    "La salle est indisponible"
            );
        }

        return salle;
    }

    public void verifyConflitHoraire(
            LocalDate dateCours,
            LocalTime heureDebut
    )
    {

        List<Reservation> conflits =
                reservationRepository.findConflitsHoraires(dateCours, heureDebut);

        if (!conflits.isEmpty()) {
            throw new InvalidReservationTimeException(
                    "Conflit détecté : la salle est déjà réservée à cette date et sur cet intervalle horaire."
            );
        }

    }


    private List<Materiel> getMaterielsDisponibles(
            List<Long> materielIds
    ) {

        if (materielIds == null
                || materielIds.isEmpty()) {

            return List.of();
        }

        List<Materiel> materiels =
                materielRepository.findAllById(
                        materielIds
                );

        if (materiels.size()
                != materielIds.size()) {

            throw new ResourceNotFoundException(
                    "Un ou plusieurs matériels sont introuvables"
            );
        }

        materiels.forEach(m -> {

            if (m.getEtat()
                    != MaterielEtat.DISPONIBLE) {

                throw new BadRequestException(
                        "Le matériel "
                                + m.getNom()
                                + " n'est pas disponible"
                );
            }
        });

        return materiels;
    }

    private String buildMessage(
            ReservationStatus status
    ) {

        return switch (status) {

            case VALIDEE -> "Réservation validée";

            case REFUSEE -> "Réservation refusée";

            case EN_COURS -> "Réservation en cours";

            case EN_ATTENTE -> "Réservation mise en attente";

            case EXPIREE -> "Réservation expirée";

            case TERMINEE -> "Réservation terminée";

        };
    }


    @AuditTrace(action = AuditActions.DEMARRER_COURS)
    @Override
    public ReservationResponseDTO demarrerCours(Long id) {

        log.info( "\n =======  Demarrage du cours.  ========" );

        Reservation reservation =
                reservationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Réservation introuvable avec l'id : " + id
                                )
                        );

        if (reservation.getStatus() != ReservationStatus.VALIDEE) {
            throw new BadRequestException(
                    "Cette reservation n'a pas encore été validée"
            );
        }

        reservation.setStatus( ReservationStatus.EN_COURS  );

        reservation.setDebutReel( LocalDateTime.now() );

        reservation.setCoursEffectue(true);

        return ReservationMapper.toResponseDTO(
                reservationRepository.save(reservation) );


    }


    @AuditTrace(action = AuditActions.VALIDATION_CAHIER_TEXTE)
    @Override
    public ReservationResponseDTO validerCahierTexte(Long id) {

        log.info( "\n =======  Validation du Cahier de texte.  ========" );

        try {

            Reservation reservation =
                    reservationRepository.findById(id)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Réservation introuvable avec l'id : " + id
                                    )
                            );

            // Vérification métier

            if (!Boolean.TRUE.equals(reservation.getCoursEffectue())) {
                throw new BadRequestException(
                        "Impossible de valider le cahier de texte : le cours n'a pas encore été effectué."
                );
            }

            if (reservation.getStatus() != ReservationStatus.TERMINEE) {
                throw new BadRequestException(
                        "Impossible de valider le cahier de texte : le cours n'a pas encore terminé."
                );
            }


            if (Boolean.TRUE.equals(reservation.getCahierTexteValide())) {

                throw new BadRequestException(
                        "Le cahier de texte est déjà validé."
                );
            }

            reservation.setCahierTexteValide(true);

            Reservation updated = reservationRepository.save(reservation);

            log.info("\n Cahier de texte validé avec succès pour la réservation {}", id );

            return ReservationMapper.toResponseDTO(updated);
        }
        catch (Exception e) {
            log.error("\n Erreur lors de la validation du cahier de texte", e );

            throw e;
        }


    }

}