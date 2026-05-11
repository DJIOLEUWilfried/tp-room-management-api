package com.iuc.tpiuc.model;


import com.iuc.tpiuc.enums.MaterielEtat;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Materiel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private Integer quantite;

    @Enumerated(EnumType.STRING)
    private MaterielEtat etat;

    @ManyToMany(mappedBy = "materiels")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "materiel")
    private List<Signalement> signalements;

}