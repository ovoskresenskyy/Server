import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server {

    private static final String SERVER_NAME = "[SERVER]";
    private static int sessionNumber;
    public static List<ConnectedClient> clients = new LinkedList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(10160)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                try {
                    ConnectedClient client = new ConnectedClient(clientSocket, "Client-"+(++sessionNumber));
                    client.sendMessage(SERVER_NAME, "Welcome to our test server!");

                    clients.add(client);

                    sendToEveryone(client.getSessionName() + " successfully connected.");
                } catch (IOException e) {
                    System.out.println("Connection is broken."); //todo: make own exception
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Connection is broken."); //todo: make own exception
        }
    }

    private static void sendToEveryone(String message) {
        Server.clients.stream()
                .filter(client -> client.getSocket().isConnected())
                .forEach(client -> client.sendMessage(SERVER_NAME, message));
    }

    public static void closeConnection(ConnectedClient client){
        try {
            client.getSocket().close();
            clients.remove(client);
        } catch (IOException e) {
            throw new RuntimeException(e); // todo: make own exception
        }
    }
}
