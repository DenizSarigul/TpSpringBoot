package fr.epsi.meteodata.model;

import jakarta.persistence.*;

@Entity
@Table(name = "station")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    private String ville;
    private Integer altitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutStation statut = StatutStation.ACTIVE;

    protected Station() {} // exigé par JPA

    public Station(String nom, String ville, Integer altitude, StatutStation statut) {
        this.nom = nom;
        this.ville = ville;
        this.altitude = altitude;
        if (statut != null) this.statut = statut;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    public Integer getAltitude() { return altitude; }
    public void setAltitude(Integer altitude) { this.altitude = altitude; }
    public StatutStation getStatut() { return statut; }
    public void setStatut(StatutStation statut) { this.statut = statut; }
}
