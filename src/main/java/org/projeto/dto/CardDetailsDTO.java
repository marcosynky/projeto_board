package org.projeto.dto;

import java.time.OffsetDateTime;

public record CardDetailsDTO(
        Long id,              // Identificador único do cartão
        String title,         // Título do cartão
        String description,   // Descrição detalhada do cartão
        boolean blocked,      // Indica se o cartão está bloqueado ou não
        OffsetDateTime blockedAt, // Data e hora em que o cartão foi bloqueado (se aplicável)
        String blockReason,   // Motivo pelo qual o cartão foi bloqueado
        int blocksAmount,     // Quantidade total de bloqueios que o cartão sofreu
        Long columnId,        // Identificador da coluna em que o cartão está
        String columnName     // Nome da coluna onde o cartão está localizado
) {
}
