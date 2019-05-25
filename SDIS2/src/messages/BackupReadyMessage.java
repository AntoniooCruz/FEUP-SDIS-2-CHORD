package messages;

import chord.ChordInfo;
import chord.ConnectionInfo;
import files.FileHandler;
import peer.Peer;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

public class BackupReadyMessage extends Message {
    private ConnectionInfo ci;
    private String filename;
    private int repDegree;
    private BigInteger hashFile;
    private String ipAddress;
    private int port;

    public BackupReadyMessage(ConnectionInfo ci, BigInteger hashFile, int repDegree, String filename,String ipAddress,int port) {
        this.hashFile = hashFile;
        this.repDegree = repDegree;
        this.ci = ci;
        this.filename = filename;
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
        return "BACKUP-READY" + this.hashFile + " " + " " + this.filename + " " + this.repDegree;
    }

    public void handleMessage() {

        try {
            byte[] content = FileHandler.readFromFile("./testFiles/" + filename);

            BigInteger fileHash = FileHandler.encrypt(filename);

            MessageForwarder.sendMessage(new BackupMessage(new ConnectionInfo(ChordInfo.peerHash, InetAddress.getLocalHost().getHostAddress(), Peer.port), fileHash, repDegree, content, ci.getIp(), ci.getPort()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}