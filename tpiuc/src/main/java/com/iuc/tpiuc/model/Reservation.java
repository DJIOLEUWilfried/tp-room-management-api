package com.iuc.tpiuc.model;


import com.iuc.tpiuc.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE salle SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDate dateReservation;
    private LocalTime heureDebut;
    private LocalTime heureFin;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column(nullable = false)
    private Boolean deleted = false;


    @ManyToOne
    @JoinColumn(name = "id_professeur")
    private Utilisateur professeur;

    @ManyToOne
    @JoinColumn(name = "id_salle")
    private Salle salle;

    @ManyToMany
    @JoinTable(
            name = "reservation_materiel",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "materiel_id")
    )
    private List<Materiel> materiels = new ArrayList<>();

}