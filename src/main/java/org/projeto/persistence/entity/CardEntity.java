package org.projeto.persistence.entity;

import lombok.Data;
// Importa a anotação Lombok para gerar getters, setters, toString, equals e hashCode automaticamente

@Data
// Classe que representa a entidade Cartão (Card)
public class CardEntity {

    private Long id;                 // Identificador único do cartão
    private String title;            // Título do cartão
    private String description;      // Descrição do cartão
    private BoardsColumnsEntity boardsColumn = new BoardsColumnsEntity();
    // Coluna do quadro à qual esse cartão pertence, inicializada para evitar null pointer
}
