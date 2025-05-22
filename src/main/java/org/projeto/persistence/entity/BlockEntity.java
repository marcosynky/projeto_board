package org.projeto.persistence.entity;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class BlockEntity {

    private Long id;                     // Identificador único do bloqueio
    private OffsetDateTime block_at;    // Data e hora do bloqueio
    private String block_reason;        // Motivo do bloqueio
    private OffsetDateTime unblock_at;  // Data e hora do desbloqueio (se aplicável)
    private String unblock_reason;      // Motivo do desbloqueio (se aplicável)
    private Long card_id;               // ID do card que foi bloqueado

}
