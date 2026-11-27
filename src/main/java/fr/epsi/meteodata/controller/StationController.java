package fr.epsi.meteodata.controller;

import fr.epsi.meteodata.dto.StationMapper;
import fr.epsi.meteodata.dto.StationRequest;
import fr.epsi.meteodata.dto.StationResponse;
import fr.epsi.meteodata.exception.RessourceIntrouvableException;
import fr.epsi.meteodata.model.Station;
import fr.epsi.meteodata.model.StatutStation;
import fr.epsi.meteodata.service.StationService;
import jakarta.validation.Valid;
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

    private RessourceIntrouvableException introuvable(Long id) {
        return new RessourceIntrouvableException("Station " + id + " introuvable");
    }

    @GetMapping
    public List<StationResponse> lister() {
        return service.lister().stream().map(StationMapper::toResponse).toList();
    }

    @GetMapping("/recherche")
    public List<StationResponse> recherche(@RequestParam String ville) {
        return service.parVille(ville).stream().map(StationMapper::toResponse).toList();
    }

    @GetMapping("/par-statut")
    public List<StationResponse> parStatut(@RequestParam StatutStation statut) {
        return service.parStatut(statut).stream().map(StationMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public StationResponse lire(@PathVariable Long id) {
        return service.trouver(id)
                .map(StationMapper::toResponse)
                .orElseThrow(() -> introuvable(id));
    }

    @PostMapping
    public ResponseEntity<StationResponse> creer(@Valid @RequestBody StationRequest r) {
        Station creee = service.enregistrer(StationMapper.toEntity(r));
        return ResponseEntity.status(HttpStatus.CREATED).body(StationMapper.toResponse(creee));
    }

    @PutMapping("/{id}")
    public StationResponse modifier(@PathVariable Long id, @Valid @RequestBody StationRequest r) {
        return service.modifier(id, StationMapper.toEntity(r))
                .map(StationMapper::toResponse)
                .orElseThrow(() -> introuvable(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        if (!service.supprimer(id)) {
            throw introuvable(id);
        }
        return ResponseEntity.noContent().build();
    }
}
