package mainengine.rmiTransfer;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRMIOutputStream extends Remote {
    public void write(int b) throws IOException, RemoteException;
    public void write(byte[] b, int off, int len) throws 
    IOException, RemoteException;
    public void close() throws IOException, RemoteException;
	//public void transfer(RMIPipe pipe);

}
