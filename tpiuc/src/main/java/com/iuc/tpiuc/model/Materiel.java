package com.iuc.tpiuc.model;


import com.iuc.tpiuc.enums.MaterielEtat;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "materiels")
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

    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;
}