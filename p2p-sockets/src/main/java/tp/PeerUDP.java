package tp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;

//NB: implements Runnable pour partie .run() ==> .startChat()
public class PeerUDP  {
    private static final int PORT = 12345;

    private int thisPeerPort = PORT; //or @...
    //NB: for simple exec on same host, peer_1 uses PORT by default ,
    // peer_2@12346 uses 12346, peer_3@12347 123467 etc
    private DatagramSocket thisPeerDatagramSocket;
    private Set<Peer> otherPeers;

    private String peerId;

    public PeerUDP(String peerId,int thisPeerPort) {
        this.peerId=peerId;
        this.thisPeerPort=thisPeerPort;
        try {
            this.thisPeerDatagramSocket=new DatagramSocket(thisPeerPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        this.otherPeers = new HashSet<>();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java Peer <peer_id> [<peer_address>:<peer_port>]...");
            return;
        }

        String peerId = args[0];
        int thisPeerPort=PORT;
        try {
            //Peer.thisPeerPort= value after @ in peer_...@....
            thisPeerPort= Integer.parseInt(peerId.split("@")[1]);
        } catch (Exception e) {
            //use default value PORT=12345
        }
        PeerUDP peer = new PeerUDP(peerId,thisPeerPort);

        for (int i = 1; i < args.length; i++) {
            String peerAddressPort = args[i];
            Peer otherPeer = new Peer(peerAddressPort);
            peer.otherPeers.add(otherPeer);
        }

        ChatTaskUDP chatTaskUDP = new ChatTaskUDP(peer.peerId,peer.thisPeerDatagramSocket,peer.otherPeers);
        new Thread(chatTaskUDP).start(); // chatTaskUDP.run() ==> chatTaskUDP.startChat(); by new thread
        peer.listenForMessages();


    }


    public void listenForMessages() {
        final int TAILLE_BUFFER = 1024;
        byte buffer[] = new byte[TAILLE_BUFFER];
        try{
            while (true) {
                DatagramPacket paquetReception = new DatagramPacket(buffer, buffer.length);

                //remplissage du paquet avec données entrantes recues et informations (ip:port) de l'émetteur
                thisPeerDatagramSocket.receive(paquetReception);

                //éventuelle mise à jour de la liste otherPeers si nécessaire
                String otherPeerAddressAndPort = paquetReception.getAddress().getHostAddress().toString()+":"+paquetReception.getPort();
                for(Peer existingOtherPeer : this.otherPeers){
                    if(!existingOtherPeer.getAddressAndPort().equals(otherPeerAddressAndPort)){
                        otherPeers.add(new Peer(otherPeerAddressAndPort));
                        break;
                    }
                }


                //System.out.println("\npaquetReception émis par" + paquetReception.getAddress().getHostAddress() + ":" + paquetReception.getPort());
                int taille = paquetReception.getLength();
                String donneesRecues = new String(paquetReception.getData(), 0, taille);
                System.out.println(donneesRecues);
            }
        } catch (IOException e) {
            System.err.println("Error listening for messages: " + e.getMessage());
        }
    }




}
