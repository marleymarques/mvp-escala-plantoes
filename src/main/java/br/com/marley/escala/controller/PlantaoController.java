package br.com.marley.escala.controller;

import br.com.marley.escala.entity.Plantao;
import br.com.marley.escala.service.PlantaoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plantoes")
public class PlantaoController
{
    private final PlantaoService service;

    public PlantaoController(PlantaoService service)
    {
        this.service = service;
    }

    @PostMapping
    public Plantao salvar(@RequestBody Plantao plantao)
    {
        return service.salvar(plantao);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id)
    {
        service.excluir(id);
    }
}