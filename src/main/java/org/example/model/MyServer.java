package org.example.model;

import org.example.exceptions.CantSetConnectionWithSocketException;
import org.example.service.MenuService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class MyServer {

    private static int sessionNumber;
    public static List<ClientConnector> clientConnectors = new LinkedList<>();

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(10160)) {
            System.out.println("Server was started!");

            MenuService menuService = MenuService.getInstance();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                try {
                    ClientConnector clientConnector = ClientConnector.createAndStart(clientSocket, "Client-" + (++sessionNumber));
                    clientConnectors.add(clientConnector);

                    menuService.sendToEveryoneFromServer(clientConnector + " successfully connected.");
                    menuService.sendPrivateMessageFromServer(clientConnector, "Welcome to our server!");

                    System.out.println(clientConnector + " is connected!");
                } catch (CantSetConnectionWithSocketException e) {
                    e.printStackTrace();
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Connection is broken.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
