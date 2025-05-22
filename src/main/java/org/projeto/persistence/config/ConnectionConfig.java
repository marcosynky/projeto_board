package org.projeto.persistence.config;

import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
// Impede instanciação da classe, pois é utilitária
public final class ConnectionConfig {

    // Obtém conexão JDBC com o banco MySQL usando variáveis de ambiente
    public static Connection getConnection() throws SQLException {
        var url = System.getenv("DB_URL");        // URL do banco configurada na variável de ambiente
        var user = System.getenv("DB_USER");      // Usuário do banco configurado na variável de ambiente
        var password = System.getenv("DB_PASSWORD");  // Senha do banco configurada na variável de ambiente

        var connection = DriverManager.getConnection(url, user, password); // Cria conexão JDBC
        connection.setAutoCommit(false);  // Desliga auto-commit para controlar transações manualmente
        return connection;                // Retorna conexão para uso no projeto
    }

}
