package tp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MySocketServer {
    static int port =9632; //par defaut
    public static void main(String[] args) {
        try {
            ServerSocket socketServeur = new ServerSocket(port);
            System.out.println("Lancement du serveur MySocketServer port="+port);
            while (true) {
                Socket socketClient = socketServeur.accept();
                Thread t = new Thread(new MySocketTask(socketClient));
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
