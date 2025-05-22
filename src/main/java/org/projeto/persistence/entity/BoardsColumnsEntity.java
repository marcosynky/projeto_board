package org.projeto.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardsColumnsEntity {

    private Long id;                    // Identificador único da coluna
    private String name;                // Nome da coluna
    private int order;                  // Ordem da coluna no board (posição)
    private BoardColumnKindEnum kind;  // Tipo da coluna (enum, ex: INITIAL, PENDING, FINAL)
    private BoardsEntity boards = new BoardsEntity();
    // Referência ao board ao qual essa coluna pertence

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CardEntity> cards = new ArrayList<>();
    // Lista de cartões pertencentes a essa coluna
    // Excluída dos métodos toString, equals e hashCode para evitar recursão infinita e melhorar desempenho

}
