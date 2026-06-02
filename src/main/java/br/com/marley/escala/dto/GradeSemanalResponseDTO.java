package br.com.marley.escala.dto;

import java.time.LocalDate;
import java.util.List;

public record GradeSemanalResponseDTO(
        LocalDate dataInicial,
        LocalDate dataFinal,
        List<LocalDate> dias,
        List<ProfissionalGradeDTO> profissionais
) {
}