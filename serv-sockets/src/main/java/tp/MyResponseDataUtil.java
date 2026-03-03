package tp;

import java.time.LocalDateTime;

public class MyResponseDataUtil {
    public static byte[] lastNewsResponseMessageData(){
        String responseMsg = "current time (server side) is " + LocalDateTime.now().toString();
        MyDataMessage dataMessage = new MyDataMessage(responseMsg);
        return dataMessage.get_main_data();
    }

    public static byte[] averageDoubleResponseMessageData(double[] values){
        int n= values.length;
        double sum=0;
        for(int i=0;i<n;i++){
            sum+=values[i];
        }
        String responseMsg = "for received array of double of size= " + n + " computed average=" + sum/n;
        MyDataMessage dataMessage = new MyDataMessage(responseMsg);
        return dataMessage.get_main_data();
    }
}
