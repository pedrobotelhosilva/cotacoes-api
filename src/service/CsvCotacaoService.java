package service;

import model.Cotacao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvCotacaoService
{
  public void salvar(List<Cotacao> cotacoes, Path arquivoCsv) throws IOException
  {
    try (BufferedWriter writer = Files.newBufferedWriter(arquivoCsv, StandardCharsets.UTF_8))
    {
      writer.write("data,valor_compra,valor_venda");
      writer.newLine();

      for (Cotacao cotacao : cotacoes)
      {
        writer.write(cotacao.getData() + "," + cotacao.getValorCompra() + "," + cotacao.getValorVenda());
        writer.newLine();
      }
    }
    System.out.println("CSV salvo em:");
    System.out.println(arquivoCsv.toAbsolutePath());
    System.out.println();
  }

  public List<Cotacao> ler(Path arquivoCsv) throws IOException
  {
    List<Cotacao> cotacoes = new ArrayList<>();

    try (BufferedReader reader = Files.newBufferedReader(arquivoCsv, StandardCharsets.UTF_8))
    {
      reader.readLine();

      String  linha;

      while ((linha = reader.readLine()) != null)
      {
        Cotacao cotacao = this.converterLinhaEmCotacao(linha);

        cotacoes.add(cotacao);
      }
    }
    return (cotacoes);
  }

  private Cotacao converterLinhaEmCotacao(String linha)
  {
    String      partes[] = linha.split(",");
    LocalDate   data = LocalDate.parse(partes[0]);
    BigDecimal  valorCompra = new BigDecimal(partes[1]);
    BigDecimal  valorVenda = new BigDecimal(partes[2]);

    return (new Cotacao(data, valorCompra, valorVenda));
  }
}
