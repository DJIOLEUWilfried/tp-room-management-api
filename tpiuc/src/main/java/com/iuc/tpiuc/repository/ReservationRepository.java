package com.iuc.tpiuc.repository;

import com.iuc.tpiuc.enums.ReservationStatus;
import com.iuc.tpiuc.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUtilisateurId(Long utilisateurId);

    List<Reservation> findByStatus(ReservationStatus status);

    List<Reservation> findByDateReservation(LocalDate date);


}
