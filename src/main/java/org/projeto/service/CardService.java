package org.projeto.service;

import lombok.AllArgsConstructor;
import org.projeto.dto.BoardsColumnInfoDTO;
import org.projeto.exception.CardBlockedException;
import org.projeto.exception.CardFinishedException;
import org.projeto.exception.EntityNotFoundException;
import org.projeto.persistence.dao.BlockDAO;
import org.projeto.persistence.dao.CardDAO;
import org.projeto.persistence.entity.CardEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.projeto.persistence.entity.BoardColumnKindEnum.CANCEL;
import static org.projeto.persistence.entity.BoardColumnKindEnum.FINAL;

@AllArgsConstructor
public class CardService {

    private final Connection connection; // conexão com banco para operações

    /**
     * Cria um novo cartão no banco de dados.
     * Faz commit se sucesso, rollback se exceção.
     */
    public CardEntity create(final CardEntity entity) throws SQLException {
        try {
            var dao = new CardDAO(connection);
            dao.insert(entity);
            connection.commit();
            return entity;
        } catch (SQLException ex){
            connection.rollback();
            throw ex;
        }
    }

    /**
     * Move um cartão para a próxima coluna no fluxo do board.
     * Verifica se o card existe, se não está bloqueado, e se pode avançar.
     * Se estiver na coluna FINAL, lança exceção.
     */
    public void moveToNextColumn(final Long cardId, final List<BoardsColumnInfoDTO> boardColumnsInfo) throws SQLException {
        try{
            var dao = new CardDAO(connection);
            var optional = dao.findById(cardId);
            var dto = optional.orElseThrow(
                    () -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId))
            );

            if (dto.blocked()){
                var message = "O card %s está bloqueado, é necessário desbloqueá-lo para mover".formatted(cardId);
                throw new CardBlockedException(message);
            }

            // Busca a coluna atual do cartão
            var currentColumn = boardColumnsInfo.stream()
                    .filter(bc -> bc.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board"));

            if (currentColumn.kind().equals(FINAL)){
                throw new CardFinishedException("O card já foi finalizado");
            }

            // Busca a próxima coluna com ordem +1
            var nextColumn = boardColumnsInfo.stream()
                    .filter(bc -> bc.order() == currentColumn.order() + 1)
                    .findFirst().orElseThrow(() -> new IllegalStateException("O card está cancelado"));

            dao.moveToColumn(nextColumn.id(), cardId); // move cartão para próxima coluna
            connection.commit();

        } catch (SQLException ex){
            connection.rollback();
            throw ex;
        }
    }

    /**
     * Cancela o cartão movendo-o para a coluna de cancelamento.
     * Verifica existência, bloqueio e status do cartão.
     */
    public void cancel(final Long cardId, final Long cancelColumnId,
                       final List<BoardsColumnInfoDTO> boardsColumnsInfo) throws SQLException {
        try{
            var dao = new CardDAO(connection);
            var optional = dao.findById(cardId);
            var dto = optional.orElseThrow(
                    () -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(cardId))
            );

            if (dto.blocked()){
                var message = "O card %s está bloqueado, é necessário desbloqueá-lo para mover".formatted(cardId);
                throw new CardBlockedException(message);
            }

            var currentColumn = boardsColumnsInfo.stream()
                    .filter(bc -> bc.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro board"));

            if (currentColumn.kind().equals(FINAL)){
                throw new CardFinishedException("O card já foi finalizado");
            }

            // Verifica se o card tem próxima coluna, se não, considera cancelado
            boardsColumnsInfo.stream()
                    .filter(bc -> bc.order() == currentColumn.order() + 1)
                    .findFirst().orElseThrow(() -> new IllegalStateException("O card está cancelado"));

            dao.moveToColumn(cancelColumnId, cardId); // move para coluna de cancelamento
            connection.commit();

        } catch (SQLException ex){
            connection.rollback();
            throw ex;
        }
    }

    /**
     * Bloqueia o cartão com um motivo informado.
     * Verifica se já está bloqueado, e se está em coluna que permite bloqueio.
     */
    public void block(final Long id, final String reason, final List<BoardsColumnInfoDTO> boardColumnsInfo) throws SQLException {
        try{
            var dao = new CardDAO(connection);
            var optional = dao.findById(id);
            var dto = optional.orElseThrow(
                    () -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(id))
            );

            if (dto.blocked()){
                var message = "O card %s já está bloqueado".formatted(id);
                throw new CardBlockedException(message);
            }

            var currentColumn = boardColumnsInfo.stream()
                    .filter(bc -> bc.id().equals(dto.columnId()))
                    .findFirst()
                    .orElseThrow();

            // Não permite bloqueio se estiver nas colunas FINAL ou CANCEL
            if (currentColumn.kind().equals(FINAL) || currentColumn.kind().equals(CANCEL)){
                var message = "O card está em uma coluna do tipo %s e não pode ser bloqueado"
                        .formatted(currentColumn.kind());
                throw new IllegalStateException(message);
            }

            var blockDAO = new BlockDAO(connection);
            blockDAO.block(reason, id); // bloqueia o cartão com motivo
            connection.commit();

        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }
    }

    /**
     * Desbloqueia o cartão com motivo informado.
     * Verifica se está bloqueado antes.
     */
    public void unblock(final Long id, final String reason) throws SQLException {
        try{
            var dao = new CardDAO(connection);
            var optional = dao.findById(id);
            var dto = optional.orElseThrow(
                    () -> new EntityNotFoundException("O card de id %s não foi encontrado".formatted(id))
            );

            if (!dto.blocked()){
                var message = "O card %s não está bloqueado".formatted(id);
                throw new CardBlockedException(message);
            }

            var blockDAO = new BlockDAO(connection);
            blockDAO.unblock(reason, id); // desbloqueia o cartão com motivo
            connection.commit();

        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }
    }
}
