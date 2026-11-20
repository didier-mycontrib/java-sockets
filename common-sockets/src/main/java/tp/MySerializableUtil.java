package tp;

import java.io.*;


public class MySerializableUtil {

    public static byte[] serializeData(Object data){
        byte[] serializedBuffer = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(data);
            serializedBuffer=baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return serializedBuffer;
    }


    public static  Object  deserializeData(byte[] serializedBuffer){
        Object deserializedObject=null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(serializedBuffer);
            ObjectInputStream ois = new ObjectInputStream(bais);
            deserializedObject =  ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return deserializedObject;
    }
}
