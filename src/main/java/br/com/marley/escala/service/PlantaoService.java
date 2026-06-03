package br.com.marley.escala.service;

import br.com.marley.escala.entity.Plantao;
import br.com.marley.escala.entity.Profissional;
import br.com.marley.escala.enums.Turno;
import br.com.marley.escala.exception.RegraNegocioException;
import br.com.marley.escala.repository.PlantaoRepository;
import br.com.marley.escala.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class PlantaoService
{
    private final PlantaoRepository repository;
    private final ProfissionalRepository profissionalRepository;

    public PlantaoService(
            PlantaoRepository repository,
            ProfissionalRepository profissionalRepository)
    {
        this.repository = repository;
        this.profissionalRepository = profissionalRepository;
    }

    public Plantao salvar(Plantao plantao)
    {
        Profissional profissional = profissionalRepository.findById(
                plantao.getProfissional().getId()
        ).orElseThrow(() -> new RuntimeException("Profissional não encontrado."));

        plantao.setProfissional(profissional);

        validarPlantaoDuplicado(plantao);
        validarCargaHorariaSemanal(plantao);

        return repository.save(plantao);
    }


    public void excluir(Long id)
    {
        if (!repository.existsById(id))
        {
            throw new RuntimeException("Plantão não encontrado.");
        }

        repository.deleteById(id);
    }

    private void validarPlantaoDuplicado(Plantao plantao)
    {
        boolean existe = repository.existsByProfissionalAndDataAndTurno(
                plantao.getProfissional(),
                plantao.getData(),
                plantao.getTurno()
        );

        if (existe)
        {
            throw new RegraNegocioException(
                    "Profissional já escalado neste turno."
            );
        }
    }

    private void validarCargaHorariaSemanal(Plantao plantao)
    {
        Profissional profissional = plantao.getProfissional();

        LocalDate inicioSemana = plantao.getData().with(DayOfWeek.MONDAY);
        LocalDate fimSemana = plantao.getData().with(DayOfWeek.SUNDAY);

        List<Plantao> plantoes = repository.findByProfissionalAndDataBetween(
                profissional,
                inicioSemana,
                fimSemana
        );

        int horasSemana = plantoes.stream()
                .mapToInt(p -> horasTurno(p.getTurno()))
                .sum();

        horasSemana += horasTurno(plantao.getTurno());

        if (horasSemana > profissional.getCargaHorariaSemanal())
        {
            throw new RegraNegocioException(
                    "Carga horária semanal excedida."
            );
        }
    }

    private int horasTurno(Turno turno)
    {
        return switch (turno)
        {
            case MANHA -> 6;
            case TARDE -> 6;
            case NOITE -> 12;
        };
    }
}