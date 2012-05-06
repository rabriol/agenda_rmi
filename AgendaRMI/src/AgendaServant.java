

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@SuppressWarnings("serial")
public class AgendaServant extends UnicastRemoteObject implements Agendable {
	
	private List<Notifiable> notificadores;
	
	private int versao, contadorId;
	private List<Contato> contatos;
	private TelaServidor telaServidor;
        
	public AgendaServant(TelaServidor telaservidor) throws RemoteException {
		super();
                this.telaServidor = telaservidor;
		contatos = new ArrayList<Contato>();
		notificadores = new ArrayList<Notifiable>();
		versao = 0;
		contadorId = 0;
	}
	
	@Override
	public Contato inserir(Contato contato, String ip) throws RemoteException{
		contato.setId(contadorId);
                contatos.add(contato);
                
                versao++;
		contadorId++;
                telaServidor.atualizarOperacoesDosClientes(montaFraseBroadcast("inserçao", ip));
		realizarBroadcast("inserçao", ip);
                
                System.out.println("gerou com sucesso o contato");
		return contato;
		
	}

	@Override
	public List<Contato> consultar(String termoBusca, String ip) throws RemoteException{
		versao++;
                String termo = termoBusca.trim();
                List<Contato> contatosResult = new ArrayList<Contato>();
                
                for (int i = 0 ; i < contatos.size() ; i++) {
                    if (contatos.get(i).getNome().contains(termo)
                            || contatos.get(i).getNome().startsWith(termo)
                            || contatos.get(i).getNome().endsWith(termo)) {
                        contatosResult.add(contatos.get(i));
                    }
                }
                
                telaServidor.atualizarOperacoesDosClientes(montaFraseBroadcast("pesquisa", ip));
                realizarBroadcast("pesquisa", ip);
		return contatosResult;
		
	}

	@Override
	public Contato atualizar(Contato contato, String ip) throws RemoteException{
                for (int i = 0 ; i < contatos.size() ; i++) {
                    if (contatos.get(i).equals(contato)) {
                        contatos.set(i, contato);
                    }
                }
                
                versao++;
                telaServidor.atualizarOperacoesDosClientes(montaFraseBroadcast("atualizacao", ip));
		realizarBroadcast("atualizacao", ip);
                
		return contato;
	}
	
	@Override
	public Contato remover(Contato contato, String ip) throws RemoteException{
		contatos.remove(contato);
		
                versao++;
                telaServidor.atualizarOperacoesDosClientes(montaFraseBroadcast("remocao", ip));
		realizarBroadcast("remocao", ip);
		
                return contato;
	}
	
	@Override
	public synchronized void adicionarCliente(Notifiable notificador) throws RemoteException {
		if (notificador == null) {
			telaServidor.atualizarOperacoesDosClientes("[SERVIDOR VERSAO:"+ versao +"] ERRO: recebi um notificador nulo");
                        return;
		}
		
                //for (Notifiable notif : notificadores) {
                //    if (notif.ip().equals(notificador.ip())) {
                //        System.out.println("O IP "+ notificador.ip() +" ja esta conectado ao servidor!");
                //        throw new ConexaoRecusadaException("O IP "+ notificador.ip() +" ja esta conectado ao servidor!");
                //    }
                //}
                
		notificadores.add(notificador);
                
                versao++;
                String notificacao = "[SERVIDOR VERSAO:"+ versao +"] a máquina "+ notificador.ip() +" se conectou";
                
                telaServidor.atualizaQuantidadeClientesConectados(notificadores.size());
                telaServidor.adicionarClienteConectado(notificador.ip());
                telaServidor.atualizarOperacoesDosClientes(notificacao);

                
		for (Notifiable notif : notificadores) {
			Notifiable client = (Notifiable)notif;
			if (!notif.equals(notificador))
			client.notifiqueMe(notificacao);
		}
	}
	
	public synchronized void removerCliente(Notifiable notificador) throws RemoteException {
		if (notificador == null) {
			telaServidor.atualizarOperacoesDosClientes("[SERVIDOR VERSAO:"+ versao +"] ERRO: recebi um notificador nulo");
                        return;
		}
		
		notificadores.remove(notificador);
                versao++;
                String notificacao = "[SERVIDOR VERSAO:"+ versao +"] a máquina "+ notificador.ip() +" se desconectou";
                
                telaServidor.atualizaQuantidadeClientesConectados(notificadores.size());
                telaServidor.removerClienteConectado(notificador.ip());
                telaServidor.atualizarOperacoesDosClientes(notificacao);
                
		for (Notifiable notif : notificadores) {
			Notifiable client = (Notifiable)notif;
			client.notifiqueMe(notificacao);
		}
	}
        
        @Override
        public void derrubarCliente(String ip) throws RemoteException {
            
            Iterator it = new ArrayList(notificadores).iterator();
            Notifiable notificadorAux = null;
            while (it.hasNext()) {
                Notifiable notif = (Notifiable)it.next();
                if (notif.ip().equals(ip)) {
                    notificadores.remove(notif);
                    notificadorAux = notif;
                }
            }
            
            if (notificadorAux != null) {
                versao++;
                String notificacao = "[SERVIDOR VERSAO:"+ versao +"] a máquina "+ ip +" foi derrubada pelo servidor";

                telaServidor.atualizaQuantidadeClientesConectados(notificadores.size());
                telaServidor.removerClienteConectado(ip);
                telaServidor.atualizarOperacoesDosClientes(notificacao);
                
                notificadorAux.fecheMe();
            } else {
                versao++;
                String notificacao = "[SERVIDOR VERSAO:"+ versao +"] nao foram encontradas nenhuma máquina para ser derrubada pelo servidor";

                telaServidor.atualizaQuantidadeClientesConectados(notificadores.size());;
                telaServidor.atualizarOperacoesDosClientes(notificacao);
            } 
        }
	
	private void realizarBroadcast(String operacao, String ip) throws RemoteException {
		for (Notifiable notif : notificadores) {
			Notifiable client = (Notifiable)notif;
			client.notifiqueMe(montaFraseBroadcast(operacao, ip));
		}
	}
        
        private String montaFraseBroadcast(String operacao, String ip) {
            return "[SERVIDOR VERSAO:"+ versao +"] DIZ: A máquina " + ip + 
					" realizou a operaçao "+operacao;
        }
}
