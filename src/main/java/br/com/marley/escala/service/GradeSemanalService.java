package br.com.marley.escala.service;

import br.com.marley.escala.dto.GradeSemanalResponseDTO;
import br.com.marley.escala.dto.ProfissionalGradeDTO;
import br.com.marley.escala.enums.Turno;
import br.com.marley.escala.entity.Plantao;
import br.com.marley.escala.entity.Profissional;
import br.com.marley.escala.repository.PlantaoRepository;
import br.com.marley.escala.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class GradeSemanalService {

    private final PlantaoRepository plantaoRepository;
    private final ProfissionalRepository profissionalRepository;

    public GradeSemanalService(
            PlantaoRepository plantaoRepository,
            ProfissionalRepository profissionalRepository
    ) {
        this.plantaoRepository = plantaoRepository;
        this.profissionalRepository = profissionalRepository;
    }

    public GradeSemanalResponseDTO consultarSemana(LocalDate dataInicial) {
        LocalDate dataFinal = dataInicial.plusDays(6);

        List<LocalDate> diasSemana = dataInicial
                .datesUntil(dataFinal.plusDays(1))
                .toList();

        List<Profissional> profissionais = profissionalRepository.findAll();
        List<Plantao> plantoes = plantaoRepository.findByDataBetween(dataInicial, dataFinal);

        Map<Long, List<Plantao>> plantoesPorProfissional = plantoes.stream()
                .collect(Collectors.groupingBy(p -> p.getProfissional().getId()));

        List<ProfissionalGradeDTO> linhas = profissionais.stream()
                .map(profissional -> montarLinha(profissional, diasSemana, plantoesPorProfissional))
                .toList();

        return new GradeSemanalResponseDTO(
                dataInicial,
                dataFinal,
                diasSemana,
                linhas
        );
    }

    private ProfissionalGradeDTO montarLinha(
            Profissional profissional,
            List<LocalDate> diasSemana,
            Map<Long, List<Plantao>> plantoesPorProfissional
    ) {
        List<Plantao> plantoes = plantoesPorProfissional
                .getOrDefault(profissional.getId(), List.of());

        Map<LocalDate, Turno> dias = new LinkedHashMap<>();

        for (LocalDate dia : diasSemana) {
            Turno turno = plantoes.stream()
                    .filter(p -> p.getData().equals(dia))
                    .map(Plantao::getTurno)
                    .findFirst()
                    .orElse(null);

            dias.put(dia, turno);
        }

        int horasAlocadas = plantoes.stream()
                .mapToInt(p -> p.getTurno().getHoras())
                .sum();

        boolean limiteAtingido = horasAlocadas >= profissional.getCargaHorariaSemanal();

        return new ProfissionalGradeDTO(
                profissional.getId(),
                profissional.getNome(),
                profissional.getCategoria(),
                profissional.getCargaHorariaSemanal(),
                horasAlocadas,
                limiteAtingido,
                dias
        );
    }
}