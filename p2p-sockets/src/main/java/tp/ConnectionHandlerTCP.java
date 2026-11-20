package tp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnectionHandlerTCP implements Runnable {
    private Socket socket;
    private String peerId;
    private PeerTCP peer;

    public ConnectionHandlerTCP(Socket socket, String peerId, PeerTCP peer) {
        this.socket = socket;
        this.peerId = peerId;
        this.peer = peer;
    }

    public String getAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    public int getPort() {
        return socket.getPort();
    }


    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received from " + getAddress() + ":" + getPort() + ": " + message);
            }
        } catch (IOException e) {
            System.err.println("Error handling connection: " + e.getMessage());
        }
    }
}
