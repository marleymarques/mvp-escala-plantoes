package br.com.marley.escala.repository;

import br.com.marley.escala.entity.Categoria;
import br.com.marley.escala.entity.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long>
{
    List<Profissional> findByCategoria(Categoria categoria);

    Optional<Profissional> findByCrmCoren(String crmCoren);
}