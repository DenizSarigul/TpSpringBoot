package fr.epsi.meteodata.controller;

import fr.epsi.meteodata.dto.MesureRequest;
import fr.epsi.meteodata.dto.MesureResponse;
import fr.epsi.meteodata.dto.StatsStation;
import fr.epsi.meteodata.service.MesureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MesureController {

    private final MesureService service;

    public MesureController(MesureService service) {
        this.service = service;
    }

    @PostMapping("/stations/{id}/mesures")
    public ResponseEntity<MesureResponse> ajouter(@PathVariable Long id,
                                                  @Valid @RequestBody MesureRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.ajouter(id, r));
    }

    @GetMapping("/stations/{id}/mesures")
    public List<MesureResponse> mesures(@PathVariable Long id) {
        return service.listerParStation(id);
    }

    @GetMapping("/mesures/anomalies")
    public List<MesureResponse> anomalies() {
        return service.anomalies();
    }

    @DeleteMapping("/mesures/anomalies")
    public Map<String, Integer> nettoyer() {
        return Map.of("supprimees", service.nettoyer());
    }

    @GetMapping("/stations/{id}/stats")
    public StatsStation stats(@PathVariable Long id) {
        return service.stats(id);
    }
}
