package org.projeto.persistence.entity;

import java.util.stream.Stream;

public enum BoardColumnKindEnum {

    INITIAL,  // Coluna inicial do fluxo
    FINAL,    // Coluna final do fluxo
    CANCEL,   // Coluna para itens cancelados
    PENDING;  // Coluna para itens pendentes

    // Método estático para buscar enum pelo nome (String)
    public static BoardColumnKindEnum findByName(final String name) {
        return Stream.of(BoardColumnKindEnum.values())
                .filter(b -> b.name().equals(name)) // filtra enum com nome exato
                .findFirst()                         // tenta pegar o primeiro
                .orElseThrow();                     // lança exceção se não encontrar
    }
}
