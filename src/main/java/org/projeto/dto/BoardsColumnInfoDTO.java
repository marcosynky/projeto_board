package org.projeto.dto;

import org.projeto.persistence.entity.BoardColumnKindEnum;

public record BoardsColumnInfoDTO(
        Long id,               // Identificador único da coluna
        int order,             // Ordem da coluna no quadro (posição)
        BoardColumnKindEnum kind // Tipo da coluna, conforme enum BoardColumnKindEnum
) {
}
