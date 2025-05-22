--liquibase formatted sql
--changeset marco_antonio:202405181200
--comment: Cria tabela BOARDS para armazenar quadros

CREATE TABLE BOARDS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

--rollback DROP TABLE BOARDS
