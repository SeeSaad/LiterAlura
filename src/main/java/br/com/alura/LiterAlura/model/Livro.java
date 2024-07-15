package br.com.alura.LiterAlura.model;

import jakarta.persistence.*;

import java.util.stream.Collectors;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String titulo;
    String idiomas;
    String autores;

    public Livro() {}

    public Livro(DadosLivro dadosLivro) {
        titulo = dadosLivro.titulo();
        idiomas = String.join(" ", dadosLivro.idiomas());
        autores = String.join(" | ", dadosLivro.dadosAutorList().stream().map(DadosAutor::nome).toList());
    }

    @Override
    public String toString() {
        return "[" + id + "] " + titulo + "\n" + idiomas + "\n" + autores;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public String getAutores() {
        return autores;
    }
}
