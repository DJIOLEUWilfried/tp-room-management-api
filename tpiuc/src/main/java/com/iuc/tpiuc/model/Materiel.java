package com.iuc.tpiuc.model;


import com.iuc.tpiuc.enums.MaterielEtat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
//@SQLDelete(sql = "UPDATE materiel SET deleted = true WHERE id=?")
//@SQLRestriction("deleted = false")
public class Materiel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private Integer quantite;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "L'état est obligatoire")
    @Column(length = 200)
    private MaterielEtat etat;

//    @Builder.Default
//    @Column(nullable = false)
//    private boolean  deleted = false;

    @ManyToMany(mappedBy = "materiels", fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "materiel", fetch = FetchType.LAZY)
    private List<Signalement> signalements = new ArrayList<>();

}