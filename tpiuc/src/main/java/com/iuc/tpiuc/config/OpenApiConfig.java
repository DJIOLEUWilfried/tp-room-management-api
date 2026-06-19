package com.iuc.tpiuc.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI customOpenAPI1() {

        return new OpenAPI()
                .info(new Info()
                        .title("API Gestion des Salles de TP et du Matériel Didactique")
                        .version("1.0")
                        .description("""
                                API de gestion des salles de travaux pratiques,
                                du matériel didactique et des réservations.
                                
                                Rôles disponibles :
                                
                                • PROFESSEUR
                                  - Réserver une salle
                                  - Réserver un matériel
                                  - Démarrer un cours
                                  - Valider le cahier de texte
                                  - Créer un signalement
                                  - Consulter son profil
                                  - Modifier son profil
                                
                                • RESPONSABLE
                                  - Gérer les salles
                                  - Gérer les matériels
                                  - Gérer les utilisateurs
                                  - Valider ou refuser les réservations
                                  - Consulter son profil
                                  - Modifier son profil
                                  - Consulter les logs d'audits
                                """)
                        .contact(
                                new Contact()
                                        .name("DJIOLEU Wilfried")
                                        .email("djioleuwilfried@gmail.com")
                        )
                )
                .addServersItem(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Serveur local")
                );

    }


}