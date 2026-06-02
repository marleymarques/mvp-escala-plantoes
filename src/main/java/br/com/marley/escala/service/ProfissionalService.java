package br.com.marley.escala.service;

import br.com.marley.escala.entity.Categoria;
import br.com.marley.escala.entity.Profissional;
import br.com.marley.escala.repository.ProfissionalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfissionalService
{
    private final ProfissionalRepository repository;

    public ProfissionalService(ProfissionalRepository repository)
    {
        this.repository = repository;
    }

    public Profissional salvar(Profissional profissional)
    {
        repository.findByCrmCoren(profissional.getCrmCoren())
                .ifPresent(p -> {
                    throw new RuntimeException("CRM/COREN já cadastrado.");
                });

        return repository.save(profissional);
    }

    public List<Profissional> listarTodos()
    {
        return repository.findAll();
    }

    public List<Profissional> listarPorCategoria(Categoria categoria)
    {
        return repository.findByCategoria(categoria);
    }

    public Profissional buscarPorId(Long id)
    {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado."));
    }

    public void excluir(Long id)
    {
        repository.deleteById(id);
    }
}