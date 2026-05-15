package util;

import model.Cotacao;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class JsonCotacaoParser
{
  public List<Cotacao> parse(String json)
  {
    List<Cotacao> cotacoes = new ArrayList<>();

    json = json.replace("[", "").replace("]", "");

    String  objetos[] = json.split("\\},\\{");

    for (String objeto : objetos)
    {
      objeto = objeto.replace("{", "").replace("}", "");

      String  timestamp = this.extrairValor(objeto, "timestamp");
      String  compra = this.extrairValor(objeto, "bid");
      String  venda = this.extrairValor(objeto, "ask");

      if (timestamp != null && compra != null && venda != null)
      {
        LocalDate data = Instant.ofEpochSecond(Long.parseLong(timestamp)).atZone(ZoneId.systemDefault()).toLocalDate();
        Cotacao   cotacao = new Cotacao(data, new BigDecimal(compra), new BigDecimal(venda));
        
        cotacoes.add(cotacao);
      }
    }
    return (cotacoes);
  }

  private String extrairValor(String objeto, String chave)
  {
    String busca = "\"" + chave + "\":\"";

    int inicio = objeto.indexOf(busca);

    if (inicio == -1)
    {
      return (null);
    }
    inicio += busca.length();

    int fim = objeto.indexOf("\"", inicio);

    if (fim == -1)
    {
      return (null);
    }

    return (objeto.substring(inicio, fim));
  }
}
