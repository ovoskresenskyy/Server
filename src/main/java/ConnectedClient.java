import lombok.Data;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;

@Data
public class ConnectedClient extends Thread {

    private final Socket socket;
    private final String sessionName;
    private final LocalDate connectionTimeStamp;
    public final BufferedReader reader;
    public final BufferedWriter sender;

    public ConnectedClient(Socket socket, String sessionName) throws IOException {
        this.socket = socket;
        this.sessionName = sessionName;
        connectionTimeStamp = LocalDate.now();
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        sender = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        start();
    }

    @Override
    public void run() {

        String command;

        try {
            while (true) {
                command = reader.readLine();
                if(command.equals("-exit")) {
                    Server.closeConnection(this);
                    break;
                }
                // todo: -file
                // todo: -send to all
            }
        } catch (IOException e) {
            // todo: make own exception
        }
    }

    public void sendMessage(String senderName, String message) {
        try {
            sender.write(senderName + " " + message + "\n");
            sender.flush();
        } catch (IOException e) {
            throw new RuntimeException(e); //todo: make own exception
        }
    }
}
