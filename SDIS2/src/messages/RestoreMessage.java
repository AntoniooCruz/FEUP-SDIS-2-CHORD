package messages;

import chord.ChordManager;
import chord.ConnectionInfo;
import files.FileHandler;
import peer.Peer;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class RestoreMessage extends Message {
    private ConnectionInfo ci;
    private BigInteger hashFile;
    private String filename;
    private String ipAddress;
    private int port;

    public RestoreMessage(ConnectionInfo ci, BigInteger hashFile, String filename,String ipAddress,int port) {
        this.ci = ci;
        this.hashFile = hashFile;
        this.filename = filename;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public String toString() {
        return "RESTORE " + this.ci + " " + this.hashFile + " " + this.filename;
    }

    public void handleMessage() {

        if(FileHandler.checkFileExists("./peerDisk/peer" + Peer.getPeerAccessPoint() + "-" + ChordManager.peerHash + "/backup/" + hashFile)) {
            try {
                byte[] content = FileHandler.readFromFile("./peerDisk/peer" + Peer.getPeerAccessPoint() + "-" + ChordManager.peerHash  + "/backup/" + hashFile);

                MessageForwarder.sendMessage(new RestoreFile(ci, hashFile, filename, content, ci.getIp(), ci.getPort()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            MessageForwarder.sendMessage(new RestoreSucessor(ci, ChordManager.peerHash , hashFile, filename, ChordManager.getFingerTable().get(0).getIp(), ChordManager.getFingerTable().get(0).getPort()));
        }
    }
    @Override
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Override
    public int getPort() {
        return this.port;
    }
}
