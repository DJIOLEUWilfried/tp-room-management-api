package com.iuc.tpiuc.model;


import com.iuc.tpiuc.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
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
    private Utilisateur utilisateur;

    @ManyToOne
    private Salle salle;

    @ManyToMany
    @JoinTable(
            name = "reservation_materiel",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "materiel_id")
    )
    private List<Materiel> materiels;

}