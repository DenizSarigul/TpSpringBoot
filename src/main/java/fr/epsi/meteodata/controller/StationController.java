package fr.epsi.meteodata.controller;

import fr.epsi.meteodata.model.Station;
import fr.epsi.meteodata.model.StatutStation;
import fr.epsi.meteodata.service.StationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
public class StationController {

    private final StationService service;

    public StationController(StationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Station> lister() { return service.lister(); }

    // GET /api/stations/recherche?ville=Lyon
    @GetMapping("/recherche")
    public List<Station> recherche(@RequestParam String ville) {
        return service.parVille(ville);
    }

    // GET /api/stations/par-statut?statut=ACTIVE
    @GetMapping("/par-statut")
    public List<Station> parStatut(@RequestParam StatutStation statut) {
        return service.parStatut(statut);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Station> lire(@PathVariable Long id) {
        return service.trouver(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Station> creer(@RequestBody Station s) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.enregistrer(s));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Station> modifier(@PathVariable Long id, @RequestBody Station s) {
        return service.modifier(id, s)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        return service.supprimer(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
