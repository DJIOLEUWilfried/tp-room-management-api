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
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private String entite;
    private Long entiteId;
    private LocalDateTime dateAction;

    @ManyToOne
    private Utilisateur utilisateur;

}