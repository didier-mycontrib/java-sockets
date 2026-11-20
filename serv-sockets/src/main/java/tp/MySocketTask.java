package tp;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;


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
            DataInputStream dataIn = new DataInputStream(in);
            DataOutputStream dataOut = new DataOutputStream(out);


            boolean endLoop=false;
            while(!endLoop) {
                //request/response loop
                //MyDataMessageV1 requestDataMessage = MyDataMessageV1.readFromInputStream(in);
                String requestMessage = null;
                try {
                    requestMessage = MySimpleMessage.readSimpleMessage(dataIn);
                } catch (EndReadLoopException e) {
                    endLoop=true;
                }
                byte[] responseData = null;
                System.out.println("received requestMessage=" + requestMessage);
                if(requestMessage!=null)
                  switch (requestMessage) {

                    case "average_double":
                        double[] values = MyMessageWithDoubleArray.readMessageData(dataIn);
                        int n= values.length;
                        double sum=0;
                        for(int i=0;i<n;i++){
                            sum+=values[i];
                        }
                        String averageResponseMsg = "for received array of double of size= " + n + " computed average=" + sum/n;
                        MySimpleMessage.writeSimpleMessage(dataOut,averageResponseMsg);
                        System.out.println("average_double response sent : "+averageResponseMsg);
                        break;
                    case "last_news":
                        String lastNewsResponseMsg = "current time (server side) is " + LocalDateTime.now().toString();
                        MySimpleMessage.writeSimpleMessage(dataOut,lastNewsResponseMsg);
                        System.out.println("last_news response sent : "+lastNewsResponseMsg);
                        break;
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
