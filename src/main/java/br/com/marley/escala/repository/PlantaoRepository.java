package br.com.marley.escala.repository;

import br.com.marley.escala.entity.Plantao;
import br.com.marley.escala.entity.Profissional;
import br.com.marley.escala.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PlantaoRepository extends JpaRepository<Plantao, Long>
{
    boolean existsByProfissionalAndDataAndTurno(
            Profissional profissional,
            LocalDate data,
            Turno turno
    );

    List<Plantao> findByDataBetween(
            LocalDate inicio,
            LocalDate fim
    );

    List<Plantao> findByProfissionalAndDataBetween(
            Profissional profissional,
            LocalDate inicio,
            LocalDate fim
    );
}