package org.projeto.dto;

import org.projeto.persistence.entity.BoardColumnKindEnum;

public record BoardsColumnDTO(
        Long id,                // Identificador único da coluna
        String name,            // Nome da coluna
        BoardColumnKindEnum kind, // Tipo da coluna, baseado no enum BoardColumnKindEnum
        int cardsAmount         // Quantidade de cartões presentes na coluna
) {
}
