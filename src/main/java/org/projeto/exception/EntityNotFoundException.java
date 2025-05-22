package org.projeto.exception;
// Declara o pacote onde a exceção EntityNotFoundException está localizada

// Define uma exceção customizada que estende RuntimeException
public class EntityNotFoundException extends RuntimeException {

    // Construtor que recebe uma mensagem personalizada para a exceção
    public EntityNotFoundException(String message) {
        super(message); // Passa a mensagem para a superclasse RuntimeException
    }
}
