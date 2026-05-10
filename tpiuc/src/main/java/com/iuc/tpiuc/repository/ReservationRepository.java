package com.iuc.tpiuc.repository;

import com.iuc.tpiuc.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
