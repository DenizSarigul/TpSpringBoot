INSERT INTO station (nom, ville, altitude, statut) VALUES ('Station Lyon-Bron', 'Lyon', 200, 'ACTIVE');
INSERT INTO station (nom, ville, altitude, statut) VALUES ('Station Chamonix', 'Chamonix', 1035, 'ACTIVE');
INSERT INTO station (nom, ville, altitude, statut) VALUES ('Station Paris-Montsouris', 'Paris', 75, 'MAINTENANCE');
INSERT INTO station (nom, ville, altitude, statut) VALUES ('Station Brest', 'Brest', 50, 'HORS_SERVICE');

-- Mesures valides (Lyon, id 1)
INSERT INTO mesure (date_heure, temperature, humidite, station_id) VALUES ('2026-11-20 08:00:00', 12.5, 65.0, 1);
INSERT INTO mesure (date_heure, temperature, humidite, station_id) VALUES ('2026-11-20 09:00:00', 13.1, 63.0, 1);
INSERT INTO mesure (date_heure, temperature, humidite, station_id) VALUES ('2026-11-20 10:00:00', 14.0, 60.0, 1);
-- Anomalies (Lyon) : sentinelle, hors plage, valeur absente, humidité impossible, doublon
INSERT INTO mesure (date_heure, temperature, humidite, station_id) VALUES ('2026-11-20 11:00:00', -999, 50.0, 1);
INSERT INTO mesure (date_heure, temperature, humidite, station_id) VALUES ('2026-11-20 12:00:00', 300, 40.0, 1);
INSERT INTO mesure (date_heure, temperature, humidite, station_id) VALUES ('2026-11-20 13:00:00', NULL, 55.0, 1);
INSERT INTO mesure (date_heure, temperature, humidite, station_id) VALUES ('2026-11-20 14:00:00', 15.2, 150.0, 1);
INSERT INTO mesure (date_heure, temperature, humidite, station_id) VALUES ('2026-11-20 08:00:00', 12.5, 65.0, 1);

-- Mesures valides (Chamonix, id 2)
INSERT INTO mesure (date_heure, temperature, humidite, station_id) VALUES ('2026-11-20 08:00:00', -3.2, 80.0, 2);
INSERT INTO mesure (date_heure, temperature, humidite, station_id) VALUES ('2026-11-20 12:00:00', 1.5, 75.0, 2);
INSERT INTO mesure (date_heure, temperature, humidite, station_id) VALUES ('2026-11-20 16:00:00', 4.0, 70.0, 2);
