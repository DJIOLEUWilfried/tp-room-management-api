package com.iuc.tpiuc.repository;

import com.iuc.tpiuc.enums.ReservationStatus;
import com.iuc.tpiuc.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByProfesseurId(Long professeurId);

    List<Reservation> findBySalleId( Long salleId );

    List<Reservation> findByDateCours( LocalDate dateCours );

    List<Reservation> findByDateCoursBetween(
            LocalDate debut,
            LocalDate fin
    );

    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByDateCreation(LocalDate date);

    /* Vérifions si une salle est déjà réservée sur une plage horaire donnée
       pour eviter les conflits de réservation
    */

    @Query("""
    SELECT r
    FROM Reservation r
    WHERE r.dateCours = :dateCours
      AND r.status = 'VALIDEE'
      AND :heureDebut BETWEEN r.heureDebut AND r.heureFin
    """)
    List<Reservation> findConflitsHoraires(
            @Param("dateCours") LocalDate dateCours,
            @Param("heureDebut") LocalTime heureDebut
    );




}
