package tp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ChatTaskUDP implements Runnable{
    private static final String PROMPT = "> ";

    private String peerId;
    private DatagramSocket thisPeerDatagramSocket;
    private Set<Peer> otherPeers;  //list of "adresse_ip:num_port"

    public ChatTaskUDP(String peerId,DatagramSocket thisPeerDatagramSocket,Set<Peer> otherPeers){
        this.peerId=peerId;
        this.thisPeerDatagramSocket = thisPeerDatagramSocket;
        this.otherPeers=otherPeers;
    }

    public void run(){
        startChat();
    }

    public void startChat() {
        Scanner scanner = new Scanner(System.in);
        boolean first=true;
        while (true) {
            String message = "";
            if(first){
                message=this.peerId + "is ready for chat";
                first=false;
            }else {
                System.out.print(peerId + PROMPT);
                message = scanner.nextLine();
                if (message.equals("exit")) {
                    break;
                }
            }

            //Simple example - send to all other peers

            for (Peer otherPeer : this.otherPeers) {
                sendMessage(otherPeer, message, peerId);
            }

        }
        scanner.close();
        System.exit(0);
    }

    public void sendMessage(Peer otherPeer, String message, String senderId) {
        try {
            String fullMessage=senderId + ": " + message;
            byte bufferEnvoi[] = fullMessage.getBytes();
            DatagramPacket paquetEnvoi = new DatagramPacket(bufferEnvoi, bufferEnvoi.length,
                                                            otherPeer.getInetAddress(), otherPeer.getPort());
            thisPeerDatagramSocket.send(paquetEnvoi);
        } catch (IOException e) {
            System.err.println("Error sending message to " + otherPeer.getAddressAndPort() + " : " + e.getMessage());
        }
    }
}
