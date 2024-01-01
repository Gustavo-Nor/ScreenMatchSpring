package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {


//        System.out.println("Digite o noome da série para busca: ");
//        var nomeSerie = leitura.nextLine();
//        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ","+") + API_KEY);
//
//        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
//        System.out.println(dados);
//
//        List<DadosTemporada> temporadas = new ArrayList<>();
//
//		for (int i = 1; i <= dados.totalTemporadas(); i++){
//			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ","+") + "&Season=" + i + API_KEY);
//			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
//			temporadas.add(dadosTemporada);
//		}
//		temporadas.forEach(System.out::println);
//
////        for (int i = 0; i < dados.totalTemporadas(); i++){
////            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
////
////            for (int j = 0; j < episodiosTemporada.size(); j++){
////                System.out.println(episodiosTemporada.get(j).titulo());
////            }
////        }
//
//        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
//
////        List<String> nomes = Arrays.asList("Jacque", "Iasmin", "Paulo", "Rodrigo", "Nico");
////        nomes.stream()
////                .sorted()
////                .limit(3)
////                .filter(n -> n.startsWith("N"))
////                .map(n -> n.toUpperCase())
////                .forEach(System.out::println);
//
//        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream())
//                .collect(Collectors.toList());
//
////        System.out.println("\n Top 10 episódios: ");
////        dadosEpisodios.stream()
////                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
////                .peek(e -> System.out.println("Primeiro filtro(N/A) " + e))
////                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
////                .peek(e -> System.out.println("Ordenação " + e))
////                .limit(10)
////                .peek(e -> System.out.println("Limite" + e))
////                .map(e -> e.titulo().toUpperCase())
////                .peek(e -> System.out.println("Mapeamento " + e))
////                .forEach(System.out::println);
////
//        List<Episodio> episodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream()
//                        .map(d -> new Episodio(t.numero(), d)))
//                .collect(Collectors.toList());
//
//        episodios.forEach(System.out::println);
//
////        System.out.println("Digite um trecho do titulo do episódio: ");
////        var trechoTitulo = leitura.nextLine();
////        Optional<Episodio> episodioBuscado = episodios.stream()
////                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
////                .findFirst();
////
////        if (episodioBuscado.isPresent()){
////            System.out.println("Episódio encontrado!");
////            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
////        } else {
////            System.out.println("Episódio não encontrado!");
////        }
////
////        System.out.println("A partir de que ano voce deseja ver os episódios? ");
////        var ano = leitura.nextInt();
////        leitura.nextLine();
////
////        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
////
////        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
////        episodios.stream()
////                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
////                .forEach(e -> System.out.println(
////                        "Temporada: " + e.getTemporada() +
////                                " Episódio: " + e.getTitulo() +
////                                " Data de lançamento: " + e.getDataLancamento().format(formatador)
////                ));
//
//        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
//                .filter(e -> e.getAvaliacao() > 0.0)
//                .collect(Collectors.groupingBy(Episodio::getTemporada,
//                        Collectors.averagingDouble(Episodio::getAvaliacao)));
//
//        System.out.println(avaliacoesPorTemporada);
//
//        DoubleSummaryStatistics est = episodios.stream()
//                .filter(e -> e.getAvaliacao() > 0.0)
//                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
//
//        System.out.println("Média: " + est.getAverage());
//        System.out.println("Pior episódio: " + est.getMin());
//        System.out.println("Melhor episódio: " + est.getMax());
//        System.out.println("Quantidade de avaliações: " + est.getCount());

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=850dc857";
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepository repositorio;
    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    private List<Serie> series = new ArrayList<>();

    private Optional<Serie> serieBusca;


    public void exibeMenu() {
        var opcao = -1;

        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por título
                    5 - Buscar séries por ator
                    6 - Buscar top 5 séries
                    7 - Buscar séries por categoria
                    8 - Buscar séries por número máximo de temporadas e avaliação mínima
                    9 - Buscar episódio por trecho
                    10 - Buscar top 5 episódios
                                    
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    buscarSeriesPorNumeroTemporadasEAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSeries.add(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {

            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void listarSeriesBuscadas(){

        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();
        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()){
            System.out.println("Dados da série: " + serieBusca.get());
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Qual o nome do ator para busca: ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliação mínima: ");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
        seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie> seriesTop5 = repositorio.findTop5ByOrderByAvaliacaoDesc();
        System.out.println("Top 5 séries: ");
        seriesTop5.forEach(s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Deseja buscar séries de qual categoria/genero? ");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries da categoria " + nomeGenero + " : ");
        seriesPorCategoria.forEach(s -> System.out.println(s.getTitulo()));
    }

    private void buscarSeriesPorNumeroTemporadasEAvaliacao() {
        System.out.println("Deseja buscar séries com até quantas temporadas: ");
        var numeroMaximoTemporadas = leitura.nextInt();
        System.out.println("Qual a avaliação mínima das séries: ");
        var avaliacaoMinima = leitura.nextDouble();
//        List<Serie> seriePorNumeroTemporadasEAvaliacao = repositorio.findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(numeroMaximoTemporadas, avaliacaoMinima);
        List<Serie> seriePorNumeroTemporadasEAvaliacao = repositorio.seriesPorTemporadaEAvaliacao(numeroMaximoTemporadas, avaliacaoMinima);
        System.out.println("Lista de séries com até " + numeroMaximoTemporadas + " temporadas e avaliação mínima de " + avaliacaoMinima);
        seriePorNumeroTemporadasEAvaliacao.forEach(s -> System.out.println(s.getTitulo() + " Total de temporadas: " +
                s.getTotalTemporadas() + " e avaliação: " + s.getAvaliacao()));
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Qual o nome do episódio para busca? ");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e -> System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()));
    }

    private void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e -> System.out.printf("Série: %s Temporada %s - Episódio %s Avaliação - %s\n",
                    e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getAvaliacao(), e.getTitulo()));
        }
    }
}