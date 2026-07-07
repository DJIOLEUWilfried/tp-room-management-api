package com.iuc.tpiuc.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = """
                Rôles disponibles dans le système.

                PROFESSEUR :
                Peut effectuer des réservations
                et signaler des matériels.

                RESPONSABLE :
                Peut gérer les salles,
                les matériels et valider
                les réservations.
                """
)
public enum Role {

    PROFESSEUR,
    RESPONSABLE

}
