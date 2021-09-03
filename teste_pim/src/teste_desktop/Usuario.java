package teste_desktop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

/**
 * Obs. Nenhuma das classes tem getters ou setters porque não foram utilizados em nenhum momento
 */
public class Usuario {
	static Scanner teclado = new Scanner(System.in);
	
	/**
	 * Metodo de login do sistema
	 * Todos os Comandos sysout servem somente para testar as funcionalidades, provavelmente 
	 * devem ser substituidos na interface grafica
	 */
	public void login() {
		String username, senha;
		String BUSCA_USER = "SELECT * FROM usuarios WHERE username = ? AND senha = ? AND codigo > 1";
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
				if(resultado.getInt(4) == 3) {
					System.out.println("Bem vindo Administrador " + resultado.getString(2));
					menu_adm();
					conect.desconectar(con);
				}else {
					System.out.println("Bem vindo! " + resultado.getString(2));
					menu();
					conect.desconectar(con);
				}
			}else {
				System.out.println("Nome de usuario e/ou senha incorreto(s)");
				conect.desconectar(con);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um erro no login");
		}
		
	}
	
	/**
	 * Menu inicial do sistema
	 * OBS. O menu está sendo controlado por teclas apenas por razão de teste, provalmente será mudado na
	 * interface grafica, só deve manter a chamada dos metodos
	 */
	public void menu() {
		Funcionario func = new Funcionario();
		boolean sair = false;
		int opcao;
		do {
			System.out.println("-----Menu Inicial Fox Trade------");
			System.out.println("1-Listar Clientes Inadimplentes");
			System.out.println("2-Buscar Cliente especifico");
			System.out.println("3-Mostrar relatorio");
			System.out.println("4-Sair");
			System.out.println("Digite a opção desejada: ");
			opcao = Integer.parseInt(teclado.nextLine());
			if(opcao == 1) {
				func.listar_inadimplentes();
			}else if(opcao == 2) {
				func.verificar_cliente();
			}else if(opcao == 3) {
				func.mostrar_relatorios();
			}else if(opcao == 4) {
				sair = true;
			}
		}while(!sair);
	}
	
	/**
	 * Modo de administrador do menu, com as funcionalidades que são exclusivas de adm
	 * é controlado pelo campo codigo, que todo usuario adm possui o codigo 3
	 */
	public void menu_adm() {
		Administrador adm = new Administrador();
		boolean sair = false;
		int opcao;
		do {
			System.out.println("-----Menu Inicial Fox Trade(ADMIN MODE)------");
			System.out.println("1-Listar clientes inadimplentes");
			System.out.println("2-Buscar cliente especifico");
			System.out.println("3-Mostrar relatorios");
			System.out.println("4-Cadastrar funcionario");
			System.out.println("5-Verificar funcionario especifico");
			System.out.println("6-Sair");
			System.out.println("Digite a opção desejada: ");
			opcao = Integer.parseInt(teclado.nextLine());
			if(opcao == 1) {
				adm.listar_inadimplentes();
			}else if(opcao == 2) {
				adm.verificar_cliente();
			}else if(opcao == 3) {
				adm.mostrar_relatorios();
			}else if(opcao == 4) {
				adm.cadastrar_func();
			}else if(opcao == 5) {
				adm.verificar_func();
			}else if(opcao == 6) {
				sair = true;
			}
		}while(!sair);
	}

}
