package tp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * avec réutilisation de MySimpleMessage.writeSimpleMessage
 */
public class MyMessageWithDoubleArray {
    public static void writeMessageWithData(DataOutputStream dataOutputStream , String message , double[] data){
        MySimpleMessage.writeSimpleMessage(dataOutputStream,message); //write main part of message
        try {
            dataOutputStream.writeInt(data.length);//write array size
            for(double d : data)
                 dataOutputStream.writeDouble(d); //write array data
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static double[] readMessageData(DataInputStream dataInputStream){
        double data[]=null;
        try {
            int n = dataInputStream.readInt();//read array size
            data = new double[n];
            for(int i=0;i<n;i++)
                data[i]=dataInputStream.readDouble(); //read array data
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
