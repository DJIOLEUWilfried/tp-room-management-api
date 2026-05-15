package com.iuc.tpiuc.service.impl;

import com.iuc.tpiuc.audit.AuditTrace;
import com.iuc.tpiuc.dto.request.AuditRequestDTO;
import com.iuc.tpiuc.dto.request.ReservationRequestDTO;
import com.iuc.tpiuc.dto.response.ReservationResponseDTO;
import com.iuc.tpiuc.enums.ReservationStatus;
import com.iuc.tpiuc.enums.Role;
import com.iuc.tpiuc.exception.custom.*;
import com.iuc.tpiuc.mapper.AuditMapper;
import com.iuc.tpiuc.mapper.ReservationMapper;
import com.iuc.tpiuc.model.*;
import com.iuc.tpiuc.repository.*;
import com.iuc.tpiuc.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final AuditRepository auditRepository;

    // private final ReservationMapper reservationMapper;

    @AuditTrace(action = "CREATION_RESERVATION")
    @Override
    public ReservationResponseDTO create(ReservationRequestDTO dto) {

        log.info("\n ============ Début création réservation ============");

        try {

            // ==============================
            // VALIDATION HEURES
            // ==============================

            if (dto.getHeureDebut()
                    .isAfter(dto.getHeureFin())) {

                log.error("\n Heure début > heure fin");

                throw new InvalidReservationTimeException(
                        "\n L'heure de début doit être inférieure à l'heure de fin");
            }

            // ==============================
            // RECUPERATION PROFESSEUR
            // ==============================

            Utilisateur professeur =
                    utilisateurRepository.findById(
                                    dto.getProfesseurId())
                            .orElseThrow(() -> {

                                log.error("\n Professeur introuvable");

                                return new ResourceNotFoundException(
                                        "\n Professeur introuvable");
                            });

            // ==============================
            // VERIFIER ROLE
            // ==============================

            if (professeur.getRole() != Role.PROFESSEUR) {

                log.error("\n Utilisateur non professeur");

                throw new UnauthorizedException("\n Seul un professeur peut réserver");
            }

            // ==============================
            // RECUPERATION SALLE
            // ==============================

            Salle salle = salleRepository.findById(
                            dto.getSalleId())
                    .orElseThrow(() -> {

                        log.error("\n Salle introuvable");

                        return new ResourceNotFoundException(
                                "\n Salle introuvable");
                    });

            // ==============================
            // VERIFIER DISPONIBILITE
            // ==============================

            if (!salle.getDisponible()) {

                log.error("\n Salle indisponible");

                throw new BadRequestException("\n Salle indisponible");
            }

            // ==============================
            // CONFLITS HORAIRES
            // ==============================

            List<Reservation> conflits =
                    reservationRepository.findConflitsHoraires(
                            salle.getId(),
                            dto.getHeureDebut(),
                            dto.getHeureFin()
                    );

            if (!conflits.isEmpty()) {

                log.error("\n Conflit horaire détecté");

                throw new RuntimeException(
                        "\n Cette salle est déjà réservée sur cette plage horaire");
            }

            // ==============================
            // RECUPERATION MATERIELS
            // ==============================

            List<Materiel> materiels =
                    dto.getMaterielIds() == null
                            ? List.of()
                            : materielRepository.findAllById(
                            dto.getMaterielIds());

            // ==============================
            // CREATION RESERVATION
            // ==============================

            dto.setStatus(ReservationStatus.EN_ATTENTE);

            Reservation reservation =
                    ReservationMapper.toEntity(
                            dto,
                            professeur,
                            salle,
                            materiels
                    );

            Reservation saved = reservationRepository.save(reservation);

            // ==============================
            // AUDIT
            // ==============================

            /*
            AuditRequestDTO auditDTO = new AuditRequestDTO();

            auditDTO.setAction("CREATION_RESERVATION");
            auditDTO.setUtilisateurId(professeur.getId());

            Audit audit = AuditMapper.toEntity(auditDTO, professeur);

            auditRepository.save(audit);

            */

            log.info("\n ============ Réservation créée avec succès : {}  ============", saved.getId());

            return ReservationMapper.toResponseDTO(saved);

        } catch (Exception e) {

            log.error("\n Erreur création réservation", e);

            throw e;
        }


    }

    private String buildAuditAction(ReservationStatus status) {

        return switch (status) {

            case VALIDEE ->
                    "VALIDATION_RESERVATION";

            case REFUSEE ->
                    "REFUS_RESERVATION";

            case EN_ATTENTE ->
                    "MISE_EN_ATTENTE_RESERVATION";
        };
    }

    private String buildMessage(ReservationStatus status) {

        return switch (status) {

            case VALIDEE ->
                    "Réservation validée avec succès";

            case REFUSEE ->
                    "Réservation refusée avec succès";

            case EN_ATTENTE ->
                    "Réservation mise en attente avec succès";
        };
    }

    @AuditTrace(action = "MODIFICATION_STATUT_RESERVATION")
    @Override
    public ReservationResponseDTO updateStatus(Long reservationId,
                                               ReservationStatus status,
                                               Long responsableId)
    {

        log.info("\n ============  Validation réservation {}  ============", reservationId);

        try {

            // ==============================
            // RECUPERATION RESPONSABLE
            // ==============================

            Utilisateur responsable = utilisateurRepository.findById( responsableId)
                            .orElseThrow(() -> new ResourceNotFoundException("\n Responsable introuvable"));

            // ==============================
            // VERIFIER ROLE
            // ==============================

            if (responsable.getRole()
                    != Role.RESPONSABLE) {

                log.error("\n Utilisateur n'est pas un responsable");

                throw new UnauthorizedException("\n Seul un responsable peut valider");
            }

            // ==============================
            // RECUPERATION RESERVATION
            // ==============================

            Reservation reservation = reservationRepository.findById(reservationId)
                            .orElseThrow(() -> new ResourceNotFoundException("\n Réservation introuvable"));

            reservation.setStatus(status);

            Reservation updated = reservationRepository.save(reservation);


            log.info("\n ============ {}: {}  ============", buildMessage(status), updated.getId());

            return ReservationMapper.toResponseDTO(updated);

        } catch (Exception e) {

            log.error("\n Erreur validation réservation", e );

            throw e;
        }


    }

    @AuditTrace(action = "RECHERCHE_RESERVATION_PAR_IDENTIFIANT")
    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO getById(Long id) {

        log.info("\n ============ Recherche réservation : {}  ============", id);

        Reservation reservation = reservationRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("\n Réservation introuvable"));

        return ReservationMapper.toResponseDTO(reservation);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getAll() {

        log.info("\n ============ Liste réservations  ============");

        return reservationRepository.findAll()
                .stream()
                .map(ReservationMapper::toResponseDTO)
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getByProfesseur(Long professeurId) {

        log.info("\n ============ Liste réservations professeur {} ============", professeurId );

        return reservationRepository
                .findByProfesseurId(professeurId)
                .stream()
                .map(ReservationMapper::toResponseDTO)
                .toList();
    }

    @Override
    public boolean delete(Long id) {

        log.info("\n ============ Suppression réservation : {}  ============", id);

        try {

            Reservation reservation = reservationRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("\n Réservation introuvable"));

            reservationRepository.delete(reservation);

            log.info("\n Réservation supprimée : {}", id );

            return true;

        } catch (Exception e) {

            log.error("\n Erreur suppression réservation", e);

            throw e;
        }

    }
}
