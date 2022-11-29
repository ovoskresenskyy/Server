package org.example.service;

import org.example.NotMyExecutor;
import org.example.model.ClientConnector;
import org.example.model.MyServer;

import java.io.IOException;

public class ClientConnectorService {

    private ClientConnectorService(){}

    private static class ClientConnectorServiceHolder {
        private final static ClientConnectorService instance = new ClientConnectorService();
    }

    public static ClientConnectorService getInstance() {
        return ClientConnectorService.ClientConnectorServiceHolder.instance;
    }

    public NotMyExecutor sendFile() {
        return () -> System.out.println("send file");
    }

    public NotMyExecutor sendMessageForAllConnected(String sender, String message) {
        return () -> MenuService.getInstance().sendToEveryone(sender, message);
    }

    public NotMyExecutor closeConnection(ClientConnector clientConnector) {
        return () -> {
            try {
                MenuService.sendToEveryone(MenuService.SERVER_NAME, clientConnector.getThread().getName() + " leave our server.");

                MyServer.clientConnectors.remove(clientConnector);
                clientConnector.getSocket().close();

                System.out.println(clientConnector.getThread().getName() + " is disconnected.");
            } catch (IOException e) {
                throw new RuntimeException(e); // todo: make own exception
            }
        };
    }
}
