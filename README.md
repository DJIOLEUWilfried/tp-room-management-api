# 📚 API REST - Gestion des Salles de TP et du Matériel Didactique

Une API REST sécurisée développée avec **Java Spring Boot** permettant de gérer les salles de travaux pratiques, le matériel didactique, les réservations et les signalements au sein d'un établissement d'enseignement.

L'objectif est de centraliser la gestion des ressources pédagogiques, d'éviter les conflits de réservation et d'assurer une meilleure traçabilité des opérations.

---

## 🚀 Fonctionnalités

### 👥 Gestion des utilisateurs
- Authentification sécurisée avec JWT
- Gestion des rôles et des permissions

### 🏫 Gestion des salles
- Création d'une salle
- Modification
- Suppression
- Consultation
- Recherche

### 💻 Gestion du matériel didactique
- Ajout d'un matériel
- Modification
- Suppression
- Consultation
- Gestion de l'état du matériel

### 📅 Gestion des réservations
- Création d'une réservation
- Validation ou refus
- Vérification des conflits de réservation
- Consultation de l'historique

### ⚠️ Gestion des signalements
- Déclaration d'un incident
- Suivi des signalements
- Consultation des historiques

### 📋 Audit
- Traçabilité des principales actions des utilisateurs

### 📖 Documentation
- Documentation interactive avec Swagger/OpenAPI

---

# 🛠️ Technologies utilisées

| Technologie | Version |
|-------------|----------|
| Java | 21 |
| Spring Boot | 3.x |
| Spring Security | ✔ |
| JWT | ✔ |
| Spring Data JPA | ✔ |
| Hibernate | ✔ |
| PostgreSQL | ✔ |
| Maven | ✔ |
| Lombok | ✔ |
| Swagger / OpenAPI | ✔ |
| JUnit 5 | ✔ |
| Mockito | ✔ |
| Postman | ✔ |
| Git | ✔ |
| GitHub | ✔ |

---

# 🏗️ Architecture

Le projet suit une architecture en couches afin de garantir la maintenabilité et l'évolutivité.

```
Controller
      │
      ▼
Service
      │
      ▼
Repository
      │
      ▼
PostgreSQL
```

Le projet utilise également :

- DTO
- Mapper
- Validation
- Exception Personnalisée
- JWT Authentication
- Spring Security

---

# 📂 Structure du projet

```
# 📂 Structure du projet

```text
src
├── main
│   ├── java
│   │   ├── audit
│   │   ├── auth
│   │   ├── config
│   │   ├── controller
│   │   ├── dto
│   │   ├── enums
│   │   ├── exception
│   │   ├── mapper
│   │   ├── model
│   │   ├── repository
│   │   ├── security
│   │   ├── service
│   │   └── utils
│   │
│   └── resources
│       
│
└── test
    
```
```

---

# 🔐 Sécurité

L'API est sécurisée grâce à :

- JWT Authentication
- Spring Security
- Gestion des rôles
- Endpoints protégés
- Chiffrement des mots de passe avec BCrypt

---

# 📖 Documentation API

Après le démarrage de l'application :

```
http://localhost:8080/swagger-ui/index.html
```

---

# ⚙️ Installation

## 1. Cloner le projet

```bash
git clone 
```

---

## 2. Accéder au projet

```bash
cd tpiuc
```

---

## 3. Configurer PostgreSQL

Créer une base de données.

Configurer ensuite le fichier :

```
application.yaml
```

---

## 4. Compiler le projet

```bash
mvn clean install
```

---

## 5. Lancer l'application

```bash
mvn spring-boot:run
```

---

# 🧪 Tests

Le projet comprend :

- Tests unitaires avec **JUnit 5**
- Mock des dépendances avec **Mockito**
- Tests des endpoints REST avec **Postman**

---

# 📈 Compétences mises en œuvre

- Développement d'API REST
- Spring Boot
- Spring Security
- JWT
- PostgreSQL
- Hibernate
- JPA
- Tests unitaires
- Validation
- Gestion des exceptions
- Architecture logicielle
- Git
- GitHub

---

# 👨‍💻 Auteur

**Wilfried DJIOLEU**

Développeur Logiciel Java & .NET

- Java
- Spring Boot
- C#
- .NET
- PostgreSQL
- MySQL

LinkedIn : https://www.linkedin.com/in/wilfried-djioleu-594542321/

---

# 📄 Licence

Ce projet est distribué à des fins éducatives et de démonstration.

---

⭐ Si ce projet vous a été utile, n'hésitez pas à laisser une étoile sur GitHub !
