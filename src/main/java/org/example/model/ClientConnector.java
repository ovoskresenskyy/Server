package org.example.model;

import org.example.NotMyExecutor;
import org.example.enums.Command;
import lombok.Data;
import org.example.exceptions.CantSetConnectionWithSocketException;
import org.example.exceptions.SocketIsNotReadyToGetUserDataException;
import org.example.exceptions.UserInputIsNullException;
import org.example.service.ClientConnectorService;
import org.example.service.MenuService;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Map;

@Data
public class ClientConnector implements Runnable {

    private Thread thread;
    private final Socket socket;
    private final LocalDate connectionTimeStamp;
    private final BufferedReader reader;
    private final BufferedWriter sender;

    public static ClientConnector createAndStart(Socket socket, String name) throws CantSetConnectionWithSocketException {

        ClientConnector clientConnector = new ClientConnector(socket, name);
        clientConnector.thread.start();
        return clientConnector;
    }

    private ClientConnector(Socket socket, String name) throws CantSetConnectionWithSocketException {

        thread = new Thread(this, name);
        this.socket = socket;
        connectionTimeStamp = LocalDate.now();
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sender = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new CantSetConnectionWithSocketException();
        }
    }

    @Override
    public void run() {

        ClientConnectorService clientConnectorService = ClientConnectorService.getInstance();
        Map<Command, NotMyExecutor> commandHandler = initializeCommands(clientConnectorService);
        MenuService menuService = MenuService.getInstance();

        String userInput;
        try {
            do {

                menuService.showSenderName(this);
                userInput = reader.readLine();
                if (userInput == null) throw new UserInputIsNullException();

                commandHandler.getOrDefault(Command.getByName(userInput), clientConnectorService.sendMessageForAllConnected(userInput))
                        .execute();
            } while (!userInput.equals("-exit"));
        } catch (IOException | UserInputIsNullException | SocketIsNotReadyToGetUserDataException e) {
            e.printStackTrace();
            clientConnectorService.closeConnection(this).execute();
        }
    }

    private Map<Command, NotMyExecutor> initializeCommands(ClientConnectorService clientConnectorService) {
        return Map.of(
                Command.SEND_FILE, clientConnectorService.sendFile(),
                Command.EXIT, clientConnectorService.closeConnection(this)
        );
    }

    @Override
    public String toString() {
        return thread.getName();
    }
}
