package mainengine;

import java.rmi.Remote;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;


public interface IServer extends Remote {
    	public abstract OutputStream getOutputStream(File f) throws IOException;
    	public abstract InputStream getInputStream(File f) throws IOException;
        //public String sayHello() throws RemoteException;
}
