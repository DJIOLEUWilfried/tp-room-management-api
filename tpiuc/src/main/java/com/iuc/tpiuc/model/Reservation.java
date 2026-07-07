package com.iuc.tpiuc.model;


import com.iuc.tpiuc.enums.ReservationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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

    @CreationTimestamp
    private LocalDateTime dateCreation;

    private LocalDate dateCours;

    private LocalTime heureDebut;

    private LocalTime heureFin;

    @Enumerated(EnumType.STRING)
    @Column(length = 200)
    private ReservationStatus status;

    /**
     * Le professeur a réellement effectué le cours
     */
    private Boolean coursEffectue = false;

    /**
     * Le cahier de texte a été rempli
     */
    private Boolean cahierTexteValide = false;

    /**
     * Date réelle de démarrage
     */
    private LocalDateTime debutReel;

    /**
     * Date réelle de fin
     */
    private LocalDateTime finReelle;

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