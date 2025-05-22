package org.projeto.exception;

// Declara o pacote onde a exceção CardFinishedException está localizada

// Define uma exceção customizada que estende RuntimeException
public class CardFinishedException extends RuntimeException {

    // Construtor que recebe uma mensagem personalizada para a exceção
    public CardFinishedException(final String message) {
        super(message);  // Passa a mensagem para a superclasse RuntimeException
    }
}
