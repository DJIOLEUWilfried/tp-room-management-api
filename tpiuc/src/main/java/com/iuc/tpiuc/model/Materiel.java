package com.iuc.tpiuc.model;


import com.iuc.tpiuc.enums.MaterielEtat;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @ManyToMany(mappedBy = "materiels", fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "materiel", fetch = FetchType.LAZY)
    private List<Signalement> signalements = new ArrayList<>();

}