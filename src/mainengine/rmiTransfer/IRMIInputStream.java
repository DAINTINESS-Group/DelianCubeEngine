package mainengine.rmiTransfer;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRMIInputStream extends Remote {
    public byte[] readBytes(int len) throws IOException, 
    RemoteException;
    public int read() throws IOException, RemoteException;
    public void close() throws IOException, RemoteException;
	//public RMIPipe transfer(int key);
}
