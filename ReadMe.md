# MeteoData API : TP 1 (API Stations)

Atelier **Java & Spring Boot** (FRWE518, EPSI), bachelor DevOps Full Stack.
Le fil rouge est MeteoData : des stations météo envoient des mesures, l'API les stocke, les nettoie et expose des statistiques.
Ce dépôt contient le **TP 1**, une API REST de gestion de stations météo, construite en trois étapes.

**Auteur :** Deniz Sarigul

## Technologies

- Java 17
- Spring Boot 4.0.0 (Spring Web MVC, Spring Data JPA, Validation, DevTools)
- Base de données H2 en mémoire
- Maven

## Ce qui a été réalisé

| Étape | Contenu |
|-------|---------|
| 1 | Premières routes : `ping`, `stations/{id}`, `convertir` |
| 2 | CRUD complet en couches (contrôleur / service), injection par constructeur, bons codes HTTP |
| 3 | Persistance JPA avec H2, entité `Station`, `StationRepository`, enum `StatutStation` stocké en `STRING`, recherche par ville et par statut |

## Lancer le projet

Prérequis : JDK 17 et Maven (ou IntelliJ IDEA).

```bash
git clone https://github.com/DenizSarigul/TpSpringBoot.git
cd TpSpringBoot
mvn spring-boot:run
```

Avec IntelliJ : ouvrir le dossier du projet (celui qui contient `pom.xml`), attendre le chargement Maven, puis lancer `MeteodataApplication`.

L'API est disponible sur `http://localhost:8080`.

## Console H2

- URL : `http://localhost:8080/h2-console`
- JDBC URL : `jdbc:h2:mem:meteodb`
- Utilisateur : `sa`
- Mot de passe : *(vide)*

Quatre stations sont insérées au démarrage via `data.sql`.

## Endpoints

### Utilitaires

| Verbe | URL | Réponse |
|-------|-----|---------|
| GET | `/api/ping` | `pong` |
| GET | `/api/convertir?celsius=20` | `20.0 °C = 68.0 °F` |

### Stations

| Verbe | URL | Rôle | Succès |
|-------|-----|------|--------|
| GET | `/api/stations` | Lister les stations | 200 |
| GET | `/api/stations/{id}` | Lire une station | 200 (404 si absente) |
| POST | `/api/stations` | Créer une station | 201 |
| PUT | `/api/stations/{id}` | Modifier une station | 200 (404 si absente) |
| DELETE | `/api/stations/{id}` | Supprimer une station | 204 (404 si absente) |
| GET | `/api/stations/recherche?ville=Lyon` | Rechercher par ville (insensible à la casse) | 200 |
| GET | `/api/stations/par-statut?statut=ACTIVE` | Filtrer par statut | 200 (400 si statut inconnu) |

Statuts possibles : `ACTIVE`, `MAINTENANCE`, `HORS_SERVICE`.

### Exemple de corps JSON (POST / PUT)

```json
{
  "nom": "Station Nice",
  "ville": "Nice",
  "altitude": 10,
  "statut": "ACTIVE"
}
```

## Tester l'API

- **Postman** : collection « MeteoData » avec la variable `baseUrl = http://localhost:8080`. Pour les requêtes POST et PUT, utiliser Body > raw > JSON.
- **IntelliJ** : le fichier `stations.http` contient toutes les requêtes de test.

## Structure du projet

```
meteodata/
├── pom.xml
├── stations.http
└── src/main/
    ├── java/fr/epsi/meteodata/
    │   ├── MeteodataApplication.java
    │   ├── controller/     StationController, OutilsController
    │   ├── service/        StationService
    │   ├── repository/     StationRepository
    │   └── model/          Station (entité JPA), StatutStation (enum)
    └── resources/
        ├── application.properties
        └── data.sql
```

## Architecture

```
Client (Postman, front) -> Controller -> Service -> Repository -> Base H2
```

- Le **contrôleur** reçoit la requête HTTP et renvoie la réponse.
- Le **service** porte la logique applicative.
- Le **repository** (Spring Data JPA) lit et écrit en base.
