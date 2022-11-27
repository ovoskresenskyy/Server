package org.example.service;

import org.example.NotMyExecutor;
import org.example.model.ClientConnector;
import org.example.model.MyServer;

import java.io.IOException;
import java.util.Scanner;

public class ClientConnectorService {
    private final ClientConnector clientConnector;
    private final Scanner scanner;

    public ClientConnectorService(ClientConnector clientConnector) {
        this.clientConnector = clientConnector;

        scanner = new Scanner(System.in);
    }

    public NotMyExecutor sendFile() {
        return () -> System.out.println("send file");
    }

    public NotMyExecutor sendMessageForAllConnected() {
        return () -> MenuService.sendToEveryone(clientConnector.getThread().getName(), getMessageFromUser(), clientConnector);
    }

    private String getMessageFromUser() {
        MenuService.sendPrivateMessage(MenuService.SERVER_NAME, clientConnector, "Insert message you want to send");
        String userInput = scanner.next(); //todo rework.
        return userInput;
    }

    public NotMyExecutor wrongCommand() {
        return () -> MenuService.sendPrivateMessage(MenuService.SERVER_NAME, clientConnector, "Unknown command");
    }

    public NotMyExecutor closeConnection() {
        return () -> {
            try {
                MenuService.sendToEveryone(MenuService.SERVER_NAME, clientConnector.getThread().getName() + " leave our server.", clientConnector);

                MyServer.clientConnectors.remove(clientConnector);
                clientConnector.getSocket().close();
                scanner.close();

                System.out.println(clientConnector.getThread().getName() + " is disconnected.");
            } catch (IOException e) {
                throw new RuntimeException(e); // todo: make own exception
            }
        };
    }
}
