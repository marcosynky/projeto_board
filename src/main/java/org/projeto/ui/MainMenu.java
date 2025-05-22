package org.projeto.ui;

// Importa entidades e serviços usados no menu principal
import org.projeto.persistence.entity.BoardsColumnsEntity;
import org.projeto.persistence.entity.BoardColumnKindEnum;
import org.projeto.persistence.entity.BoardsEntity;
import org.projeto.service.BoardsQueryService;
import org.projeto.service.BoardsService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.projeto.persistence.config.ConnectionConfig.getConnection;
import static org.projeto.persistence.entity.BoardColumnKindEnum.*;

public class MainMenu {

    // Scanner para ler entrada do usuário, usando \n como delimitador
    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    // Método principal que exibe o menu e processa as opções do usuário
    public void execute() throws SQLException {
        System.out.println("Bem vindo ao gerenciador de boards, escolha a opção desejada");
        var option = -1;
        while (true) {
            System.out.println("1 - Criar um novo board");
            System.out.println("2 - Selecionar um board existente");
            System.out.println("3 - Excluir um board");
            System.out.println("4 - Sair");
            option = scanner.nextInt(); // lê opção do usuário
            switch (option) {
                case 1 -> createBoard();    // cria board novo
                case 2 -> selectBoard();    // seleciona board existente
                case 3 -> deleteBoard();    // exclui board
                case 4 -> System.exit(0);   // encerra programa
                default -> System.out.println("Opção inválida, informe uma opção do menu");
            }
        }
    }

    // Cria um novo board com colunas configuradas pelo usuário
    private void createBoard() throws SQLException {
        var entity = new BoardsEntity(); // cria entidade Board vazia

        System.out.println("Informe o nome do seu board");
        entity.setName(scanner.next());   // lê nome do board

        System.out.println("Seu board terá colunas além das 3 padrões? Se sim informe quantas, senão digite '0'");
        var additionalColumns = scanner.nextInt(); // lê número de colunas extras

        List<BoardsColumnsEntity> columns = new ArrayList<>(); // lista para armazenar colunas

        System.out.println("Informe o nome da coluna inicial do board");
        var initialColumnName = scanner.next();
        var initialColumn = createColumn(initialColumnName, INITIAL, 0); // cria coluna inicial
        columns.add(initialColumn);

        // Cria colunas adicionais do tipo PENDING conforme informado
        for (int i = 0; i < additionalColumns; i++) {
            System.out.println("Informe o nome da coluna de tarefa pendente do board");
            var pendingColumnName = scanner.next();
            var pendingColumn = createColumn(pendingColumnName, PENDING, i + 1);
            columns.add(pendingColumn);
        }

        System.out.println("Informe o nome da coluna final");
        var finalColumnName = scanner.next();
        var finalColumn = createColumn(finalColumnName, FINAL, additionalColumns + 1);
        columns.add(finalColumn);

        System.out.println("Informe o nome da coluna de cancelamento do baord");
        var cancelColumnName = scanner.next();
        var cancelColumn = createColumn(cancelColumnName, CANCEL, additionalColumns + 2);
        columns.add(cancelColumn);

        entity.setBoardsColumns(columns); // associa lista de colunas ao board

        // Abre conexão e insere board e colunas no banco via serviço
        try (var connection = getConnection()) {
            var service = new BoardsService(connection);
            service.insert(entity);
        }
    }

    // Seleciona um board existente pelo ID e abre menu para manipulação
    private void selectBoard() throws SQLException {
        System.out.println("Informe o id do board que deseja selecionar");
        var id = scanner.nextLong(); // lê id do board

        try (var connection = getConnection()) {
            var queryService = new BoardsQueryService(connection);
            var optional = queryService.findById(id); // busca board por id
            optional.ifPresentOrElse(
                    b -> new BoardsMenu(b).execute(),    // se encontrado, abre menu específico do board
                    () -> System.out.printf("Não foi encontrado um board com id %s\n", id) // senão mostra mensagem
            );
        }
    }

    // Exclui um board do banco pelo ID informado pelo usuário
    private void deleteBoard() throws SQLException {
        System.out.println("Informe o id do board que será excluido");
        var id = scanner.nextLong(); // lê id para exclusão

        try (var connection = getConnection()) {
            var service = new BoardsService(connection);
            if (service.delete(id)) {  // tenta excluir e verifica resultado
                System.out.printf("O board %s foi excluido\n", id);
            } else {
                System.out.printf("Não foi encontrado um board com id %s\n", id);
            }
        }
    }

    // Método auxiliar para criar uma coluna com nome, tipo (kind) e ordem (posição)
    private BoardsColumnsEntity createColumn(final String name, final BoardColumnKindEnum kind, final int order) {
        var boardColumn = new BoardsColumnsEntity();
        boardColumn.setName(name);
        boardColumn.setKind(kind);
        boardColumn.setOrder(order);
        return boardColumn;
    }
}
