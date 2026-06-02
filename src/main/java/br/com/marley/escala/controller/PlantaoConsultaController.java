package br.com.marley.escala.controller;

import br.com.marley.escala.dto.GradeSemanalResponseDTO;
import br.com.marley.escala.service.GradeSemanalService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/plantoes")
@CrossOrigin(origins = "*")
public class PlantaoConsultaController {

    private final GradeSemanalService gradeSemanalService;

    public PlantaoConsultaController(GradeSemanalService gradeSemanalService) {
        this.gradeSemanalService = gradeSemanalService;
    }

    @GetMapping("/semana")
    public GradeSemanalResponseDTO consultarSemana(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataInicial
    ) {
        return gradeSemanalService.consultarSemana(dataInicial);
    }
}

