# MeteoData API

Atelier **Java & Spring Boot** (FRWE518, EPSI), bachelor DevOps Full Stack.

Des stations météo envoient des mesures. Certaines sont erronées (capteur en panne, valeur -999...). L'API les **valide**, les **stocke**, **détecte et supprime les anomalies** et fournit des **statistiques** à un tableau de bord.

**Auteur :** Deniz Sarigul

## Technologies

- Java 17
- Spring Boot 4.0.0 (Spring Web MVC, Spring Data JPA, Validation, DevTools)
- H2 en mémoire
- Maven

## Avancement

| Partie | Contenu |
|--------|---------|
| TP 1 étape 1 | Premières routes (`ping`, `convertir`) |
| TP 1 étape 2 | CRUD en couches, injection par constructeur, codes HTTP |
| TP 1 étape 3 | JPA / H2, entité `Station`, enum `StatutStation`, recherche par ville et par statut |
| TP 2 | Entité `Mesure` (`@ManyToOne`), DTO, validation, erreurs centralisées, règles métier, nettoyage, statistiques |

## Lancer le projet

Prérequis : JDK 17 et Maven (ou IntelliJ IDEA).

```bash
git clone https://github.com/DenizSarigul/TpSpringBoot.git
cd TpSpringBoot
mvn spring-boot:run
```

Avec IntelliJ : ouvrir le dossier contenant `pom.xml`, attendre le chargement Maven puis lancer `MeteodataApplication`.

API : `http://localhost:8080`

Console H2 : `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:meteodb`, utilisateur `sa`, mot de passe vide).

Au démarrage, `data.sql` insère 4 stations et 11 mesures dont **5 anomalies volontaires** (valeur -999, 300 °C, température absente, humidité 150 %, doublon) pour pouvoir tester le nettoyage.

## Endpoints

### Stations

| Verbe | URL | Rôle | Succès |
|-------|-----|------|--------|
| GET | `/api/stations` | Lister les stations | 200 |
| GET | `/api/stations/{id}` | Lire une station | 200 / 404 |
| POST | `/api/stations` | Créer une station | 201 / 400 |
| PUT | `/api/stations/{id}` | Modifier une station | 200 / 400 / 404 |
| DELETE | `/api/stations/{id}` | Supprimer une station | 204 / 404 / 409 si elle a des mesures |
| GET | `/api/stations/recherche?ville=Lyon` | Recherche par ville | 200 |
| GET | `/api/stations/par-statut?statut=ACTIVE` | Filtre par statut | 200 / 400 |

### Mesures, anomalies et statistiques

| Verbe | URL | Rôle | Succès |
|-------|-----|------|--------|
| POST | `/api/stations/{id}/mesures` | Ajouter une mesure | 201 / 400 / 404 / 409 |
| GET | `/api/stations/{id}/mesures` | Mesures d'une station | 200 / 404 |
| GET | `/api/mesures/anomalies` | Lister les anomalies | 200 |
| DELETE | `/api/mesures/anomalies` | Nettoyer la base | 200 (`{"supprimees": n}`) |
| GET | `/api/stations/{id}/stats` | Statistiques d'une station | 200 / 404 |

### Utilitaires

`GET /api/ping` renvoie `pong` et `GET /api/convertir?celsius=20` renvoie la température en °F.

## Exemples

Ajouter une mesure (`POST /api/stations/1/mesures`) :

```json
{ "dateHeure": "2026-11-27T10:00:00", "temperature": 14.2, "humidite": 61.0 }
```

Statistiques (`GET /api/stations/2/stats`) :

```json
{ "station": "Station Chamonix", "nbMesures": 3, "tempMoyenne": 0.8, "tempMin": -3.2, "tempMax": 4.0 }
```

Erreur de validation (400) :

```json
{ "temperature": "La température doit être inférieure ou égale à 60" }
```

## Règles métier et validation

- Une mesure est **refusée (409)** si la station n'est pas `ACTIVE` (méthode `accepteMesures()` de l'enum `StatutStation`).
- Une mesure est **refusée (409)** si une mesure existe déjà pour la même station à la même date.
- Validation des entrées (400) : date et température obligatoires, température entre -90 et 60, humidité entre 0 et 100, nom de station non vide.
- Une **anomalie** est une mesure avec température absente, hors plage (dont la valeur sentinelle -999), humidité hors plage, ou doublon (même station, même date).
- Les **statistiques** (nombre, moyenne, min, max) sont calculées par la base, uniquement sur les mesures valides.

Les erreurs sont renvoyées en JSON propre, sans stack trace, par `GestionErreurs` (`@RestControllerAdvice`) :

| Cas | Code |
|-----|------|
| Ressource introuvable | 404 |
| Données invalides, JSON mal formé, paramètre inconnu (`?statut=CASSEE`) | 400 |
| Règle métier non respectée, doublon, station avec mesures liées | 409 |

## Tester

- **Postman** : importer `MeteoData.postman_collection.json` (variable `baseUrl = http://localhost:8080`), puis lancer la collection dans l'ordre avec le Collection Runner sur une base fraîche (redémarrer l'application avant). Chaque requête vérifie son code de statut.
- **IntelliJ** : le fichier `requetes.http` contient les requêtes principales.

## Architecture

```
Client -> Controller -> Service -> Repository -> Base H2
```

Aucune entité n'est exposée : le contrôleur échange uniquement des **DTO** (`records`) avec le client, convertis par des mappers.

```
src/main/java/fr/epsi/meteodata/
├── MeteodataApplication.java
├── controller/   StationController, MesureController, OutilsController
├── service/      StationService, MesureService
├── repository/   StationRepository, MesureRepository
├── model/        Station, Mesure (entités JPA), StatutStation (enum)
├── dto/          StationRequest/Response, MesureRequest/Response, StatsStation, mappers
└── exception/    RessourceIntrouvableException, RegleMetierException, GestionErreurs
src/main/resources/
├── application.properties
└── data.sql
```

Relation : une `Station` possède plusieurs `Mesure` (`@ManyToOne` côté `Mesure`, clé étrangère `station_id`).
