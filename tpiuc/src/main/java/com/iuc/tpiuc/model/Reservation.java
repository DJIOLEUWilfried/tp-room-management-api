package com.iuc.tpiuc.model;


import com.iuc.tpiuc.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateReservation;

    private LocalTime heureDebut;

    private LocalTime heureFin;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "professeur_id")
    private Utilisateur professeur;

    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;


}