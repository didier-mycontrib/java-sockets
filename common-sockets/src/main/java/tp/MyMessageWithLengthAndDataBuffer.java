package tp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * avec réutilisation de MySimpleMessage.writeSimpleMessage
 *
 * en plus du message (string) principal
 * cette classe permet d'écrire ou de lire des données binaires quelconques (pas trop longues)
 * -dabord la taille
 * -ensuite le tableau de byte
 */
public class MyMessageWithLengthAndDataBuffer {
    public static void writeMessageWithData(DataOutputStream dataOutputStream , String message , byte[] data){
        MySimpleMessage.writeSimpleMessage(dataOutputStream,message); //write main part of message
        try {
            dataOutputStream.writeInt(data.length);//write array size
            dataOutputStream.write(data);//write array data
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeMessageWithSerializedData(DataOutputStream dataOutputStream , String message , Object dataObject){
        writeMessageWithData(dataOutputStream,message,MySerializableUtil.serializeData(dataObject));
    }

    public static byte[] readMessageData(DataInputStream dataInputStream){
        byte data[]=null;
        try {
            int n = dataInputStream.readInt();//read array size
            data = new byte[n];
            dataInputStream.read(data);//read array data
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static Object readMessageDataObject(DataInputStream dataInputStream){
        return MySerializableUtil.deserializeData(readMessageData(dataInputStream));
    }
}
