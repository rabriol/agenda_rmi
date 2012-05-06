

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class AgendaClient {
	
	private Agendable agenda = null;
	private Notifiable notificador = null;
        private String host;
	
	public AgendaClient(Updatable tela, String host) throws RemoteException {
            this.host = host;
            configurarNotificador(tela);
            obterConexaoComServidor();
	}

	public Contato inserirNovoContato(Contato contato) throws RemoteException {
		agenda.inserir(contato, notificador.ip());
		return contato;
		
	}

	public List<Contato> pesquisarContato(String termo) throws RemoteException {
		return agenda.consultar(termo, notificador.ip());
	}
        
        public void removerContato(Contato contato) throws RemoteException {
                agenda.remover(contato, notificador.ip());
        }
        
        public Contato atualizarContato(Contato contato) throws RemoteException {
                return agenda.atualizar(contato, notificador.ip());
        }
 	
	private void configurarNotificador(Updatable tela) throws RemoteException {
		notificador = new Notificador(tela);
		
	}
	
	private void obterConexaoComServidor() throws RemoteException {
            if (agenda == null) {
                try {
                        //System.setSecurityManager(new RMISecurityManager());
                        System.out.println("obtendo conexao com o server...");

                        Registry registry = LocateRegistry.getRegistry(host);
                        agenda = (Agendable)registry.lookup("Agendable");

                        System.out.println("conexao obtida com sucesso...");

                        agenda.adicionarCliente(notificador);

                } catch (AccessException e) {
                        e.printStackTrace();
                        //throw new RemoteException(e.getLocalizedMessage());
                } catch (NotBoundException e) {
                        e.printStackTrace();
                        //throw new RemoteException(e.getLocalizedMessage());

                }
            }
	}
	
	public void fecharConexao() throws RemoteException {
		agenda.removerCliente(notificador);
	}
}
