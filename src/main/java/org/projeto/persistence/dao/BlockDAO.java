package org.projeto.persistence.dao;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import static org.projeto.persistence.converter.OffsetDateTimeConverter.toTimestamp;


@AllArgsConstructor
public class BlockDAO {
    private final Connection connection;  // Conexão JDBC para acessar o banco

    // Método para bloquear um cartão, inserindo registro na tabela BLOCKS
    public void block(final String reason, final Long cardId) throws SQLException {
        var sql = "INSERT INTO BLOCKS (blocked_at, block_reason, card_id) VALUES (?, ?, ?);";
        try(var statement = connection.prepareStatement(sql)){
            var i = 1;
            // Seta o timestamp atual convertido para Timestamp JDBC
            statement.setTimestamp(i++, toTimestamp(OffsetDateTime.now()));
            // Seta o motivo do bloqueio
            statement.setString(i++, reason);
            // Seta o ID do cartão a ser bloqueado
            statement.setLong(i, cardId);

            // Executa a inserção no banco
            statement.executeUpdate();
        }
    }

    // Método para desbloquear um cartão, atualizando registro na tabela BLOCKS
    public void unblock(final String reason, final Long cardId) throws SQLException {
        var sql = "UPDATE BLOCKS SET unblocked_at = ?, unblock_reason = ? WHERE card_id = ? AND unblock_reason IS NULL;";
        try(var statement = connection.prepareStatement(sql)){
            var i = 1;
            // Seta o timestamp atual para o momento do desbloqueio
            statement.setTimestamp(i++, toTimestamp(OffsetDateTime.now()));
            // Seta o motivo do desbloqueio
            statement.setString(i++, reason);
            // Seta o ID do cartão a ser desbloqueado
            statement.setLong(i, cardId);

            // Executa a atualização no banco
            statement.executeUpdate();
        }
    }
}
