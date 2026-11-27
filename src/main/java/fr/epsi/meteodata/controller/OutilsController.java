package fr.epsi.meteodata.controller;

import org.springframework.web.bind.annotation.*;

// Routes de l'étape 1, conservées à part
@RestController
@RequestMapping("/api")
public class OutilsController {

    @GetMapping("/ping")
    public String ping() { return "pong"; }

    @GetMapping("/convertir")
    public String convertir(@RequestParam double celsius) {
        return celsius + " °C = " + (celsius * 9 / 5 + 32) + " °F";
    }
}
