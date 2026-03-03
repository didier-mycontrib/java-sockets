package tp;

public class MyRequestDataUtil {
    public static byte[] lastNewsMessageData(){
        MyDataMessage dataMessage = new MyDataMessage("last_news");
        return dataMessage.get_main_data();
    }

    public static byte[] averageDoubleMessage(double[] values){
        MyDataMessage dataMessage = new MyDataMessage("average_double",values.length, 8,MyDataUtil.bytesFromDoubleArray(values));
        return dataMessage.get_full_data();
    }

}
