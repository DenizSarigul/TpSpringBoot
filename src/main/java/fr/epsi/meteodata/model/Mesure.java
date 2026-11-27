package fr.epsi.meteodata.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mesure")
public class Mesure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateHeure;
    private Double temperature;
    private Double humidite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    protected Mesure() {} // exigé par JPA

    public Mesure(LocalDateTime dateHeure, Double temperature, Double humidite, Station station) {
        this.dateHeure = dateHeure;
        this.temperature = temperature;
        this.humidite = humidite;
        this.station = station;
    }

    public Long getId() { return id; }
    public LocalDateTime getDateHeure() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
    public Double getHumidite() { return humidite; }
    public void setHumidite(Double humidite) { this.humidite = humidite; }
    public Station getStation() { return station; }
    public void setStation(Station station) { this.station = station; }
}
