package tp;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Peer {
    private String address;
    private int port;

    public Peer(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public Peer(String addressAndPort){
        String[] parts = addressAndPort.split(":");
        this.address = parts[0];
        this.port = Integer.parseInt(parts[1]);
    }

    public String getAddressAndPort(){
        return address+":"+port;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public InetAddress getInetAddress() {
        try {
            return InetAddress.getByName(address);
        } catch (UnknownHostException e) {
           e.printStackTrace();
           return null;
        }
    }
}
