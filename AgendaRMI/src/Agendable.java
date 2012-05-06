

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface Agendable extends Remote{
	
	Contato inserir(Contato contato, String ip) throws RemoteException;
	List<Contato> consultar(String termoBusca, String ip) throws RemoteException;
	Contato atualizar(Contato contato, String ip) throws RemoteException;
	Contato remover(Contato contato, String ip) throws RemoteException;
	void adicionarCliente(Notifiable notificador) throws RemoteException;
	void removerCliente(Notifiable notificador) throws RemoteException;
        void derrubarCliente(String ip) throws RemoteException;
}
