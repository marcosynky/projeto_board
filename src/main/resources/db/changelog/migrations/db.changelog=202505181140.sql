--liquibase formatted sql
--changeset marco_antonio:202405181210
--comment: Cria tabela CARDS para armazenar os cartões dos boards

CREATE TABLE CARDS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    board_column_id BIGINT NOT NULL,
    CONSTRAINT board_column_cards_fk FOREIGN KEY (board_column_id) REFERENCES BOARDS_COLUMNS(id) ON DELETE CASCADE
);

--rollback DROP TABLE CARDS
