package fr.epsi.meteodata.repository;

import fr.epsi.meteodata.dto.StatsStation;
import fr.epsi.meteodata.model.Mesure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MesureRepository extends JpaRepository<Mesure, Long> {

    List<Mesure> findByStationIdOrderByDateHeureAsc(Long stationId);

    boolean existsByStationIdAndDateHeure(Long stationId, LocalDateTime dateHeure);

    // Anomalies : valeur absente, sentinelle (-999), hors plage physique, doublon
    @Query("""
            SELECT m FROM Mesure m
            WHERE m.temperature IS NULL
               OR m.temperature < -90 OR m.temperature > 60
               OR m.humidite < 0 OR m.humidite > 100
               OR EXISTS (SELECT 1 FROM Mesure m2
                          WHERE m2.station.id = m.station.id
                            AND m2.dateHeure = m.dateHeure
                            AND m2.id < m.id)
            ORDER BY m.id
            """)
    List<Mesure> trouverAnomalies();

    // Statistiques calculées par la base, uniquement sur les mesures valides
    @Query("""
            SELECT new fr.epsi.meteodata.dto.StatsStation(
                s.nom, COUNT(m), AVG(m.temperature),
                MIN(m.temperature), MAX(m.temperature))
            FROM Mesure m JOIN m.station s
            WHERE s.id = :id
              AND m.temperature BETWEEN -90 AND 60
            GROUP BY s.nom
            """)
    Optional<StatsStation> statsParStation(@Param("id") Long id);
}
