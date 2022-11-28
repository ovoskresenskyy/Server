package org.example.service;

import org.example.NotMyExecutor;
import org.example.exceptions.SocketIsNotReadyToGetUserDataException;
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
        scanner.useDelimiter("\n");
    }

    public NotMyExecutor sendFile() {
        return () -> System.out.println("send file");
    }

    public NotMyExecutor sendMessageForAllConnected() throws SocketIsNotReadyToGetUserDataException {
        return () -> MenuService.sendToEveryone(clientConnector.getThread().getName(), getInputFromUser());
    }

    private String getInputFromUser() throws SocketIsNotReadyToGetUserDataException {
        MenuService.sendPrivateMessage(MenuService.SERVER_NAME, clientConnector, "Insert message you want to send");
        try {
            return clientConnector.getReader().readLine();
        } catch (IOException e) {
            throw new SocketIsNotReadyToGetUserDataException();
        }
    }

    public NotMyExecutor wrongCommand() {
        return () -> MenuService.sendPrivateMessage(MenuService.SERVER_NAME, clientConnector, "Unknown command");
    }

    public NotMyExecutor closeConnection() {
        return () -> {
            try {
                MenuService.sendToEveryone(MenuService.SERVER_NAME, clientConnector.getThread().getName() + " leave our server.");

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
