package org.example.model;

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
            System.out.println("Server started!");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                try {
                    ClientConnector clientConnector = ClientConnector.createAndStart(clientSocket, "Client-" + (++sessionNumber));
                    clientConnectors.add(clientConnector);

                    MenuService.sendToEveryone(MenuService.SERVER_NAME
                            , clientConnector.getThread().getName() + " successfully connected."
                            , clientConnector);
                } catch (IOException e) {
                    System.out.println("Connection is broken."); //todo: make own exception
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Connection is broken."); //todo: make own exception
        }
    }

}
