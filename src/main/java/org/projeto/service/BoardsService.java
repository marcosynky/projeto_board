package org.projeto.service;

import lombok.AllArgsConstructor;
import org.projeto.persistence.dao.BoardsColumnDAO;
import org.projeto.persistence.dao.BoardsDAO;
import org.projeto.persistence.entity.BoardsEntity;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class BoardsService {

    private final Connection connection;  // conexão com banco usada nas operações

    /**
     * Insere um quadro (board) e suas colunas no banco.
     * Faz commit ao final, ou rollback em caso de erro.
     * @param entity entidade BoardsEntity com colunas preenchidas
     * @return entidade atualizada (com IDs, se atribuídos no banco)
     * @throws SQLException erro em operação com banco
     */
    public BoardsEntity insert(final BoardsEntity entity) throws SQLException {
        var dao = new BoardsDAO(connection);
        var boardColumnDAO = new BoardsColumnDAO(connection);

        try {
            dao.insert(entity);  // insere o quadro no banco

            // Garante que cada coluna tenha referência ao board
            var columns = entity.getBoardsColumns().stream().map(c -> {
                c.setBoards(entity);
                return c;
            }).toList();

            // Insere todas as colunas do quadro no banco
            for (var column : columns) {
                boardColumnDAO.insert(column);
            }

            connection.commit();  // confirma a transação

        } catch (SQLException e) {
            connection.rollback();  // desfaz alterações em caso de erro
            throw e;                // relança exceção para tratamento fora do método
        }

        return entity;  // retorna a entidade inserida
    }

    /**
     * Deleta um quadro pelo seu ID.
     * @param id id do board a ser deletado
     * @return true se deletou, false se o quadro não existia
     * @throws SQLException erro em operação com banco
     */
    public boolean delete(final Long id) throws SQLException {
        var dao = new BoardsDAO(connection);

        try {
            if (!dao.exists(id)) {
                return false;  // board não existe
            }

            dao.delete(id);       // deleta board do banco
            connection.commit();  // confirma transação
            return true;

        } catch (SQLException e) {
            connection.rollback();  // desfaz alterações em caso de erro
            throw e;                // relança exceção para tratamento externo
        }
    }
}
