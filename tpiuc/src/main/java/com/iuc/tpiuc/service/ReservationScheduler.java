package com.iuc.tpiuc.service;



import com.iuc.tpiuc.enums.ReservationStatus;
import com.iuc.tpiuc.model.Reservation;
import com.iuc.tpiuc.repository.ReservationRepository;
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
public class ReservationScheduler {

    private final ReservationRepository reservationRepository;


    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void cloturerCoursAutomatiquement() {

        LocalDate today = LocalDate.now();

        LocalTime now = LocalTime.now();

        List<Reservation> reservations =
                reservationRepository
                        .findByStatus(
                                ReservationStatus.EN_COURS
                        );

        for (Reservation reservation : reservations) {

            if (
                    reservation.getDateCours().equals(today)
                            &&
                            now.isAfter(
                                    reservation.getHeureFin()
                            )
            ) {

                reservation.setStatus(
                        ReservationStatus.TERMINEE
                );

                reservation.setFinReelle(
                        LocalDateTime.now()
                );
            }
        }
    }


    @Scheduled(cron = "0 */10 * * * *")
    @Transactional
    public void verifierReservationsExpirees() {

        LocalDate aujourdHui = LocalDate.now();

        LocalTime maintenant = LocalTime.now();

        List<Reservation> reservations =
                reservationRepository.findByStatus( ReservationStatus.VALIDEE );

        for (Reservation reservation : reservations) {

            boolean coursPasse =
                    reservation.getDateCours().isBefore(aujourdHui)

                            ||

                            (
                                    reservation.getDateCours().isEqual(aujourdHui)
                                            &&
                                            reservation.getHeureDebut().isBefore(maintenant)
                            );

            if (coursPasse && !reservation.getCoursEffectue()) {
                reservation.setStatus( ReservationStatus.EXPIREE );
            }

        }

        log.info("\n ============ Vérification des réservations expirées terminée. ============");


    }



}
