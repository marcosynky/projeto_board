package org.projeto.persistence.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.NoSuchElementException;

import static org.projeto.persistence.entity.BoardColumnKindEnum.CANCEL;
import static org.projeto.persistence.entity.BoardColumnKindEnum.INITIAL;

@Data
public class BoardsEntity {
    private Long id;               // Identificador único do quadro
    private String name;           // Nome do quadro

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BoardsColumnsEntity> boardsColumns = new ArrayList<>();
    // Lista de colunas que pertencem ao quadro
    // Excluída dos métodos toString, equals e hashCode para evitar recursão e melhorar performance

    // Retorna a coluna inicial do quadro (tipo INITIAL)
    public BoardsColumnsEntity getInitialColumn() {
        return getFilteredColumn(bc -> bc.getKind().equals(INITIAL));
    }
    // Retorna a coluna de cancelamento do quadro (tipo CANCEL)
    public BoardsColumnsEntity getCancelColumn() {
        return getFilteredColumn(bc -> bc.getKind().equals(CANCEL));
    }

    // Método privado que retorna a primeira coluna que satisfaz o filtro (Predicate)
    private BoardsColumnsEntity getFilteredColumn(Predicate<BoardsColumnsEntity> filter) {
        return boardsColumns.stream()
                .filter(filter)                          // filtra as colunas de acordo com o Predicate
                .findFirst()                              // retorna o primeiro elemento que passar no filtro
                .orElseThrow(() -> new NoSuchElementException("Nenhuma coluna encontrada para o tipo especificado"));
    }


   }

