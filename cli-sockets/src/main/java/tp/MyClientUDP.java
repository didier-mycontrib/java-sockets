package tp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class MyClientUDP {
    final static int port = 9633;
    final static int TAILLE_BUFFER_RECEPTION = 1024;

    public static void main(String args[]) throws Exception {
        try {
            String host = (args.length>0)?args[0]:"localhost";
            InetAddress serveur = InetAddress.getByName(host);
            String message="James Bond"; //données à envoyer
            byte bufferEnvoi[] = message.getBytes();
            DatagramSocket clientDynamicDatagramSocket = new DatagramSocket(); //num port coté client pas important (alloué dynamiquement)
            DatagramPacket paquetEnvoi = new DatagramPacket(bufferEnvoi, bufferEnvoi.length, serveur, port);
            DatagramPacket paquetReception = new DatagramPacket(new byte[TAILLE_BUFFER_RECEPTION ], TAILLE_BUFFER_RECEPTION );

            clientDynamicDatagramSocket.setSoTimeout(30000);
            clientDynamicDatagramSocket.send(paquetEnvoi);
            clientDynamicDatagramSocket.receive(paquetReception);

            System.out.println("Message : " + new String(paquetReception.getData(),
                    0, paquetReception.getLength()));
            System.out.println("retourné par  : " + paquetReception.getAddress() + ":" +
                    paquetReception.getPort());
        } catch (SocketTimeoutException ste) {
            System.out.println("Le delai pour la reponse a expire");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
