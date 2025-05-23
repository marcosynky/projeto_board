package org.projeto.persistence.migration;


import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;


import static org.projeto.persistence.config.ConnectionConfig.getConnection;

@AllArgsConstructor
// Gera construtor que recebe a conexão como parâmetro
public class MigrationStrategy {

    private final Connection connection;  // conexão com o banco de dados

    // Método que executa a migração do banco usando Liquibase
    public void executeMigration() {
        var originalOut = System.out;  // salva a saída padrão original
        var originalErr = System.err;  // salva a saída de erro original

        try (var fos = new FileOutputStream("liquibase.log")) {  // cria um arquivo para log da migração
            // Redireciona saída padrão e erro para o arquivo liquibase.log
            System.setOut(new PrintStream(fos));
            System.setErr(new PrintStream(fos));

            try (
                    var connection = getConnection();                   // abre conexão com o banco
                    var jdbcConnection = new JdbcConnection(connection); // cria conexão JDBC para Liquibase
            ) {
                var liquibase = new Liquibase(
                        "/db/changelog/changelog-master.yml",        // arquivo master do changelog do Liquibase
                        new ClassLoaderResourceAccessor(),               // acessa recursos via classloader
                        jdbcConnection                                   // conexão JDBC com o banco
                );

                liquibase.update();  // executa a atualização/migração do banco conforme o changelog

            } catch (SQLException | LiquibaseException e) {
                e.printStackTrace();    // imprime exceção caso algo falhe na migração
                System.setErr(originalErr);  // restaura saída de erro original para garantir visibilidade
            }

        } catch (IOException ex) {
            ex.printStackTrace();  // trata erros na criação ou escrita do arquivo de log
        } finally {
            // Restaura saída padrão e de erro para os originais, garantindo ambiente limpo
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
    }
}
