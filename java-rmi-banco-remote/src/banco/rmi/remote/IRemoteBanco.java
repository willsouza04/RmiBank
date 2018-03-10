package banco.rmi.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteBanco extends Remote{

	public void saque(double valor, int id)throws RemoteException;
	public void deposito(double valor, int id)throws RemoteException;
	public void cadastrar(String nome, String cpf, String senha)throws RemoteException;
	public int login(String cpf, String senha)throws RemoteException;
	public String dados(int id)throws RemoteException;
	public double saldo(int id)throws RemoteException;
}
