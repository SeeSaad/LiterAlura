package br.com.alura.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Integer dataNascimento;
    private Integer dataMorte;

    public Autor() {}

    public Autor(DadosAutor d) {
        nome = d.nome();
        dataNascimento = d.dataNascimento();
        dataMorte = d.dataMorte();
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nome +
                "\nnascimento : " + dataNascimento +
                "\nmorte : " + dataMorte;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getDataNascimento() {
        return dataNascimento;
    }

    public Integer getDataMorte() {
        return dataMorte;
    }
}
