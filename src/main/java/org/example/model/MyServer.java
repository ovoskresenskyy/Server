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
    public static List<SocketConnector> socketConnectors = new LinkedList<>(); //todo: replace with something ThreadSafety

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(10160)) {
            System.out.println("Server was started!");

            MenuService menuService = MenuService.getInstance();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                try {
                    SocketConnector clientConnector = SocketConnector.createAndStart(clientSocket, "Client-" + (++sessionNumber));
                    socketConnectors.add(clientConnector);

                    menuService.sendToEveryone("", clientConnector + " successfully connected.");

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
