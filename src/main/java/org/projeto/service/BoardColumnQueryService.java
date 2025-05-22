package org.projeto.service;

import lombok.AllArgsConstructor;
import org.projeto.persistence.dao.BoardsColumnDAO;
import org.projeto.persistence.entity.BoardsColumnsEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardColumnQueryService {

    private final Connection connection;  // conexão com banco usada pelo DAO

    /**
     * Busca uma coluna do board pelo seu ID.
     * Retorna Optional com a entidade BoardsColumnsEntity, ou vazio se não encontrado.
     * @param id ID da coluna
     * @return Optional contendo a coluna ou vazio
     * @throws SQLException erro em operação com banco
     */
    public Optional<BoardsColumnsEntity> findById(final Long id) throws SQLException {
        var dao = new BoardsColumnDAO(connection);
        return dao.findById(id);  // delega a consulta para o DAO
    }

}
