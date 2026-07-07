package com.iuc.tpiuc.enums;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Etat du matériel"
)
public enum MaterielEtat {

    DISPONIBLE,
    EN_PANNE,
    OCCUPE;

}
