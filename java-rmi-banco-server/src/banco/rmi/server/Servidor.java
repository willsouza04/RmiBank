package banco.rmi.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import banco.rmi.remote.IRemoteBanco;

public class Servidor implements IRemoteBanco{

	public static void main(String[] args) {
		
		try {
			System.out.println("Construindo Servidor Remoto...");
			Servidor servidor = new Servidor();
			IRemoteBanco stub = (IRemoteBanco)
					UnicastRemoteObject.exportObject(servidor,0);
			
			Registry registry = LocateRegistry.getRegistry(9876);
			
			registry.bind("Servidor_aula", stub);
			
			System.out.println("servidor iniciado...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private List<Usuario> usuarios = new ArrayList<>();;
	int conexoes = 0;	
	Instant inicio = null;
	Instant fim = null;

	@Override
	public void deposito(double vlr, int id) {
		Usuario user = new Usuario();
		user = checaUsuario(id);
		if (user == null) {
			System.out.println("Usuario nao cadastrado");
		}else {
			conexoes++;
			user.setSaldo(user.getSaldo() + vlr);
		}
	}

	@Override
	public double saldo(int id) {
		Usuario user = new Usuario();
		user = checaUsuario(id);
		if (user == null) {
			System.out.println("Usuario nao cadastrado");
			return (0);
		}else {
			return user.getSaldo();
		}
	}

	@Override
	public void saque(double vlr, int id) {
		Usuario user = new Usuario();
		user = checaUsuario(id);		
		if(inicio != null) {
			fim = Instant.now();
			Duration duracao = Duration.between(fim, inicio);
			long duracaoEmMilissegundos = duracao.toMillis();
			if(duracaoEmMilissegundos < -120000) {
				inicio = null;
				user.setSaldo(user.getSaldo() - vlr);
				user.setLimite(vlr);
			}
			else {
				int duracaoEmSegundos = 120 + (int)(duracaoEmMilissegundos / 1000);
				JOptionPane.showMessageDialog(null, "Limite de saque excedido!\nAguarde " + duracaoEmSegundos + " segundo.");
			}
		}else {
			if (user.getLimite() >= 500) {
				inicio = Instant.now();
				JOptionPane.showMessageDialog(null, "Limite de saque excedido!\nAguarde 2 minutos.");
			}else {
				user.setSaldo(user.getSaldo() - vlr);
				user.setLimite(user.getLimite() + vlr);
			}
		}
	}

	public Usuario checaUsuario(int id) {
		for (Usuario usuario : usuarios) {
			if (id == usuario.getId()){
				return usuario;
			}
		}
		return null;
	}

	@Override
	public void cadastrar(String nome, String cpf, String senha) throws RemoteException {
		Usuario u = new Usuario();
		u.setId(usuarios.size()+1);
		u.setNome(nome);
		u.setCpf(cpf);
		u.setSenha(senha);
		u.setSaldo(0);
		u.setLimite(0);
		usuarios.add(u);
	}

	@Override
	public String dados(int id) throws RemoteException {
		String dados = "";
		for (Usuario usuario : usuarios) {
			if (id == usuario.getId()){
				dados += usuario.getNome()+"/"+usuario.getCpf();
			}
		}
		return dados;
	}

	@Override
	public int login(String cpf, String senha) throws RemoteException {
		int id = 0;
		for (Usuario usuario : usuarios) {
			if (cpf.equals(usuario.getCpf())){
				if(senha.equals(usuario.getSenha())) {
					id = usuario.getId();
				}
			}
		}
		return id;
	}
}
