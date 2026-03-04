package tp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class MyServerUDP {
    final static int port = 9633;

    public static void main(String argv[]) throws Exception {
        final int TAILLE_BUFFER = 1024;
        byte buffer[] = new byte[TAILLE_BUFFER];
        DatagramSocket serverDatagramSocket= new DatagramSocket(port);

        System.out.println("Lancement du serveur MyServerUDP");
        while (true) {
            DatagramPacket paquetReception = new DatagramPacket(buffer, buffer.length);

            //remplissage du paquet avec données entrantes recues et informations (ip:port) de l'émetteur
            serverDatagramSocket.receive(paquetReception);

            System.out.println("\npaquetReception émis par"+paquetReception.getAddress()+ ":" +  paquetReception.getPort());
            int taille = paquetReception.getLength();
            String donneesRecues = new String(paquetReception.getData(),0, taille);
            System.out.println("Donnees reçues = "+donneesRecues);

            String message = "Bonjour "+donneesRecues;
            byte[] responseData= message.getBytes();
            System.out.println("Donnees retournées = "+message);
            DatagramPacket paquetEnvoi = new DatagramPacket(responseData,
                    responseData.length, paquetReception.getAddress(), paquetReception.getPort());
            //on envoi vers le destinataire spécifié au niveau de paquetEnvoi (ici l'emmeteur de la requête):
            serverDatagramSocket.send(paquetEnvoi);
        }
    }
}