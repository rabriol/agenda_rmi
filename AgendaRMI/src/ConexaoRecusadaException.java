
import java.rmi.RemoteException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rafaeuoliveira
 */
public class ConexaoRecusadaException extends RemoteException{
    
    public ConexaoRecusadaException() {
        super();
    }
    
    public ConexaoRecusadaException(String name) {
        super(name);
    }
}
