package org.projeto.dto;

import java.util.List;

public record BoardDetailsDTO(
        Long id,                // Identificador único do quadro
        String name,            // Nome do quadro
        List<BoardsColumnDTO> columns // Lista das colunas do quadro, cada uma representada por um BoardColumnDTO
) {
}
