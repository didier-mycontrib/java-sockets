package tp;


import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

//dans le contexte du TP les messages echangés auront toujours cette structure commune
//paquet d'octets avec dans l'ordre:
// 64 premiers octets = utf8 bytes array of message string (with trailing null chars \0 to ignore)
// 4 octets suivants = n (as int) = number of entries in array
// 4 octets suivants = s (as int) = size of each entry of array
// si n>0, eventuels octets suivants = n*s bytes = all bytes of array of ...
//exemples:
// "last_news" , 0 , 0
// "average_double" , 4 , 8 , array of 4 doubles
// ...
public class MyDataMessage {
    private String message; //partie principale du message véhiculée en utf8 sur tableau de taille 64
    private int n; //nombre d'éléments de l'éventuel tableau des données complémentaires
    private int s;// taille unitaire des éléments du tableau des données complémentaires
    private byte[] extra_data; // null par défaut, éventuelle partie complémentaire (de taille = n*s bytes)
    private boolean ok=true;

    //construit et retourne la partie principale du message (message + n + s) sur 64 + 4 + 4 =72 octets
    public byte[] get_main_data(){
        byte[] mainData = new byte[64+4+4];
        byte[] messageData = MyDataUtil.utf8Buffer64FromLittleString(this.message);
        for(int i=0;i<64;i++) mainData[i]=messageData[i];
        byte[] nData = ByteBuffer.allocate(4).putInt(n).array();
        for(int i=0;i<4;i++) mainData[i+64]=nData[i];
        byte[] sData = ByteBuffer.allocate(4).putInt(s).array();
        for(int i=0;i<4;i++) mainData[i+68]=sData[i];
        return mainData;
    }


    //construit et retourne le message complet:
    public byte[] get_full_data(){
        byte[] mainData = this.get_main_data(); //de taille 64+4+4=72
        if(n==0) return mainData;
        /*else*/
        byte[] fullData = Arrays.copyOf(mainData, 72+n*s);
        for(int i=0;i<s*n;i++)
            fullData[i+72]=this.extra_data[i];
        return fullData;
    }

    public void buildMainMessageFromMainData(byte[] mainData){
        this.message=MyDataUtil.stringFromUtf8Buffer64(mainData);//extraction du message depuis les 64 premiers octets
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        //read n = number of items in array:
        int delta=64;
        for(int i=0;i<4;i++)
            buffer.put(mainData[i+delta]);
        buffer.rewind();
        this.n = buffer.getInt(); //number of items in arrays
        //System.out.println("n="+n);//ok

        //read s = size of items in array:
        delta=68; //64+4
        buffer.rewind();
        for(int i=0;i<4;i++)
            buffer.put(mainData[i+delta]);
        buffer.rewind();
        this.s = buffer.getInt(); //size of items in arrays (ex: 8 = size of double)
        //System.out.println("s="+s); //ok
    }

    public MyDataMessage(String message, int n, int s, byte[] extra_data) {
        this.message = message;
        this.n = n;
        this.s = s;
        this.extra_data = extra_data;
    }

    public MyDataMessage(String message){
        this(message,0,0,null);
    }

    public MyDataMessage(){
        this("");
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getExtra_data() {
        return extra_data;
    }

    public void setExtra_data(byte[] extra_data) {
        this.extra_data = extra_data;
    }

    public int getN() {
        return n;
    }

    public int getS() {
        return s;
    }

    public boolean isOk() {
        return ok;
    }

    public int getExtraDataFullSize(){
        return this.n * this.s;
    }

    public static MyDataMessage readFromInputStream(InputStream in){
        MyDataMessage dataMessage = new MyDataMessage();
        try {
            byte[] mainFirstPartOfData = in.readNBytes(72); //64+4+4=72 (message+n+s)
            if(mainFirstPartOfData.length==0){
                //nothing more to read
                dataMessage.ok=false;
                //throw new RuntimeException("end or reading ...");
                dataMessage.message="end or reading ..."; dataMessage.n=0;
            }
            dataMessage.buildMainMessageFromMainData(mainFirstPartOfData);
            if(dataMessage.n>0)
                dataMessage.setExtra_data(in.readNBytes(dataMessage.getExtraDataFullSize()));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return dataMessage;
    }
}
