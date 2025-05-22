package org.projeto.service;

import lombok.AllArgsConstructor;
import org.projeto.dto.BoardDetailsDTO;
import org.projeto.persistence.dao.BoardsColumnDAO;
import org.projeto.persistence.dao.BoardsDAO;
import org.projeto.persistence.entity.BoardsColumnsEntity;
import org.projeto.persistence.entity.BoardsEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;


@AllArgsConstructor
public class BoardsQueryService {

    private final Connection connection;  // conexão com banco para as DAOs

    // Busca um quadro pelo ID e popula suas colunas
    public Optional<BoardsEntity> findById(final Long id) throws SQLException {
        var dao = new BoardsDAO(connection);
        var boardColumnDAO = new BoardsColumnDAO(connection);

        var optional = dao.findById(id);  // busca o quadro

        if (optional.isPresent()) {
            var entity = optional.get();
            // Busca as colunas do quadro e associa ao objeto BoardEntity
            entity.setBoardsColumns(boardColumnDAO.findByBoardId(entity.getId()));
            return Optional.of(entity);
        }
        return Optional.empty();  // retorna vazio se não encontrado
    }

    // Retorna detalhes do quadro com colunas e quantidade de cartões por coluna
    public Optional<BoardDetailsDTO> showBoardDetails(final Long id) throws SQLException {
        var dao = new BoardsDAO(connection);
        var boardColumnDAO = new BoardsColumnDAO(connection);

        var optional = dao.findById(id);  // busca o quadro

        if (optional.isPresent()) {
            var entity = optional.get();
            // Busca colunas com detalhes (incluindo quantidade de cartões)
            var columns = boardColumnDAO.findByBoardIdWithDetails(entity.getId());
            // Cria DTO para apresentação dos detalhes do quadro
            var dto = new BoardDetailsDTO(entity.getId(), entity.getName(), columns);
            return Optional.of(dto);
        }
        return Optional.empty();  // retorna vazio se não encontrado
    }
}
