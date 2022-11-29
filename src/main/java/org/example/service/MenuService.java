package org.example.service;

import org.example.enums.Command;
import org.example.model.ClientConnector;
import org.example.model.MyServer;

import java.io.IOException;
import java.util.EnumSet;

public class MenuService {

    private MenuService() {
    }

    private static class MenuServiceHolder {
        private final static MenuService instance = new MenuService();
    }

    public static MenuService getInstance() {
        return MenuServiceHolder.instance;
    }

    public void printCommandMenu(ClientConnector recipient) {
        StringBuilder greeting = new StringBuilder("Known commands:");
        EnumSet.allOf(Command.class)
                .forEach(command -> greeting
                        .append(System.lineSeparator())
                        .append("-> ")
                        .append(command)
                        .append(" ")
                        .append(command.getDescription()));

        sendPrivateMessageFromServer(recipient, greeting.toString());
    }

    public void showSenderName(ClientConnector recipient) {
        try {
            recipient.getSender().write(issueSenderName(recipient));
            recipient.getSender().flush();
        } catch (IOException e) {
            throw new RuntimeException(e); //todo: make own exception
        }
    }

    public void sendPrivateMessage(String sender, ClientConnector recipient, String message) {
        try {
            recipient.getSender().write(issueSenderName(sender) + message + System.lineSeparator());
            recipient.getSender().flush();
        } catch (IOException e) {
            throw new RuntimeException(e); //todo: make own exception
        }
    }

    public void sendPrivateMessageFromServer(ClientConnector recipient, String message) {
        sendPrivateMessage("Server", recipient, message);
    }

    public void sendToEveryone(String sender, String message) {
        MyServer.clientConnectors.stream()
                .filter(clientConnector -> clientConnector.getThread().isAlive())
                .filter(clientConnector -> clientConnector.getSocket().isConnected())
                .forEach(recipient -> sendPrivateMessage(sender, recipient, message));
    }

    public void sendToEveryoneFromServer(String message) {
        sendToEveryone("Server", message);
    }

    private String issueSenderName(String senderName) {
        return "[" + senderName + "]: ";
    }

    private String issueSenderName(ClientConnector sender) {
        return issueSenderName(sender.toString());
    }
}
