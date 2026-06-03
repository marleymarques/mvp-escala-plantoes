package br.com.marley.escala.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "profissional")
@Data
public class Profissional
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String crmCoren;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private Integer cargaHorariaSemanal;
}