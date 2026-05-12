package com.iuc.tpiuc.service.impl;

import com.iuc.tpiuc.dto.request.AuditRequestDTO;
import com.iuc.tpiuc.dto.request.ReservationRequestDTO;
import com.iuc.tpiuc.dto.response.ReservationResponseDTO;
import com.iuc.tpiuc.enums.ReservationStatus;
import com.iuc.tpiuc.enums.Role;
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

    private final ReservationMapper reservationMapper;

    @Override
    public ReservationResponseDTO create(ReservationRequestDTO dto) {

        log.info("\n ============ Début création réservation ============");

        try {

            // ==============================
            // VALIDATION HEURES
            // ==============================

            if (dto.getHeureDebut()
                    .isAfter(dto.getHeureFin())) {

                log.error("Heure début > heure fin");

                throw new RuntimeException(
                        "L'heure de début doit être inférieure à l'heure de fin");
            }

            // ==============================
            // RECUPERATION PROFESSEUR
            // ==============================

            Utilisateur professeur =
                    utilisateurRepository.findById(
                                    dto.getProfesseurId())
                            .orElseThrow(() -> {

                                log.error("Professeur introuvable");

                                return new RuntimeException(
                                        "Professeur introuvable");
                            });

            // ==============================
            // VERIFIER ROLE
            // ==============================

            if (professeur.getRole() != Role.PROFESSEUR) {

                log.error("Utilisateur non professeur");

                throw new RuntimeException(
                        "Seul un professeur peut réserver");
            }

            // ==============================
            // RECUPERATION SALLE
            // ==============================

            Salle salle = salleRepository.findById(
                            dto.getSalleId())
                    .orElseThrow(() -> {

                        log.error("Salle introuvable");

                        return new RuntimeException(
                                "Salle introuvable");
                    });

            // ==============================
            // VERIFIER DISPONIBILITE
            // ==============================

            if (!salle.getDisponible()) {

                log.error("Salle indisponible");

                throw new RuntimeException(
                        "Salle indisponible");
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

                log.error("Conflit horaire détecté");

                throw new RuntimeException(
                        "Cette salle est déjà réservée sur cette plage horaire");
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

            Reservation reservation =
                    reservationMapper.toEntity(
                            dto,
                            professeur,
                            salle,
                            materiels
                    );

            Reservation saved =
                    reservationRepository.save(
                            reservation);

            // ==============================
            // AUDIT
            // ==============================

            AuditRequestDTO auditDTO = new AuditRequestDTO();

            auditDTO.setAction("CREATION_RESERVATION");
            auditDTO.setUtilisateurId(professeur.getId());

            Audit audit = AuditMapper.toEntity(auditDTO, professeur);

            auditRepository.save(audit);

            log.info("\n ============ Réservation créée : {}  ============", saved.getId());

            return ReservationMapper.toResponseDTO(saved);

        } catch (Exception e) {

            log.error("\n Erreur création réservation", e);

            throw e;
        }


    }

    @Override
    public ReservationResponseDTO updateStatus(Long reservationId, ReservationStatus status, Long responsableId) {
        return null;
    }

    @Override
    public ReservationResponseDTO getById(Long id) {
        return null;
    }

    @Override
    public List<ReservationResponseDTO> getAll() {
        return List.of();
    }

    @Override
    public List<ReservationResponseDTO> getByProfesseur(Long professeurId) {
        return List.of();
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
