package com.iuc.tpiuc.enums;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = """
                Etats possibles d'une réservation :

                EN_ATTENTE : Réservation créée par le professeur.

                VALIDEE : Réservation acceptée par le responsable.

                REFUSEE : Réservation refusée.

                EN_COURS : Le TP a commencé.

                TERMINEE : Le TP est terminé.

                EXPIREE : Le professeur ne s'est pas présenté.
                """
)
public enum ReservationStatus {

    EN_ATTENTE,   // Créée par le professeur
    VALIDEE,     //  Acceptée par le responsable
    REFUSEE,    //   Refusée
    EN_COURS,  //    Le TP a commencé
    TERMINEE,  //    Le TP est terminé
    EXPIREE    //    Le professeur ne s'est pas présenté

}
