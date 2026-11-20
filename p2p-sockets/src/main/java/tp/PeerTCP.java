package tp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//NB: implements Runnable pour partie .run() ==> .startChat()
public class PeerTCP implements Runnable {

    private static final int PORT = 12345;
    private static int thisPeerPort = PORT; //or @...
    //NB: for simple exec on same host, peer_1 uses PORT by default ,
    // peer_2@12346 uses 12346, peer_3@12347 123467 etc

    private static final String PROMPT = "> ";
    private List<ConnectionHandlerTCP> connections;

    private String peerId;
    public PeerTCP(String peerId) {
        this.peerId=peerId;
        connections = new ArrayList<>();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Peer <peer_id> [<peer_address>:<peer_port>]...");
            return;
        }

        String peerId = args[0];
        try {
            //Peer.thisPeerPort= value after @ in peer_...@....
            PeerTCP.thisPeerPort= Integer.parseInt(peerId.split("@")[1]);
        } catch (Exception e) {
            //use default value PORT=12345
        }
        PeerTCP peer = new PeerTCP(peerId);

        for (int i = 1; i < args.length; i++) {
            String peerAddressPort = args[i];
            String[] parts = peerAddressPort.split(":");
            String address = parts[0];
            int port = Integer.parseInt(parts[1]);

            peer.connectToPeer(address, port);
        }

        new Thread(peer).start(); // peer.run() ==> peer.startChat(); by new thread
        peer.listenForConnections();


    }

    public void connectToPeer(String address, int port) {
        try {
            Socket socket = new Socket(address, port);
            ConnectionHandlerTCP handler = new ConnectionHandlerTCP(socket, peerId, this);
            connections.add(handler);
            new Thread(handler).start();
            System.out.println("Connected to peer at " + address + ":" + port);
        } catch (IOException e) {
            System.err.println("Could not connect to " + address + ":" + port + ": " + e.getMessage());
        }
    }

    public void listenForConnections() {
        try (ServerSocket serverSocket = new ServerSocket(thisPeerPort)) {
            System.out.println("Listening on port " + thisPeerPort);
            while (true) {
                Socket socket = serverSocket.accept();
                ConnectionHandlerTCP handler = new ConnectionHandlerTCP(socket, peerId, this);
                connections.add(handler);
                new Thread(handler).start();
                System.out.println("New peer connected: " + socket.getInetAddress().getHostAddress()+ "@" + socket.getPort());
            }
        } catch (IOException e) {
            System.err.println("Error listening for connections: " + e.getMessage());
        }
    }

    public void sendMessage(String recipientAddress, int recipientPort, String message, String senderId) {
        try {
            Socket socket = new Socket(recipientAddress, recipientPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(senderId + ": " + message);
            socket.close();
        } catch (IOException e) {
            System.err.println("Error sending message to " + recipientAddress + ":" + recipientPort + ": " + e.getMessage());
        }
    }

    @Override
    public void run(){
        startChat();
    }

    public void startChat() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(peerId + PROMPT);
            String message = scanner.nextLine();
            if (message.equals("exit")) {
                break;
            }

            //Simple example - send to all connected peers
            for(ConnectionHandlerTCP connection : connections){
                sendMessage(connection.getAddress(), connection.getPort(), message, peerId);
            }

        }
        scanner.close();
        System.exit(0);
    }
}
