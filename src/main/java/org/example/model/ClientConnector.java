package org.example.model;

import org.example.NotMyExecutor;
import org.example.enums.Command;
import lombok.Data;
import org.example.exceptions.CantSetConnectionWithSocketException;
import org.example.exceptions.UserInputIsNullException;
import org.example.service.ClientConnectorService;
import org.example.service.MenuService;

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

        MenuService.sendPrivateMessage(MenuService.SERVER_NAME, this, "Welcome to our server!\nKnown commands:");
    }

    @Override
    public void run() {

        ClientConnectorService clientConnectorService = new ClientConnectorService(this);
        Map<Command, NotMyExecutor> commands = initializeCommands(clientConnectorService);

        String userInput;
        try {
            do {
                MenuService.printCommandMenu(this);

                userInput = reader.readLine();
                System.out.println("userInput = " + userInput);
                if (userInput == null) throw new UserInputIsNullException();
                System.out.flush();

                commands.getOrDefault(Command.getByName(userInput), clientConnectorService.wrongCommand())
                        .execute();
            } while (!userInput.equals("-exit"));
        } catch (IOException | UserInputIsNullException e) {
            e.printStackTrace();
            clientConnectorService.closeConnection().execute();
        }
    }

    private Map<Command, NotMyExecutor> initializeCommands(ClientConnectorService clientConnectorService) {
        return Map.of(
                Command.SEND_FILE, clientConnectorService.sendFile(),
                Command.MESSAGE, clientConnectorService.sendMessageForAllConnected(),
                Command.EXIT, clientConnectorService.closeConnection()
        );
    }
}
