package org.example.service;

import org.example.enums.Command;
import org.example.model.ClientConnector;
import org.example.model.MyServer;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

public class MenuService {

    public static final String SERVER_NAME = "[SERVER]";
    private static final Set<Command> COMMANDS = EnumSet.allOf(Command.class);

    public static void printCommandMenu(ClientConnector recipient) {

        StringBuilder greeting = new StringBuilder("Welcome to our server!\nKnown commands:");
        COMMANDS.forEach(command -> greeting.append("\n -> ")
                .append(command)
                .append(" ")
                .append(command.getDescription()));

        sendPrivateMessage(SERVER_NAME, recipient, greeting.toString());
    }

    public static void sendPrivateMessage(String sender, ClientConnector recipient, String message) {
        try {
            recipient.getSender().write(sender + " " + message + "\n");
            recipient.getSender().flush();
        } catch (IOException e) {
            throw new RuntimeException(e); //todo: make own exception
        }
    }

    public static void sendToEveryone(String sender, String message, ClientConnector elementToSkip) {
        MyServer.clientConnectors.stream()
                .filter(clientConnector -> !clientConnector.equals(elementToSkip))
                .filter(clientConnector -> clientConnector.getThread().isAlive())
                .filter(clientConnector -> clientConnector.getSocket().isConnected())
                .forEach(clientConnector -> sendPrivateMessage(sender, clientConnector, message));
    }

}