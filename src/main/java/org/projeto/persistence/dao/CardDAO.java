package org.projeto.persistence.dao;

import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;
import org.projeto.dto.CardDetailsDTO;
import org.projeto.persistence.entity.CardEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static org.projeto.persistence.converter.OffsetDateTimeConverter.toOffsetDateTime;


@AllArgsConstructor
public class CardDAO {
    private Connection connection;  // conexão JDBC com o banco de dados

    // Insere um novo cartão no banco e atualiza o ID da entidade com o gerado
    public CardEntity insert(final CardEntity entity) throws SQLException {
        var sql = "INSERT INTO CARDS (title, description, board_column_id) values (?, ?, ?);";
        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setString(i++, entity.getTitle());                    // título do cartão
            statement.setString(i++, entity.getDescription());              // descrição do cartão
            statement.setLong(i, entity.getBoardsColumn().getId());          // ID da coluna onde o cartão está
            statement.executeUpdate();

            // Obtém o ID gerado automaticamente após a inserção (MySQL específico)
            if (statement instanceof StatementImpl impl) {
                entity.setId(impl.getLastInsertID());
            }
        }
        return entity;
    }

    // Move o cartão para outra coluna atualizando o board_column_id
    public void moveToColumn(final Long columnId, final Long cardId) throws SQLException {
        var sql = "UPDATE CARDS SET board_column_id = ? WHERE id = ?;";
        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setLong(i++, columnId);  // novo ID da coluna
            statement.setLong(i, cardId);      // ID do cartão a ser movido
            statement.executeUpdate();
        }
    }

    // Busca um cartão pelo ID retornando um DTO com detalhes do cartão e bloqueios
    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var sql =
                """
                SELECT c.id,
                       c.title,
                       c.description,
                       b.blocked_at,
                       b.block_reason,
                       c.board_column_id,
                       bc.name,
                       (SELECT COUNT(sub_b.id)
                          FROM BLOCKS sub_b
                         WHERE sub_b.card_id = c.id) blocks_amount
                  FROM CARDS c
                  LEFT JOIN BLOCKS b
                    ON c.id = b.card_id
                   AND b.unblocked_at IS NULL  -- considera apenas bloqueios ativos
                 INNER JOIN BOARDS_COLUMNS bc
                    ON bc.id = c.board_column_id
                 WHERE c.id = ?;
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();

            var resultSet = statement.getResultSet();

            if (resultSet.next()) {
                var dto = new CardDetailsDTO(
                        resultSet.getLong("c.id"),                              // ID do cartão
                        resultSet.getString("c.title"),                         // título
                        resultSet.getString("c.description"),                   // descrição
                        nonNull(resultSet.getString("b.block_reason")),        // bloqueado? true se houver motivo
                        toOffsetDateTime(resultSet.getTimestamp("b.blocked_at")), // data/hora do bloqueio
                        resultSet.getString("b.block_reason"),                  // motivo do bloqueio
                        resultSet.getInt("blocks_amount"),                      // total de bloqueios já feitos
                        resultSet.getLong("c.board_column_id"),                 // ID da coluna atual
                        resultSet.getString("bc.name")                           // nome da coluna atual
                );
                return Optional.of(dto);
            }
        }
        return Optional.empty();  // retorna vazio se não encontrou o cartão
    }
}
