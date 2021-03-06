package messages;

import chord.ChordManager;
import chord.ConnectionInfo;
import files.FileHandler;
import peer.Peer;
import protocols.Backup;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

public class ReclaimBackup extends Message {
    private int repDegree;
    private BigInteger hashFile;
    private ConnectionInfo ci;
    private byte[] body;
    private String ipAddress;
    private int port;

    public ReclaimBackup(ConnectionInfo ci, BigInteger hashFile, int repDegree, byte[] body,String ipAddress,int port) {
        this.ci = ci;
        this.hashFile = hashFile;
        this.repDegree = repDegree;
        this.body = body;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Override
    public int getPort() {
        return this.port;
    }
    @Override
    public String toString() {
        return "RECLAIM_BACKUP " + this.hashFile + " " + this.repDegree;
    }

    public void handleMessage() {
        if (Peer.storage.getSpaceOcupied() + body.length > Peer.storage.getSpaceReserved()) {
            System.out.println("Not enough space to save file");
            MessageForwarder.sendMessage(new ReclaimBackup(ci, hashFile, repDegree, body, ChordManager.getFingerTable().get(0).getIp(), ChordManager.getFingerTable().get(0).getPort()));
        } else { 
            if(FileHandler.checkFileExists("./peerDisk/peer" + Peer.getPeerAccessPoint() + "-"  + ChordManager.peerHash + "/backup/" + hashFile)){
                MessageForwarder.sendMessage(new ReclaimBackup(ci, hashFile, repDegree, body, ChordManager.getFingerTable().get(0).getIp(), ChordManager.getFingerTable().get(0).getPort()));
            } else {

            try {
                FileHandler.writeFile("./peerDisk/peer" + Peer.getPeerAccessPoint() + "-" + ChordManager.peerHash + "/backup/" + hashFile, body);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) { 
                e.printStackTrace();
            }
            }
        }
    }
}
