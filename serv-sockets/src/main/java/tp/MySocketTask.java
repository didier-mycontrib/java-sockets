package tp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class MySocketTask implements Runnable {

    private Socket socketClient;

    public MySocketTask(Socket socketClient){
        this.socketClient=socketClient;
    }

    @Override
    public void run() {
        String clientInetAddressAsString="?";
        try {
            clientInetAddressAsString = socketClient.getInetAddress().toString();
           System.out.println("Connexion avec : "+clientInetAddressAsString);

            InputStream in = socketClient.getInputStream();
            OutputStream out = socketClient.getOutputStream();


            boolean endLoop=false;
            while(!endLoop) {
                //request/response loop
                MyDataMessage requestDataMessage = MyDataMessage.readFromInputStream(in);
                String requestMessage = requestDataMessage.getMessage();
                byte[] responseData = null;
                System.out.println("received requestMessage=" + requestMessage);
                endLoop=!requestDataMessage.isOk();
                switch (requestMessage) {
                    case "average_double":
                        double[] values = MyDataUtil.extractDoubleArrayFromExtraBytes(requestDataMessage.getExtra_data());
                        responseData = MyResponseDataUtil.averageDoubleResponseMessageData(values);
                        out.write(responseData);
                        break;

                    case "last_news":
                        responseData = MyResponseDataUtil.lastNewsResponseMessageData();
                        out.write(responseData);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {  socketClient.close();  } catch (IOException e) {
                //throw new RuntimeException(e);
            }
            System.out.println("socket closed for client="+clientInetAddressAsString);
        }
    }
}
