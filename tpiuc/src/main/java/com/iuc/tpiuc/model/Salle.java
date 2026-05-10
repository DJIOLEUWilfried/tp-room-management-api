package com.iuc.tpiuc.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
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
    private String localisation;

    @OneToMany(mappedBy = "salle")
    private List<Reservation> reservations;


}