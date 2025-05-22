package org.projeto.persistence.dao;

import com.mysql.cj.jdbc.StatementImpl;
import lombok.RequiredArgsConstructor;
import org.projeto.dto.BoardsColumnDTO;
import org.projeto.persistence.entity.BoardsColumnsEntity;
import org.projeto.persistence.entity.BoardColumnKindEnum;
import org.projeto.persistence.entity.CardEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;


@RequiredArgsConstructor
public class BoardsColumnDAO {

    private final Connection connection; // conexão com o banco para todas operações

    // Insere uma nova coluna no quadro e atualiza o ID da entidade com o gerado pelo banco
    public BoardsColumnsEntity insert(final BoardsColumnsEntity entity) throws SQLException {
        var sql = "INSERT INTO BOARDS_COLUMNS (name, `order`, kind, board_id) VALUES (?, ?, ?, ?);";
        try (var statement = connection.prepareStatement(sql)) {
            var i = 1;
            statement.setString(i++, entity.getName());          // Nome da coluna
            statement.setInt(i++, entity.getOrder());            // Ordem (posição)
            statement.setString(i++, entity.getKind().name());   // Tipo da coluna (enum convertido para String)
            statement.setLong(i, entity.getBoards().getId());     // ID do quadro ao qual pertence

            statement.executeUpdate();

            // Obtém o ID gerado automaticamente após a inserção (MySQL específico)
            if (statement instanceof StatementImpl impl) {
                entity.setId(impl.getLastInsertID());
            }
            return entity;
        }
    }

    // Busca todas as colunas de um quadro pelo ID do quadro
    public List<BoardsColumnsEntity> findByBoardId(final Long boardId) throws SQLException {
        List<BoardsColumnsEntity> entities = new ArrayList<>();
        var sql = "SELECT id, name, `order`, kind FROM BOARDS_COLUMNS WHERE board_id = ? ORDER BY `order`";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            statement.executeQuery();
            var resultSet = statement.getResultSet();

            while (resultSet.next()) {
                var entity = new BoardsColumnsEntity();
                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                entity.setOrder(resultSet.getInt("order"));
                entity.setKind(BoardColumnKindEnum.findByName(resultSet.getString("kind")));
                entities.add(entity);
            }
            return entities;
        }
    }

    // Busca as colunas de um quadro com detalhes, incluindo a quantidade de cartões em cada coluna
    public List<BoardsColumnDTO> findByBoardIdWithDetails(final Long boardId) throws SQLException {
        List<BoardsColumnDTO> dtos = new ArrayList<>();
        var sql =
                """
                SELECT bc.id,
                       bc.name,
                       bc.kind,
                       (SELECT COUNT(c.id)
                          FROM CARDS c
                         WHERE c.board_column_id = bc.id) cards_amount
                  FROM BOARDS_COLUMNS bc
                 WHERE board_id = ?
                 ORDER BY `order`;
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            statement.executeQuery();
            var resultSet = statement.getResultSet();

            while (resultSet.next()) {
                var dto = new BoardsColumnDTO(
                        resultSet.getLong("bc.id"),       // ID da coluna
                        resultSet.getString("bc.name"),   // Nome da coluna
                        BoardColumnKindEnum.findByName(resultSet.getString("bc.kind")), // Tipo da coluna (enum)
                        resultSet.getInt("cards_amount")  // Quantidade de cartões na coluna
                );
                dtos.add(dto);
            }
            return dtos;
        }
    }

    // Busca uma coluna pelo seu ID e também traz os cartões associados a ela (se houver)
    public Optional<BoardsColumnsEntity> findById(final Long boardId) throws SQLException {
        var sql =
                """
                SELECT bc.name,
                       bc.kind,
                       c.id,
                       c.title,
                       c.description
                  FROM BOARDS_COLUMNS bc
                  LEFT JOIN CARDS c
                    ON c.board_column_id = bc.id
                 WHERE bc.id = ?;
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, boardId);
            statement.executeQuery();
            var resultSet = statement.getResultSet();

            if (resultSet.next()) {
                var entity = new BoardsColumnsEntity();
                entity.setName(resultSet.getString("bc.name"));
                entity.setKind(BoardColumnKindEnum.findByName(resultSet.getString("bc.kind")));

                do {
                    // Se não houver cartão vinculado, sai do loop
                    if (isNull(resultSet.getString("c.title"))) {
                        break;
                    }
                    // Cria e popula o cartão associado à coluna
                    var card = new CardEntity();
                    card.setId(resultSet.getLong("c.id"));
                    card.setTitle(resultSet.getString("c.title"));
                    card.setDescription(resultSet.getString("c.description"));
                    entity.getCards().add(card);

                } while (resultSet.next());

                return Optional.of(entity);
            }

            // Se não encontrou a coluna, retorna Optional vazio
            return Optional.empty();
        }
    }

}
