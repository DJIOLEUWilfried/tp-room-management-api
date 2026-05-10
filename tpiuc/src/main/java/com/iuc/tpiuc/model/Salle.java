package com.iuc.tpiuc.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "salles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private Integer capacite;

    private Boolean disponible;

    @OneToMany(mappedBy = "salle")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "salle")
    private List<Materiel> materiels;
}