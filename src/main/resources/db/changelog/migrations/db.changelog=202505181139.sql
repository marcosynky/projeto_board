--liquibase formatted sql
--changeset marco_antonio:202405181200
--comment: Cria tabela BOARDS_COLUMNS para armazenar colunas do quadro

CREATE TABLE BOARDS_COLUMNS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    `order` INT NOT NULL,
    kind VARCHAR(7) NOT NULL,
    board_id BIGINT NOT NULL,
    CONSTRAINT boards__boards_columns_fk FOREIGN KEY (board_id) REFERENCES BOARDS (id) ON DELETE CASCADE,
    CONSTRAINT id_order_uk UNIQUE KEY unique_board_id_order (board_id, `order`)
);

--rollback DROP TABLE BOARDS_COLUMNS
