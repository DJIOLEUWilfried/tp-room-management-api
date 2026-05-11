package com.iuc.tpiuc.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

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
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nom;

    private Integer capacite;
    private Boolean disponible;
    private String localisation;

    private Boolean deleted = false;

    @OneToMany(mappedBy = "salle", fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();


}