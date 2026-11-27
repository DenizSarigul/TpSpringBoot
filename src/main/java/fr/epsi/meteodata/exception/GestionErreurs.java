package fr.epsi.meteodata.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GestionErreurs {

    // 404 : ressource absente
    @ExceptionHandler(RessourceIntrouvableException.class)
    public ResponseEntity<Map<String, String>> introuvable(RessourceIntrouvableException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erreur", e.getMessage()));
    }

    // 409 : règle métier non respectée (station non ACTIVE, doublon...)
    @ExceptionHandler(RegleMetierException.class)
    public ResponseEntity<Map<String, String>> regleMetier(RegleMetierException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erreur", e.getMessage()));
    }

    // 400 : données du corps invalides (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> invalide(MethodArgumentNotValidException e) {
        Map<String, String> erreurs = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(f -> erreurs.put(f.getField(), f.getDefaultMessage()));
        return ResponseEntity.badRequest().body(erreurs);
    }

    // 400 : JSON mal formé ou valeur d'enum inconnue dans le corps
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> corpsIllisible(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("erreur", "Corps de requête invalide (JSON mal formé ou valeur non reconnue)"));
    }

    // 400 : paramètre d'URL invalide (ex. ?statut=CASSEE)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> typeInvalide(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("erreur", "Valeur invalide pour le paramètre '" + e.getName() + "'"));
    }

    // 409 : contrainte en base (ex. supprimer une station qui a des mesures)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> integrite(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("erreur", "Opération impossible : des données liées existent (ex. mesures de la station)"));
    }
}
