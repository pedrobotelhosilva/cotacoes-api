package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiCotacaoService
{
  private static final String BASE_API_URL = "https://economia.awesomeapi.com.br/json/daily/";

  public String buscarCotacoes(String moeda, int dias) throws Exception
  {
    String url = this.montarUrl(moeda, dias);
    HttpClient            client = HttpClient.newHttpClient();
    HttpRequest           request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    HttpResponse<String>  response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200)
    {
      throw new RuntimeException("Erro na API. Status: " + response.statusCode());
    }
    return (response.body());
  }

  private String montarUrl(String moeda, int dias)
  {
    return (BASE_API_URL + moeda + "/" + dias);
  }
}
