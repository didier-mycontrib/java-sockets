package tp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MySocketClient {
    static int port = 9632;   //filtrage wireshark : tcp.port==9632

    public static void send_last_news_message(DataInputStream dataIn, DataOutputStream dataOut){
            //send request to server:
            MySimpleMessage.writeSimpleMessage(dataOut,"last_news");
            System.out.println("lastNews request sent , waiting for response ...");
            //waiting & retreive response:
            String lastNews = MySimpleMessage.readSimpleMessage(dataIn);
            System.out.println("responseMessage (lastNews)="+lastNews);
    }

    public static void send_average_double_request(DataInputStream dataIn, DataOutputStream dataOut){
            //send request to server:
            double[] values= { 6.0, 8.0 , 10.0 , 12.0 , 14.0}; //expected average = 10
            MyMessageWithDoubleArray.writeMessageWithData(dataOut,"average_double",values);
            //waiting & retreive response:
            String averageResponseMessage = MySimpleMessage.readSimpleMessage(dataIn);
            System.out.println("responseMessage (average of double array)="+averageResponseMessage);
    }


    public static void main(String[] args) {
             String host = (args.length>0)?args[0]:"localhost";
        InetAddress serveur = null;
        try {
            serveur = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        try  (Socket socket = new Socket(serveur, port)){
            InputStream in = socket.getInputStream();
            DataInputStream dataIn = new DataInputStream(in);
            OutputStream out = socket.getOutputStream();
            DataOutputStream dataOut = new DataOutputStream(out);
            send_last_news_message(dataIn,dataOut);
            send_average_double_request(dataIn,dataOut);
        } catch (Exception e) {
            e.printStackTrace();
        } //try with auto_closeable resource=socket .
    }
}
