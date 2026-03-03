package tp;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;

public class MyDataUtil {


   public static void dumpDoubleValues(double values[]){
       for(int i=0;i<values.length;i++)
           System.out.println("values["+i+"]="+values[i]);
   }

   public static byte[] bytesFromDoubleArray(double array[]){
       int n= array.length;
       ByteBuffer buff = ByteBuffer.allocate(8*n);
       for(int i=0;i<n;i++) {
           buff.putDouble(i*8, array[i]);
       }
       return buff.array();
   }

    public static double[] extractDoubleArrayFromExtraBytes(byte[] extra_data){
       int s=8 ; //each double = 8 bytes
       int n =  extra_data.length / 8;
        //read array of double
        ByteBuffer buffer = ByteBuffer.allocate(Double.BYTES); //8
        double[] values=new double[n];
        for(int i=0;i<n;i++) {
            for(int j=0;j<8;j++)
                buffer.put(extra_data[i*8+j]);
            buffer.rewind();
            values[i]=buffer.getDouble();
            buffer.rewind();
        }
        return values;
    }




    //transform little string (<=64 char) as utf8 bytes array of fixed size=32
    public static byte[] utf8Buffer64FromLittleString(String s){
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        //System.out.println("initial length:" + bytes.length);
        byte[] expanded = Arrays.copyOf(bytes, 64); //fill last chars by \0
        //System.out.println("expanded length:" + expanded.length);
        return expanded;
    }

    //transform utf8 bytes array of fixed size=64 to little String
    public static String stringFromUtf8Buffer64(byte[] bytes){
        String s = new String(bytes, StandardCharsets.UTF_8);
        //System.out.println("s="+s);
        int firstNullPos = s.indexOf('\0');
        s=s.substring(0,firstNullPos); //remove  null chars at end of string
        //System.out.println("s="+s);
        return s;
    }
}
