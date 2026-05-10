package com.iuc.tpiuc.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Signalement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private LocalDateTime dateSignalement;

    @ManyToOne
    private Utilisateur createur;

    @ManyToOne
    private Materiel materiel;
}