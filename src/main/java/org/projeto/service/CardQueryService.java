package org.projeto.service;

import lombok.AllArgsConstructor;
import org.projeto.dto.CardDetailsDTO;
import org.projeto.persistence.dao.CardDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class CardQueryService {

    private final Connection connection;  // conexão com banco usada pelo DAO

    /**
     * Busca detalhes de um cartão (card) pelo seu ID.
     * Retorna um Optional com o DTO contendo os dados, ou vazio se não encontrado.
     */
    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        var dao = new CardDAO(connection);
        return dao.findById(id);  // chama o DAO para fazer a consulta no banco
    }

}
