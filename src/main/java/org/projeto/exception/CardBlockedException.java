package org.projeto.exception;
// Declara o pacote onde a exceção CardBlockedException está localizada

// Define uma exceção customizada que estende RuntimeException
public class CardBlockedException extends RuntimeException {

    // Construtor que recebe uma mensagem personalizada para a exceção
    public CardBlockedException(final String message) {
        super(message);  // Passa a mensagem para a classe pai RuntimeException
    }
}
