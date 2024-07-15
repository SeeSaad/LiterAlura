package br.com.alura.LiterAlura.main;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.LiterAlura.model.Autor;
import br.com.alura.LiterAlura.model.DadosGuntexSearch;
import br.com.alura.LiterAlura.model.DadosLivro;
import br.com.alura.LiterAlura.model.Livro;
import br.com.alura.LiterAlura.repository.AutorRepository;
import br.com.alura.LiterAlura.repository.LivroRepository;
import br.com.alura.LiterAlura.service.ConsumoApi;
import br.com.alura.LiterAlura.service.ConverteDados;
import br.com.alura.LiterAlura.service.GuntexUrlConverter;


public class Principal {
    private final Scanner scanner = new Scanner(System.in);
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConverteDados converteDados = new ConverteDados();

    private final LivroRepository repositorioLivros;
    private final AutorRepository repositorioAutores;
    private List<Livro> listaDeLivros;
    private List<Autor> listaDeAutores;

    public Principal(LivroRepository repositorioLivros, AutorRepository repositorioAutores) {
        this.repositorioLivros = repositorioLivros;
        this.repositorioAutores = repositorioAutores;
    }

    private void waitForInput() {
        System.out.print("[Enter] para continuar : ");
        scanner.nextLine();
    }

    public void menu() {
        int opcao = -1;

        while(opcao != 0) {
            listaDeLivros = repositorioLivros.findAll();
            listaDeAutores = repositorioAutores.findAll();
            System.out.println("""
                    ============================
                    1 - Buscar livro pelo titulo
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    
                    0 - Sair
                    ============================""");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine();
            } else scanner.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivro();
                    waitForInput();
                    break;
                case 2:
                    listaDeLivros.forEach(System.out::println);
                    waitForInput();
                    break;
                case 3:
                    listaDeAutores.forEach(System.out::println);
                    waitForInput();
                    break;
                case 4:
                    autoresVivosEmUmAno();
                    waitForInput();
                    break;
                case 5:
                    livrosDeterminadoIdioma();
                    waitForInput();
                    break;
                case 0:
                    break;
            }
        }
    }

    private void livrosDeterminadoIdioma() {
        System.out.println("digite o idioma: (ex: en, pt)");
        String idioma = scanner.nextLine().toLowerCase();

        List<Livro> listaEncontrada = listaDeLivros.stream()
                .filter(l -> l.getIdiomas().toLowerCase().contains(idioma))
                .toList();

        if (listaEncontrada.isEmpty()) {
            System.out.println("Nenhum livro encontrado");
            return;
        }

        listaEncontrada.forEach(System.out::println);
    }

    private void autoresVivosEmUmAno() {
        System.out.print("Digite o ano em que o autor estava vivo : ");
        if (!scanner.hasNextInt()) {
            System.out.println("ano invalido");
            return;
        }
        int ano = scanner.nextInt();

        if (listaDeAutores.stream()
                .filter(a -> a.getDataNascimento() != null && a.getDataMorte() != null)
                .filter(a -> a.getDataNascimento() <= ano && a.getDataMorte() >= ano)
                .count() == 0)
        {
            System.out.println("nenum autor encontrado");
        } else {
            listaDeAutores.stream()
                    .filter(a -> a.getDataNascimento() != null && a.getDataMorte() != null)
                    .filter(a -> a.getDataNascimento() <= ano && a.getDataMorte() >= ano)
                    .forEach(System.out::println);
        }
    }

    private void buscarLivro() {
        System.out.print("Digite o nome do livro : ");
        String nome = scanner.nextLine();

        String json = consumoApi.obterDados(GuntexUrlConverter.searchBookURL(nome));
        DadosGuntexSearch guntexSearch = converteDados.obterDados(json, DadosGuntexSearch.class);

        switch (guntexSearch.resultadosList().size()) {
            case 0:
                System.out.println("Nenhum resultado encontrado");
                break;
            case 1:
                System.out.println("Encontrado 1 resultado:");
                guntexSearch.resultadosList().forEach(System.out::println);
                break;
            default:
                int i = 0;
                for (DadosLivro livro : guntexSearch.resultadosList()) {
                    System.out.println("[" + i + "]\n" + livro);
                    i++;
                }
                System.out.println("Mais de 1 resultado encontrado, selecione o numero:");
                if (scanner.hasNextInt()) {
                    int escolha = scanner.nextInt();
                    scanner.nextLine();

                    if (escolha < guntexSearch.resultadosList().size() && escolha > -1) {
                        save(guntexSearch.resultadosList().get(escolha));
                    }
                }
        }
    }

    private void save(DadosLivro dadosLivro) {
        Livro livro = new Livro(dadosLivro);
        List<Autor> autores = dadosLivro.dadosAutorList().stream()
                .map(Autor::new)
                .toList();
        repositorioLivros.save(livro);
        autores.forEach(repositorioAutores::save);
    }

}
