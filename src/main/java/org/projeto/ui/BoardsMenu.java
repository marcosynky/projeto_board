package org.projeto.ui;

import lombok.AllArgsConstructor;
import org.projeto.dto.BoardsColumnInfoDTO;
import org.projeto.persistence.entity.BoardsColumnsEntity;
import org.projeto.persistence.entity.BoardsEntity;
import org.projeto.persistence.entity.CardEntity;
import org.projeto.service.BoardsQueryService;
import org.projeto.service.CardQueryService;
import org.projeto.service.CardService;

import java.sql.SQLException;
import java.util.Scanner;

import static org.projeto.persistence.config.ConnectionConfig.getConnection;

@AllArgsConstructor
public class BoardsMenu {

    // Scanner para ler entradas do usuário, com delimitador para linhas completas
    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    // Board atual que será manipulado no menu
    private final BoardsEntity entity;

    // Método principal que mostra o menu e executa as ações conforme opção do usuário
    public void execute() {
        try {
            System.out.printf("Bem vindo ao board %s, selecione a operação desejada\n", entity.getId());
            var option = -1;
            while (option != 9) {
                // Menu de opções
                System.out.println("1 - Criar um card");
                System.out.println("2 - Mover um card");
                System.out.println("3 - Bloquear um card");
                System.out.println("4 - Desbloquear um card");
                System.out.println("5 - Cancelar um card");
                System.out.println("6 - Ver board");
                System.out.println("7 - Ver coluna com cards");
                System.out.println("8 - Ver card");
                System.out.println("9 - Voltar para o menu anterior um card");
                System.out.println("10 - Sair");
                option = scanner.nextInt();

                // Chama o método adequado de acordo com a opção
                switch (option) {
                    case 1 -> createCard();
                    case 2 -> moveCardToNextColumn();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltando para o menu anterior");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opção inválida, informe uma opção do menu");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    // Cria um novo card lendo título e descrição do usuário, associando à coluna inicial do board
    private void createCard() throws SQLException {
        var card = new CardEntity();
        System.out.println("Informe o título do card");
        card.setTitle(scanner.next());
        System.out.println("Informe a descrição do card");
        card.setDescription(scanner.next());
        // Associa o card à primeira coluna do board (coluna inicial)
        card.setBoardsColumn(entity.getBoardsColumns().get(0));

        try (var connection = getConnection()) {
            new CardService(connection).create(card);
        }
    }

    // Move um card para a próxima coluna do board (fluxo de colunas)
    private void moveCardToNextColumn() throws SQLException {
        System.out.println("Informe o id do card que deseja mover para a próxima coluna");
        var cardId = scanner.nextLong();

        // Cria lista de informações das colunas para uso no serviço
        var boardColumnsInfo = entity.getBoardsColumns().stream()
                .map(bc -> new BoardsColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();

        try (var connection = getConnection()) {
            new CardService(connection).moveToNextColumn(cardId, boardColumnsInfo);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage()); // Exibe erros (ex: bloqueio, finalizado)
        }
    }

    // Bloqueia um card informando id e motivo
    private void blockCard() throws SQLException {
        System.out.println("Informe o id do card que será bloqueado");
        var cardId = scanner.nextLong();
        System.out.println("Informe o motivo do bloqueio do card");
        var reason = scanner.next();

        var boardColumnsInfo = entity.getBoardsColumns().stream()
                .map(bc -> new BoardsColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();

        try (var connection = getConnection()) {
            new CardService(connection).block(cardId, reason, boardColumnsInfo);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Desbloqueia um card informando id e motivo
    private void unblockCard() throws SQLException {
        System.out.println("Informe o id do card que será desbloqueado");
        var cardId = scanner.nextLong();
        System.out.println("Informe o motivo do desbloqueio do card");
        var reason = scanner.next();

        try (var connection = getConnection()) {
            new CardService(connection).unblock(cardId, reason);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Cancela um card movendo para a coluna de cancelamento do board
    private void cancelCard() throws SQLException {
        System.out.println("Informe o id do card que deseja mover para a coluna de cancelamento");
        var cardId = scanner.nextLong();
        var cancelColumn = entity.getCancelColumn();

        var boardColumnsInfo = entity.getBoardsColumns().stream()
                .map(bc -> new BoardsColumnInfoDTO(bc.getId(), bc.getOrder(), bc.getKind()))
                .toList();

        try (var connection = getConnection()) {
            new CardService(connection).cancel(cardId, cancelColumn.getId(), boardColumnsInfo);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Mostra detalhes do board e suas colunas, incluindo quantidade de cards
    private void showBoard() throws SQLException {
        try (var connection = getConnection()) {
            var optional = new BoardsQueryService(connection).showBoardDetails(entity.getId());
            optional.ifPresent(b -> {
                System.out.printf("Board [%s,%s]\n", b.id(), b.name());
                b.columns().forEach(c ->
                        System.out.printf("Coluna [%s] tipo: [%s] tem %s cards\n", c.name(), c.kind(), c.cardsAmount())
                );
            });
        }
    }

    // Exibe uma coluna específica e seus cards após o usuário escolher a coluna
    private void showColumn() throws SQLException {
        var columnsIds = entity.getBoardsColumns().stream().map(BoardsColumnsEntity::getId).toList();
        var selectedColumnId = -1L;

        while (!columnsIds.contains(selectedColumnId)) {
            System.out.println("Informe o id da coluna que deseja visualizar");
            // Lista colunas disponíveis com id, nome e tipo
            entity.getBoardsColumns().forEach(c -> System.out.printf("%s - %s [%s]\n", c.getId(), c.getName(), c.getKind()));
            selectedColumnId = scanner.nextLong();
        }

        // Aqui você poderia adicionar a lógica para exibir cards da coluna selecionada
    }

    // Exibe detalhes de um card pelo id informado pelo usuário
    private void showCard() throws SQLException {
        System.out.println("Informe o id do card que deseja visualizar");
        var selectedCardId = scanner.nextLong();

        try (var connection = getConnection()) {
            new CardQueryService(connection).findById(selectedCardId)
                    .ifPresentOrElse(
                            c -> {
                                System.out.printf("Card %s - %s.\n", c.id(), c.title());
                                System.out.printf("Descrição: %s\n", c.description());
                                System.out.println(c.blocked() ?
                                        "Está bloqueado. Motivo: " + c.blockReason() :
                                        "Não está bloqueado");
                                System.out.printf("Já foi bloqueado %s vezes\n", c.blocksAmount());
                                System.out.printf("Está no momento na coluna %s - %s\n", c.columnId(), c.columnName());
                            },
                            () -> System.out.printf("Não existe um card com o id %s\n", selectedCardId));
        }
    }
}
