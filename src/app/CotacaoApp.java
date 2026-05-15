package app;

import model.Cotacao;
import service.ApiCotacaoService;
import service.CsvCotacaoService;
import service.FileManagerService;
import service.RelatorioService;
import util.JsonCotacaoParser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CotacaoApp
{
  private final ApiCotacaoService   apiCotacaoService;
  private final JsonCotacaoParser   jsonCotacaoParser;
  private final CsvCotacaoService   csvCotacaoService;
  private final RelatorioService    relatorioService;
  private final FileManagerService  fileManagerService;

  public CotacaoApp()
  {
    this.apiCotacaoService = new ApiCotacaoService();
    this.jsonCotacaoParser = new JsonCotacaoParser();
    this.csvCotacaoService = new CsvCotacaoService();
    this.relatorioService = new RelatorioService();
    this.fileManagerService = new FileManagerService();
  }

  public void run(String args[])
  {
    List<String>  moedas = this.pegarMoedas(args);
    int           dias = this.pegarDias(args);

    System.out.println("Iniciando aplicação...");
    System.out.println("Cotações: " + moedas);
    System.out.println("Dias: " + dias);
    System.out.println();

    for (String moeda : moedas)
    {
      this.executarComTratamento(moeda, dias);
    }
  }

  private void executarComTratamento(String moeda, int dias)
  {
    try
    {
      this.processarCotacao(moeda, dias);
    }
    catch (Exception e)
    {
      System.out.println("Erro ao processar " + moeda + ": " + e.getMessage());
      System.out.println();
    }
  }

  private void processarCotacao(String moeda, int dias) throws Exception
  {
    System.out.println("Processando cotação: " + moeda);

    Path    diretorioExecucao = this.fileManagerService.criarEstruturaDePastas(moeda);
    String  json = this.apiCotacaoService.buscarCotacoes(moeda, dias);
    Path    arquivoJson = diretorioExecucao.resolve("bruto").resolve("cotacoes.json");

    this.fileManagerService.salvarTexto(arquivoJson, json);

    List<Cotacao> cotacoes = this.jsonCotacaoParser.parse(json);
    Path          arquivoCsv = diretorioExecucao.resolve("csv").resolve("cotacoes.csv");

    this.csvCotacaoService.salvar(cotacoes, arquivoCsv);

    List<Cotacao> cotacoesLidas = this.csvCotacaoService.ler(arquivoCsv);
    String        relatorio = this.relatorioService.gerar(cotacoesLidas, moeda, dias);
    Path          arquivoRelatorio = diretorioExecucao.resolve("relatorios").resolve("relatorio.txt");

    this.fileManagerService.salvarTexto(arquivoRelatorio, relatorio);

    Path arquivoBackup = diretorioExecucao.resolve("backup").resolve("backup_cotacoes.csv");

    this.fileManagerService.criarBackup(arquivoCsv, arquivoBackup);
    this.fileManagerService.listarArquivosGerados(diretorioExecucao);
    System.out.println("Finalizado: " + moeda);
    System.out.println();
  }

  private List<String> pegarMoedas(String args[])
  {
    List<String> moedas = new ArrayList<>();

    for (String arg : args)
    {
      if (!this.ehNumero(arg))
      {
        moedas.add(arg.toUpperCase());
      }
    }

    if (moedas.isEmpty())
    {
      moedas.add("USD-BRL");
    }

    return (moedas);
  }

  private int pegarDias(String args[])
  {
    for (String arg : args)
    {
      if (this.ehNumero(arg))
      {
        return (Integer.parseInt(arg));
      }
    }

    return (30);
  }

  private boolean ehNumero(String texto)
  {
    try
    {
      Integer.parseInt(texto);
      return (true);
    }
    catch (NumberFormatException e)
    {
      return (false);
    }
  }
}
