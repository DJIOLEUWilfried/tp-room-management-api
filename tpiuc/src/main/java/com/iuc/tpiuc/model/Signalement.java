package com.iuc.tpiuc.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @CreationTimestamp
    private LocalDateTime dateSignalement;

    @ManyToOne
    @JoinColumn(name = "id_createur")
    private Utilisateur createur;

    @ManyToOne
    @JoinColumn(name = "id_materiel")
    private Materiel materiel;

}