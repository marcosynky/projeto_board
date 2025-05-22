package org.projeto;

// Importa a classe responsável por executar as migrações no banco de dados
import org.projeto.persistence.migration.MigrationStrategy;

// Importa método para obter conexão com o banco
import static org.projeto.persistence.config.ConnectionConfig.getConnection;

// Classe principal da aplicação
public class Main {

    // Método principal que será executado ao iniciar a aplicação
    public static void main(String[] args) throws Exception {

        // Obtém uma conexão com o banco de dados, usando try-with-resources para fechar automaticamente
        try (var connection = getConnection()) {

            // Cria uma instância da estratégia de migração e executa as migrações pendentes no banco
            new MigrationStrategy(connection).executeMigration();
        }
    }
}
