package br.com.marley.escala.dto;

import br.com.marley.escala.entity.Categoria;
import br.com.marley.escala.enums.Turno;

import java.time.LocalDate;
import java.util.Map;

public record ProfissionalGradeDTO(
        Long profissionalId,
        String nome,
        Categoria categoria,
        Integer cargaHorariaSemanal,
        Integer horasAlocadas,
        Boolean limiteAtingido,
        Map<LocalDate, Turno> dias
) {
}