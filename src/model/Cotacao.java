package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Cotacao
{
  private final LocalDate   data;
  private final BigDecimal  valorCompra;
  private final BigDecimal  valorVenda;

  public Cotacao(LocalDate data, BigDecimal valorCompra, BigDecimal valorVenda)
  {
    this.data = data;
    this.valorCompra = valorCompra;
    this.valorVenda = valorVenda;
  }

  public LocalDate getData()
  {
    return (this.data);
  }

  public BigDecimal getValorCompra()
  {
    return (this.valorCompra);
  }

  public BigDecimal getValorVenda()
  {
    return (this.valorVenda);
  }
}
