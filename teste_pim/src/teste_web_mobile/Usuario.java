package teste_web_mobile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;


/**
 * Obs. Nenhuma das classes tem getters ou setters porque não foram utilizados
 */
public class Usuario {
	static Scanner teclado = new Scanner(System.in);
	private String username, senha;
	
	
	/**
	 * Metodo de login do sistema para o cliente, todos os sysouts no codigo inteiro são apenas
	 * para testar as funcionalidades, mas não irão existir no site, as entradas de dados e saídas
	 * de informação serão feitas na interface grafica
	 */
	public void login() {
		String BUSCA_USER = "SELECT * FROM usuarios WHERE username = ? AND senha = ? AND codigo = 1";
		System.out.println("Digite o seu nome de usuario: ");
		username = teclado.nextLine();
		System.out.println("Digite sua senha: ");
		senha = teclado.nextLine();
		try {
			Conexao conect = new Conexao();
			Connection con = conect.conectar();
			PreparedStatement usuario = con.prepareStatement(BUSCA_USER);
			usuario.setString(1, username);
			usuario.setString(2, senha);
			ResultSet resultado = usuario.executeQuery();
			resultado.last();
			int qtd = resultado.getRow();
			if(qtd>0) {
				System.out.println("Bem vindo(a) " + resultado.getString(2));
				menu();
			}else {
				System.out.println("Nome de usuario e/ou senha incorreto(s)");
				System.out.println("Deseja se cadastrar? (1 p/sim 2 p/não");
				int opcao = Integer.parseInt(teclado.nextLine());
				if(opcao == 1) {
					Cliente cli = new Cliente();
					cli.cadastrar();
				}
				conect.desconectar(con);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um erro no login");
		}
		
	}
	
	/**
	 * Menu inicial do sistema, a partir deste metodo todas as funcionalidades presentes no sistema
	 * serão chamadas
	 */
	public void menu() {
		Cliente cli = new Cliente(username, senha);
		boolean sair = false;
		int opcao;
		do {
			System.out.println("-----Menu Inicial Fox Trade------");
			System.out.println("1-Firmar contrato");
			System.out.println("2-Mostrar graficos");
			System.out.println("3-Mostrar relatorios");
			System.out.println("4-Cadastrar carteira digital");
			System.out.println("5-Mostrar historico de transações");
			System.out.println("6-Sair");
			System.out.println("Digite a opção desejada: ");
			opcao = Integer.parseInt(teclado.nextLine());
			if(opcao == 1) {
				cli.firmar_contrato();
			}else if(opcao == 2) {
				System.out.println("WIP");
			}else if(opcao == 3) {
				cli.mostrar_relatorios();
			}else if(opcao == 4) {
				cli.cadastrar_carteira_digital();
			}else if(opcao == 5) {
				cli.mostrar_historico_transacoes();
			}else if(opcao == 6) {
				sair = true;
			}
		}while(!sair);
	}

}
