package service;

import model.Cotacao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

public class RelatorioService
{
  public String gerar(List<Cotacao> cotacoes, String moeda, int dias)
  {
    Cotacao       menorCompra = cotacoes.stream().min(Comparator.comparing(Cotacao::getValorCompra)).get();
    Cotacao       maiorCompra = cotacoes.stream().max(Comparator.comparing(Cotacao::getValorCompra)).get();
    Cotacao       menorVenda = cotacoes.stream().min(Comparator.comparing(Cotacao::getValorVenda)).get();
    Cotacao       maiorVenda = cotacoes.stream().max(Comparator.comparing(Cotacao::getValorVenda)).get();
    BigDecimal    mediaCompra = this.calcularMediaCompra(cotacoes);
    BigDecimal    mediaVenda = this.calcularMediaVenda(cotacoes);
    StringBuilder texto = new StringBuilder();

    texto.append("RELATÓRIO DE COTAÇÕES").append(System.lineSeparator());
    texto.append(System.lineSeparator());
    texto.append("Moeda: ").append(moeda).append(System.lineSeparator());
    texto.append("Dias solicitados: ").append(dias).append(System.lineSeparator());
    texto.append("Registros encontrados: ").append(cotacoes.size()).append(System.lineSeparator());
    texto.append(System.lineSeparator());
    texto.append("Menor compra: ").append(menorCompra.getValorCompra()).append(" em ").append(menorCompra.getData()).append(System.lineSeparator());
    texto.append("Maior compra: ").append(maiorCompra.getValorCompra()).append(" em ").append(maiorCompra.getData()).append(System.lineSeparator());
    texto.append("Menor venda: ").append(menorVenda.getValorVenda()).append(" em ").append(menorVenda.getData()).append(System.lineSeparator());
    texto.append("Maior venda: ").append(maiorVenda.getValorVenda()).append(" em ").append(maiorVenda.getData()).append(System.lineSeparator());
    texto.append(System.lineSeparator());
    texto.append("Média compra: ").append(mediaCompra).append(System.lineSeparator());
    texto.append("Média venda: ").append(mediaVenda).append(System.lineSeparator());

    return (texto.toString());
  }

  private BigDecimal calcularMediaCompra(List<Cotacao> cotacoes)
  {
    BigDecimal soma = cotacoes.stream().map(Cotacao::getValorCompra).reduce(BigDecimal.ZERO, BigDecimal::add);

    return (soma.divide(BigDecimal.valueOf(cotacoes.size()), 4, RoundingMode.HALF_UP));
  }

  private BigDecimal calcularMediaVenda(List<Cotacao> cotacoes)
  {
    BigDecimal soma = cotacoes.stream().map(Cotacao::getValorVenda).reduce(BigDecimal.ZERO, BigDecimal::add);

    return (soma.divide(BigDecimal.valueOf(cotacoes.size()), 4, RoundingMode.HALF_UP));
  }
}
