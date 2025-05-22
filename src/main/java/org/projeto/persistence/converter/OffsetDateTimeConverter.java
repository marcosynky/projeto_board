package org.projeto.persistence.converter;

import lombok.NoArgsConstructor;
// Importa anotação Lombok para gerar construtor sem argumentos

import java.sql.Timestamp;
// Importa Timestamp para trabalhar com data/hora do banco de dados
import java.time.OffsetDateTime;
// Importa OffsetDateTime para manipulação de data/hora com fuso horário

import static java.time.ZoneOffset.UTC;
// Importa constante UTC para padronizar o fuso horário
import static java.util.Objects.nonNull;
// Importa método para checar se objeto não é nulo
import static lombok.AccessLevel.PRIVATE;
// Importa nível de acesso PRIVATE para o construtor gerado por Lombok

@NoArgsConstructor(access = PRIVATE)
// Gera um construtor privado para impedir instanciação da classe utilitária
public final class OffsetDateTimeConverter {

    // Converte um java.sql.Timestamp para java.time.OffsetDateTime usando fuso UTC
    public static OffsetDateTime toOffsetDateTime(final Timestamp value){
        return nonNull(value) ? OffsetDateTime.ofInstant(value.toInstant(), UTC) : null;
    }

    // Converte um java.time.OffsetDateTime para java.sql.Timestamp usando fuso UTC
    public static Timestamp toTimestamp(final OffsetDateTime value){
        return nonNull(value) ? Timestamp.valueOf(value.atZoneSameInstant(UTC).toLocalDateTime()) : null;
    }

}
