

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


@SuppressWarnings("serial")
public class Notificador extends UnicastRemoteObject implements Notifiable{
	
    Updatable tela;
    String ip;

    protected Notificador(Updatable tela) throws RemoteException {
            super();
            obterIp();
            this.tela = tela;
    }

    @Override
    public void notifiqueMe(String name) throws RemoteException {
            tela.atualizarTela(name);

    }

    private void obterIp() {
        try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                System.out.println("deu erro na hora de pegar o ip");
                e.printStackTrace();
            }
    }

    @Override
    public String ip() {
        return ip;
    }

    @Override
    public void fecheMe() throws RemoteException {
        tela.fecheMe();
    }

}
