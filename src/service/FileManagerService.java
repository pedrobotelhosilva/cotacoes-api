package service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.stream.Stream;

public class FileManagerService
{
  private static final Path BASE_DIR = Path.of("dados-cotacoes");

  public Path criarEstruturaDePastas(String moeda) throws IOException
  {
    Path diretorioExecucao = BASE_DIR.resolve(moeda.toLowerCase()).resolve(LocalDate.now().toString()).normalize();

    Files.createDirectories(diretorioExecucao.resolve("bruto"));
    Files.createDirectories(diretorioExecucao.resolve("csv"));
    Files.createDirectories(diretorioExecucao.resolve("relatorios"));
    Files.createDirectories(diretorioExecucao.resolve("backup"));

    System.out.println("Estrutura de pastas criada em:");
    System.out.println(diretorioExecucao.toAbsolutePath());
    System.out.println();
    return (diretorioExecucao);
  }

  public void salvarTexto(Path arquivo, String conteudo) throws IOException
  {
    Files.writeString(arquivo, conteudo, StandardCharsets.UTF_8);

    System.out.println("Arquivo salvo em:");
    System.out.println(arquivo.toAbsolutePath());
    System.out.println();
  }

  public void criarBackup(Path origem, Path destino) throws IOException
  {
    Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);

    System.out.println("Backup criado em:");
    System.out.println(destino.toAbsolutePath());
    System.out.println();
  }

  public void listarArquivosGerados(Path diretorio) throws IOException
  {
    System.out.println("Arquivos gerados:");

    try (Stream<Path> paths = Files.walk(diretorio))
    {
      paths.filter(Files::isRegularFile).forEach(path -> System.out.println("- " + diretorio.relativize(path)));
    }

    System.out.println();
  }
}
