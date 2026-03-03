package tp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MySocketClient {
    static int port = 9632;

    public static void send_last_news_message(Socket socket){
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            //send request to server:
            out.write(MyRequestDataUtil.lastNewsMessageData());
            //waiting & retreive response:
            MyDataMessage responseDataMessage = MyDataMessage.readFromInputStream(in);
            System.out.println("responseMessage="+responseDataMessage.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void send_average_double_request(Socket socket){
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            //send request to server:
            double[] values= { 6.0, 8.0 , 10.0 , 12.0 , 14.0}; //expected average = 10
            byte[] requestData=MyRequestDataUtil.averageDoubleMessage(values);
            out.write(requestData);
            //waiting & retreive response:
            MyDataMessage responseDataMessage = MyDataMessage.readFromInputStream(in);
            System.out.println("responseMessage="+responseDataMessage.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static void main(String[] args) {
        /*
        byte[]  bytes = MyDataUtil.utf8Buffer32FromLittleString("coucou");
        String message = MyDataUtil.StringFromUtf8Buffer32(bytes);
        System.out.println("message="+message);
        */

        String host = (args.length>0)?args[0]:"localhost";
        InetAddress serveur = null;
        try {
            serveur = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        try  (Socket socket = new Socket(serveur, port)){
            send_last_news_message(socket);
            send_average_double_request(socket);
        } catch (Exception e) {
            e.printStackTrace();
        } //try with auto_closeable resource=socket .
    }
}
