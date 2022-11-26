import lombok.Data;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;

@Data
public class ConnectedClient {

    private final Socket socket;
    private final String name;
    private final LocalDate connectionTimeStamp;
    public final BufferedReader reader;
    public final BufferedWriter sender;

    public ConnectedClient(Socket socket, String name) throws IOException {
        this.socket = socket;
        this.name = name;
        connectionTimeStamp = LocalDate.now();
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        sender = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void sendMessage(String senderName, String message) {
        try {
            sender.write(senderName + " " + message + "\n");
            sender.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
