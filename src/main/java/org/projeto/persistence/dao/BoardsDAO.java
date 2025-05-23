package org.projeto.persistence.dao;


import com.mysql.cj.jdbc.StatementImpl;
import lombok.AllArgsConstructor;
import org.projeto.persistence.entity.BoardsEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardsDAO {

    private final Connection connection;

    // Insere um novo quadro (board) no banco e atualiza o ID da entidade com o gerado
    public BoardsEntity insert(final BoardsEntity entity) throws SQLException {
        var sql = "INSERT INTO BOARDS (name) values (?);";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getName());  // seta o nome do quadro
            statement.executeUpdate();

            // Obtém o ID gerado automaticamente (MySQL específico)
            if (statement instanceof StatementImpl impl) {
                entity.setId(impl.getLastInsertID());
            }
        }
        return entity;
    }

    // Remove um quadro pelo seu ID
    public void delete(final Long id) throws SQLException {
        var sql = "DELETE FROM BOARDS WHERE id = ?;";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);  // seta o ID do quadro para deletar
            statement.executeUpdate();

        }
    }

    // Busca um quadro pelo ID e retorna Optional com a entidade, ou Optional vazio se não existir
    public Optional<BoardsEntity> findById(final Long id) throws SQLException {
        var sql = "SELECT id, name FROM BOARDS WHERE id = ?;";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();

            var resultSet = statement.getResultSet();

            if (resultSet.next()) {
                var entity = new BoardsEntity();
                entity.setId(resultSet.getLong("id"));       // obtém o ID
                entity.setName(resultSet.getString("name")); // obtém o nome
                return Optional.of(entity);
            }
            return Optional.empty();  // não encontrou o quadro
        }
    }


    // Verifica se um quadro existe pelo seu ID (retorna true se existir, false caso contrário)
    public boolean exists(final Long id) throws SQLException {
        var sql = "SELECT 1 FROM BOARDS WHERE id = ?;";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeQuery();

            // Se houver algum resultado, significa que o quadro existe
            return statement.getResultSet().next();
        }
    }

}