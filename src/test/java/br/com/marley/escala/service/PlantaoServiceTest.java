package br.com.marley.escala.service;

import br.com.marley.escala.entity.Categoria;
import br.com.marley.escala.entity.Plantao;
import br.com.marley.escala.entity.Profissional;
import br.com.marley.escala.enums.Turno;
import br.com.marley.escala.exception.RegraNegocioException;
import br.com.marley.escala.repository.PlantaoRepository;
import br.com.marley.escala.repository.ProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlantaoServiceTest {

    @Mock
    private PlantaoRepository plantaoRepository;

    @Mock
    private ProfissionalRepository profissionalRepository;

    private PlantaoService service;
    private Profissional profissional;

    @BeforeEach
    void setUp() {
        service = new PlantaoService(plantaoRepository, profissionalRepository);

        profissional = new Profissional();
        profissional.setId(1L);
        profissional.setNome("Dra. Ana Souza");
        profissional.setCrmCoren("CRM12345");
        profissional.setCategoria(Categoria.MEDICO);
        profissional.setCargaHorariaSemanal(20);
    }

    @Test
    void deveBloquearPlantaoDuplicadoNoMesmoDiaETurno() {
        Plantao novoPlantao = criarPlantao(LocalDate.of(2026, 6, 2), Turno.MANHA);

        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(plantaoRepository.existsByProfissionalAndDataAndTurno(
                profissional,
                novoPlantao.getData(),
                novoPlantao.getTurno()
        )).thenReturn(true);

        RegraNegocioException exception = assertThrows(
                RegraNegocioException.class,
                () -> service.salvar(novoPlantao)
        );

        assertEquals("Profissional já escalado neste turno.", exception.getMessage());
        verify(plantaoRepository, never()).save(any(Plantao.class));
    }

    @Test
    void deveBloquearQuandoCargaHorariaSemanalForExcedida() {
        Plantao novoPlantao = criarPlantao(LocalDate.of(2026, 6, 2), Turno.NOITE);

        Plantao plantaoExistente1 = criarPlantao(LocalDate.of(2026, 6, 1), Turno.MANHA);
        Plantao plantaoExistente2 = criarPlantao(LocalDate.of(2026, 6, 3), Turno.MANHA);

        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(plantaoRepository.existsByProfissionalAndDataAndTurno(
                profissional,
                novoPlantao.getData(),
                novoPlantao.getTurno()
        )).thenReturn(false);
        when(plantaoRepository.findByProfissionalAndDataBetween(
                eq(profissional),
                eq(LocalDate.of(2026, 6, 1)),
                eq(LocalDate.of(2026, 6, 7))
        )).thenReturn(List.of(plantaoExistente1, plantaoExistente2));

        RegraNegocioException exception = assertThrows(
                RegraNegocioException.class,
                () -> service.salvar(novoPlantao)
        );

        assertEquals("Carga horária semanal excedida.", exception.getMessage());
        verify(plantaoRepository, never()).save(any(Plantao.class));
    }

    @Test
    void deveSalvarQuandoNaoHouverDuplicidadeNemExcessoDeCarga() {
        Plantao novoPlantao = criarPlantao(LocalDate.of(2026, 6, 2), Turno.TARDE);

        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(plantaoRepository.existsByProfissionalAndDataAndTurno(
                profissional,
                novoPlantao.getData(),
                novoPlantao.getTurno()
        )).thenReturn(false);
        when(plantaoRepository.findByProfissionalAndDataBetween(
                eq(profissional),
                eq(LocalDate.of(2026, 6, 1)),
                eq(LocalDate.of(2026, 6, 7))
        )).thenReturn(List.of());
        when(plantaoRepository.save(novoPlantao)).thenReturn(novoPlantao);

        Plantao salvo = service.salvar(novoPlantao);

        assertEquals(novoPlantao, salvo);
        verify(plantaoRepository).save(novoPlantao);
    }

    private Plantao criarPlantao(LocalDate data, Turno turno) {
        Plantao plantao = new Plantao();
        plantao.setData(data);
        plantao.setTurno(turno);
        plantao.setProfissional(profissional);
        return plantao;
    }
}
