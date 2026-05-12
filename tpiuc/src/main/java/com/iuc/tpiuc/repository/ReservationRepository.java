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

    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByDateReservation(LocalDate date);

    /* Vérifions si une salle est déjà réservée sur une plage horaire donnée
       pour eviter les conflits de réservation
    */

    @Query("""
         SELECT r FROM Reservation r
         WHERE r.salle.id = :salleId
         AND r.deleted = false
         AND r.status <> 'REFUSEE'
         AND (
               (:heureDebut < r.heureFin)
               AND
               (:heureFin > r.heureDebut)
         )
    """)
    List<Reservation> findConflitsHoraires(
            @Param("salleId") Long salleId,
            @Param("heureDebut") LocalTime heureDebut,
            @Param("heureFin") LocalTime heureFin
    );


}
