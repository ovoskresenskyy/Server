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

        StringBuilder greeting = new StringBuilder("Welcome to our server!")
                .append(System.lineSeparator())
                .append("Known commands:");

        EnumSet.allOf(Command.class)
                .forEach(command -> greeting
                        .append(System.lineSeparator())
                        .append("-> ")
                        .append(command)
                        .append(" ")
                        .append(command.getDescription()));

        sendPrivateMessage("", recipient, greeting.toString());
    }

    public void sendPrivateMessage(String sender, ClientConnector recipient, String message) {
        try {
            recipient.getSender().write(issueSenderName(sender) + message + System.lineSeparator());
            recipient.getSender().flush();
        } catch (IOException e) {
            throw new RuntimeException(e); //todo: make own exception
        }
    }

    public void sendToEveryone(String sender, String message) {
        System.out.println(sender + ": " + message);

        MyServer.clientConnectors.stream()
                .filter(clientConnector -> clientConnector.getThread().isAlive())
                .filter(clientConnector -> clientConnector.getSocket().isConnected())
                .forEach(recipient -> sendPrivateMessage(sender, recipient, message));
    }

    private String issueSenderName(String senderName) {
        return senderName.equals("") ? senderName : "[" + senderName + "]: ";
    }
}
