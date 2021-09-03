package teste_desktop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Funcionario extends Usuario {
	
	/**
	 * Metodo para buscar as informa��es de um cliente especifico passando o identificador
	 * da tabela
	 * 
	 * Obs. se achar melhor trocar a op��o de busca do ID para outros parametros de busca, sinta-se
	 * livre
	 * 
	 * Obs2. Uma das regras de negocio disse que dados sensiveis devem ser ocultos, n�o acho que
	 * as informa��es impressas configuram dados seniveis por�m se voce achar q alguma delas s�o
	 * sensiveis, pode retirar sem problema, s� toma cuidado pq se vc remover, vc tem q manter 
	 * os gets do result set do jeito que est�
	 */
	public void verificar_cliente() {
		String BUSCA_CLIENTE = "SELECT * FROM clientes WHERE id = ?";
		System.out.println("Digite o ID do cliente: ");
		int id = Integer.parseInt(teclado.nextLine());
		
		try {
			Conexao conect = new Conexao();
			Connection con = conect.conectar();
			PreparedStatement busca = con.prepareStatement(BUSCA_CLIENTE);
			busca.setInt(1, id);
			ResultSet resultado = busca.executeQuery();
			resultado.last();
			int qtd = resultado.getRow();
			if(qtd > 0) {
				System.out.println("ID: " + resultado.getInt(1));
				System.out.println("Nome: " + resultado.getString(2));
				System.out.println("Email: " + resultado.getString(4));
				System.out.println("Data de Nascimento: " + resultado.getDate(5));
				System.out.println("Telefone: " + resultado.getString(6));
				System.out.println("Endere�o: " + resultado.getString(7));
				System.out.println("Cidade: " + resultado.getString(8));
				System.out.println("Estado(UF): " + resultado.getString(9));
				System.out.println("Status: " + resultado.getString(10));
				System.out.println("ID_Usuario: " + resultado.getInt(11));
			}else {
				System.out.println("N�o existe cliente com o id informado");
			}
			resultado.close();
			conect.desconectar(con);	
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um erro na busca");
		}
		
	}
	
	/**
	 * Metodo que funciona como um sub-menu para o funcionario escolher qual relatorio ele deseja
	 * visualizar
	 * 
	 * Obs. Se achar melhor adcionar mais informa��es ao relatorio sinta-se livre para fazer as
	 * altera��es no codigo e no banco
	 */
	public void mostrar_relatorios() {
		System.out.println("1-Relatorio de transa��es do mes atual");
		System.out.println("2-Relatorio de transa��es de um mes especifico");
		System.out.println("3-Relatorio geral de transa��es");
		System.out.println("Selecione uma op��o de relatorio: ");
		int opcao = Integer.parseInt(teclado.nextLine());
		if (opcao == 1) {
			gerar_relatorio_mes_atual();
		}else if(opcao == 2) {
			gerar_relatorio_mensal();
		}else if(opcao == 3) {
			gerar_relatorio_geral();
		}
	}
	
	/**
	 * Metodo que gera o relatorio referente a todas as transa��es realizadas por clientes da 
	 * empresa
	 */
	public void gerar_relatorio_geral() {
		String BUSCAR_VALORES = "SELECT sum(t.valor), cm.nome FROM transacoes AS t, criptomoedas AS cm "
				+ "WHERE t.id_criptomoeda = cm.id GROUP BY id_criptomoeda";
		
		try {
			Conexao conect = new Conexao();
			Connection con = conect.conectar();
			PreparedStatement busca = con.prepareStatement(BUSCAR_VALORES);
			ResultSet resultado = busca.executeQuery();
			resultado.last();
			int qtd = resultado.getRow();
			resultado.beforeFirst();
			if(qtd>0) {
				System.out.println("Relatorio Geral de transa��es");
				System.out.println("--------------------");
				System.out.println("Foram movimentados: ");
				while(resultado.next()) {
					System.out.println(resultado.getFloat(1) + " em " + resultado.getString(2));
				}
				System.out.println("--------------------");
			}else {
				System.out.println("N�o h� transa��es");
				busca.close();
				conect.desconectar(con);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um erro na busca");
		}
		
	}
	/**
	 * Metodo que gera o relatorio de todaas as transa��es realizadas pelos clientes num mes
	 * especifico digitado pelo funcionario
	 */
	public void gerar_relatorio_mensal() {
		String BUSCAR_VALORES = "SELECT sum(t.valor), cm.nome FROM transacoes AS t, criptomoedas AS cm "
				+ "WHERE t.id_criptomoeda = cm.id AND MONTH(t.data) = ? GROUP BY id_criptomoeda";
		
		System.out.println("Digite o mes que deseja buscar: ");
		int mes = Integer.parseInt(teclado.nextLine());
		
		try {
			Conexao conect = new Conexao();
			Connection con = conect.conectar();
			PreparedStatement busca = con.prepareStatement(BUSCAR_VALORES);
			busca.setInt(1, mes);
			ResultSet resultado = busca.executeQuery();
			resultado.last();
			int qtd = resultado.getRow();
			resultado.beforeFirst();
			if(qtd>0) {
				System.out.println("Relatorio de transa��es do m�s " + mes);
				System.out.println("--------------------");
				System.out.println("Foram movimentados: ");
				while(resultado.next()) {
					System.out.println(resultado.getFloat(1) + " em " + resultado.getString(2));
				}
				System.out.println("--------------------");
			}else {
				System.out.println("N�o h� transa��es");
				busca.close();
				conect.desconectar(con);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um erro na busca");
		}
		
	}
	
	/**
	 * Metodo que gera o relatorio de todas as transa��es realizadas pelos clientes no mes atual
	 * em que est� sendo feito o acesso
	 */
	public void gerar_relatorio_mes_atual(){
		String BUSCAR_VALORES = "SELECT sum(t.valor), cm.nome FROM transacoes AS t, criptomoedas AS cm "
				+ "WHERE t.id_criptomoeda = cm.id AND MONTH(t.data) = MONTH(CURDATE()) GROUP BY id_criptomoeda";
		
		try {
			Conexao conect = new Conexao();
			Connection con = conect.conectar();
			PreparedStatement busca = con.prepareStatement(BUSCAR_VALORES);
			ResultSet resultado = busca.executeQuery();
			resultado.last();
			int qtd = resultado.getRow();
			resultado.beforeFirst();
			if(qtd>0) {
				System.out.println("Relatorio do mes atual");
				System.out.println("--------------------");
				System.out.println("Foram movimentados: ");
				while(resultado.next()) {
					System.out.println(resultado.getFloat(1) + " em " + resultado.getString(2));
				}
				System.out.println("--------------------");
			}else {
				System.out.println("N�o h� transa��es");
				busca.close();
				conect.desconectar(con);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um erro na busca");
		}
	}
	/**
	 Metodo para listar clientes inadimplentes, � controlado pelo campo status
	 Status: 1-Sem problemas 2-Inadimplente
	 */
	public void listar_inadimplentes() {
		String BUSCAR_INADIMPLENTES = "SELECT * FROM clientes WHERE status = 2";
		
		try {
			Conexao conect = new Conexao();
			Connection con = conect.conectar();
			PreparedStatement busca = con.prepareStatement(BUSCAR_INADIMPLENTES);
			ResultSet resultado = busca.executeQuery();
			resultado.last();
			int qtd = resultado.getRow();
			resultado.beforeFirst();
			if(qtd>0) {
				System.out.println("Listando clientes...");
				System.out.println("--------------------");
				while(resultado.next()) {
					System.out.println("ID: " + resultado.getInt(1));
					System.out.println("Nome: " + resultado.getString(2));
					System.out.println("CPF/CNPJ: " + resultado.getString(3));
					System.out.println("Telefone: " + resultado.getString(6));
					System.out.println("--------------------");
				}
			busca.close();
			conect.desconectar(con);
			}else {
				System.out.println("N�o h� clientes inadimplentes");
				busca.close();
				conect.desconectar(con);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um erro na busca");
		}
	}

}
