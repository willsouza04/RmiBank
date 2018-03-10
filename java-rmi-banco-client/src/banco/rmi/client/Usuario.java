package banco.rmi.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JOptionPane;

import banco.rmi.remote.IRemoteBanco;

public class Usuario {

public static void main(String[] args) {
		
		System.out.println("Registrando no servidor remoto...");		
		
		try {
			Registry registry = LocateRegistry.getRegistry(9876);			
			int id_usuario = 0;
			
			IRemoteBanco stub =  (IRemoteBanco)
					registry.lookup("Servidor_aula");
			int flag = 1;
			
			while(flag != 0) {
				
				if(id_usuario == 0) {
					int opcao = Integer.parseInt((JOptionPane.showInputDialog(""
							+ "1-Entrar \n"
							+ "2-Cadastrar \n"
							+ "0-Sair")));
					
					switch(opcao) {
					case 1:
						String cpf_login = JOptionPane.showInputDialog("CPF");
						String senha_login = JOptionPane.showInputDialog("senha");
						id_usuario = stub.login(cpf_login, senha_login);
						break;
					case 2:
						String nome = JOptionPane.showInputDialog("nome");
						String cpf = JOptionPane.showInputDialog("CPF");
						String senha = JOptionPane.showInputDialog("senha");
						stub.cadastrar(nome, cpf, senha);
						break;
					case 0:
						flag = 0;
						break;
					}			
				}	
				else {
					String dados[] = stub.dados(id_usuario).split("/"); 
					
					int opcao = Integer.parseInt((JOptionPane.showInputDialog(""
							+ "Nome: " + dados[0]
							+ "\nCPF: " + dados[1]
							+ "\n____________________\n"
							+ "1-deposito \n"
							+ "2-saque \n"
							+ "3-saldo \n"
							+ "0-sair")));
					switch (opcao) {
					case 1:
						stub.deposito(Double.parseDouble(JOptionPane.showInputDialog("Digite o valor do deposito")),
								id_usuario);
						break;
					case 2:	
						stub.saque(Double.parseDouble(JOptionPane.showInputDialog("Digite o valor do saque")),
								id_usuario);
						break;						
					case 3:
						JOptionPane.showMessageDialog(null, "R$ " + stub.saldo(id_usuario));
						break;
					case 0:
						id_usuario = 0;
						break;
					}
				}		
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
