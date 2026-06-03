package br.com.marley.escala.controller;

import br.com.marley.escala.entity.Categoria;
import br.com.marley.escala.entity.Profissional;
import br.com.marley.escala.service.ProfissionalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalController
{
    private final ProfissionalService service;

    public ProfissionalController(ProfissionalService service)
    {
        this.service = service;
    }

    @PostMapping
    public Profissional salvar(@RequestBody Profissional profissional)
    {
        return service.salvar(profissional);
    }

    @GetMapping
    public List<Profissional> listar(
            @RequestParam(required = false) Categoria categoria)
    {
        if (categoria != null)
        {
            return service.listarPorCategoria(categoria);
        }

        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Profissional buscar(@PathVariable Long id)
    {
        return service.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id)
    {
        service.excluir(id);
    }
}