--liquibase formatted sql
--changeset marco_antonio:202405181220
--comment: Cria tabela BLOCKS para armazenar bloqueios dos cards

CREATE TABLE BLOCKS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    block_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    block_reason VARCHAR(255) NOT NULL,
    unblock_at TIMESTAMP NULL,
    unblock_reason VARCHAR(255) NOT NULL,
    card_id BIGINT NOT NULL,
    CONSTRAINT cards_blocks_fk FOREIGN KEY (card_id) REFERENCES CARDS(id) ON DELETE CASCADE
);

--rollback DROP TABLE BLOCKS
