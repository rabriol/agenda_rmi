

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Notifiable extends Remote{
	void notifiqueMe(String name) throws RemoteException;
        String ip() throws RemoteException;
        void fecheMe() throws RemoteException;
}
