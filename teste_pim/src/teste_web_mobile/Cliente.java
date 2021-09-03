package teste_web_mobile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Cliente extends Usuario {
	private String username;
	private String senha;
	
	
	public Cliente() {
		
	}
	
	public Cliente(String username, String senha) {
		this.username = username;
		this.senha = senha;
	}
	
	/**
	 * O valor pré definido pro codigo é para representar q o usuario é um cliente,
	 * o valor pré definido pro status representa que o cliente não tem nenhum problema de inandimplencia
	 * 
	 * Obs. a data de nascimento está sendo extraida como string e como o Java é ruim para trabalhar com data
	 * eu não consegui converter, então é muito suscetivel a erros, se der, tente arrumar isso na interface
	 * grafica,
	 */
	public void cadastrar() {
		String CHECA_USUARIO = "SELECT * FROM usuarios WHERE username = ? AND senha = ? AND codigo = 1";
		String INSERE_USUARIO = "INSERT INTO usuarios(username, senha, codigo) VALUES (?, ?, 1)";
		String INSERE_CLIENTE = "INSERT INTO clientes"
				+ "(nome, documento, email, data_nascimento, telefone, endereco, cidade, estado, status, id_usuario)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String BUSCA_DATA = "SELECT DATE_SUB(CURDATE(), INTERVAL 18 YEAR) > ?";
		
		System.out.println("Cadastro de Clientes");
		System.out.println("--------------------");
		System.out.println("Digite o nome de usuario");
		String username = teclado.nextLine();
		System.out.println("Digite a senha de usuario");
		String senha = teclado.nextLine();
		
		try {
			Conexao conect = new Conexao();
			Connection con = conect.conectar();
			PreparedStatement usuario = con.prepareStatement(CHECA_USUARIO);
			usuario.setString(1, username);
			usuario.setString(2, senha);
			ResultSet resultado = usuario.executeQuery();
			resultado.last();
			int qtd = resultado.getRow();
			if(qtd>0) {
				System.out.println("Este usuario já existe");
				conect.desconectar(con);
			}else {
				String PEGA_ID = "SELECT * FROM usuarios WHERE username = ? AND senha = ? AND codigo = 1";
				PreparedStatement salvar = con.prepareStatement(INSERE_USUARIO);
				salvar.setString(1, username);
				salvar.setString(2, senha);
				System.out.println("Digite seu nome: ");
				String nome = teclado.nextLine();
				System.out.println("Digite seu cpf/cnpj");
				String documento = teclado.nextLine();
				System.out.println("Digite seu email");
				String email = teclado.nextLine();
				System.out.println("Digite sua data de nascimento (yyyy-mm-dd)");
				String data = teclado.nextLine();
				boolean valida = false;
				while(!valida) {
					PreparedStatement validar = con.prepareStatement(BUSCA_DATA);
					validar.setString(1, data);
					ResultSet res2 = validar.executeQuery();
					res2.last();
					if(res2.getInt(1) == 1) {
						valida = true;
					}else {
						System.out.println("O cliente deve ser maior de idade!");
						System.out.println("Digite sua data de nascimento (yyyy-mm-dd)");
						data = teclado.nextLine();
					}
				}	
				System.out.println("Digite seu telefone");
				String telefone = teclado.nextLine();
				System.out.println("Digite sua endereço");
				String endereco = teclado.nextLine();
				System.out.println("Digite a cidade onde mora: ");
				String cidade = teclado.nextLine();
				System.out.println("Digite a UF onde mora: ");
				String estado = teclado.nextLine();
				salvar.executeUpdate();
				PreparedStatement pega_id = con.prepareStatement(PEGA_ID);
				pega_id.setString(1, username);
				pega_id.setString(2, senha);
				ResultSet res = pega_id.executeQuery();
				res.last();
				int id_usuario = res.getInt(1);
				PreparedStatement cliente = con.prepareStatement(INSERE_CLIENTE);
				cliente.setString(1, nome);
				cliente.setString(2, documento);
				cliente.setString(3, email);
				cliente.setString(4, data);
				cliente.setString(5, telefone);
				cliente.setString(6, endereco);
				cliente.setString(7, cidade);
				cliente.setString(8, estado);
				cliente.setInt(9, 1);
				cliente.setInt(10, id_usuario);
				cliente.executeUpdate();
				System.out.println("Cliente cadastrado com sucesso");
				salvar.close();
				cliente.close();
				pega_id.close();
				conect.desconectar(con);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um erro no processo de cadastro");
		}
		
	}
	
	/**
	 * Este metodo serve para o cliente poder cadastrar novas carteiras digitais
	 * 	
	 * OBS.Como carteiras digitais são softwares, n achei necessario guardar numero, ou outras 
	 * informações da carteira já q n teriamos acesso pq é um outro software, n foi dito em nenhum
	 * momento que a trader tinha seu software proprio pra carteira digital então tratei como se não
	 * tivesse
	 */
	public void cadastrar_carteira_digital() {
		String INSERE_CARTEIRA = "INSERT INTO carteiras_digitais(nome, id_cliente) VALUES (?, ?)";
		String PEGA_ID = "SELECT * FROM usuarios WHERE username = ? AND senha = ? AND codigo = 1";
		String PEGA_ID_CLIENTE = "SELECT * FROM clientes WHERE id_usuario = ?";
		
		System.out.println("Digite o nome da carteira digital");
		String carteira = teclado.nextLine();
		
		try {
			Conexao conect = new Conexao();
			Connection con = conect.conectar();
			PreparedStatement pega_id = con.prepareStatement(PEGA_ID);
			pega_id.setString(1, this.username);
			pega_id.setString(2, this.senha);
			ResultSet res = pega_id.executeQuery();
			res.last();
			int id_usuario = res.getInt(1);
			PreparedStatement pega_id_cliente = con.prepareStatement(PEGA_ID_CLIENTE);
			pega_id_cliente.setInt(1, id_usuario);
			ResultSet res2 = pega_id_cliente.executeQuery();
			res2.last();
			int id_cliente = res2.getInt(1);
			PreparedStatement salvar = con.prepareStatement(INSERE_CARTEIRA);
			salvar.setString(1, carteira);
			salvar.setInt(2, id_cliente);
			salvar.executeUpdate();
			System.out.println("Carteira salva com sucesso");
			pega_id.close();
			salvar.close();
			conect.desconectar(con);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um erro no processo de cadastro da carteira");
		}
		
	}
	
	/**
	 * Este metodo simula o fechamento de um smart contract, mtos dos trechos são simbolicos pq 
	 * não tenho mto conhecimento de como smart contracts funcionam
	 * 
	 * Obs. Coloquei o campo termos como se fosse selecionar um template em um arquivo(exemplo termos1.pdf)
	 * Espero que não tenha ficado confuso, se achar melhor trocar, sinta-se livre pra alterar tanto no codigo
	 * quanto no banco
	 */
	public void firmar_contrato() {
		
		String INSERIR_CONTRATO = "INSERT INTO contratos_digitais(nome_parte, termos, id_cliente)"
				+ "VALUES(?, ?, ?)";
		String PEGA_ID = "SELECT * FROM usuarios WHERE username = ? AND senha = ? AND codigo = 1";
		String PEGA_ID_CLIENTE = "SELECT * FROM clientes WHERE id_usuario = ?";
		
		System.out.println("Digite o nome da outra parte envolvida no contrato: ");
		String nome_parte = teclado.nextLine();
		System.out.println("Selecione os termos do contrato: ");//Trecho simbolico de codigo
		String termos = teclado.nextLine();
		
		try {
			Conexao conect = new Conexao();
			Connection con = conect.conectar();
			PreparedStatement pega_id = con.prepareStatement(PEGA_ID);
			pega_id.setString(1, this.username);
			pega_id.setString(2, this.senha);
			ResultSet res = pega_id.executeQuery();
			res.last();
			int id_usuario = res.getInt(1);
			PreparedStatement pega_id_cliente = con.prepareStatement(PEGA_ID_CLIENTE);
			pega_id_cliente.setInt(1, id_usuario);
			ResultSet res2 = pega_id_cliente.executeQuery();
			res2.last();
			int id_cliente = res2.getInt(1);
			PreparedStatement salvar = con.prepareStatement(INSERIR_CONTRATO);
			salvar.setString(1, nome_parte);
			salvar.setString(2, termos);
			salvar.setInt(3, id_cliente);
			salvar.executeUpdate();
			pega_id.close();
			pega_id_cliente.close();
			salvar.close();
			conect.desconectar(con);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um erro no processo de fechamento do contrato");
		}
	}
	
	/**
	 * OBS. Achei melhor deixar os graficos como WIP(work in progress) pq são essencialmente os mesmos
	 * codigos dos relatorios mas poderia confundir na hora de implementar a interface grafica, creio que
	 * o select pra atribuir o valor nos graficos não deve ser mto diferente então se n mudar é só dar
	 * um ctrl c ctrl v no codigo abaixo, dos relatorios
	 */
	public void mostrar_graficos() {
		System.out.println("1-Grafico de transações do mes atual");
		System.out.println("2-Grafico de transações de um mes especifico");
		System.out.println("3-Grafico geral de transações");
		System.out.println("Selecione uma opção de grafico: ");
		int opcao = Integer.parseInt(teclado.nextLine());
		if (opcao == 1) {
			System.out.println("WIP");
		}else if(opcao == 2) {
			System.out.println("WIP");
		}else if(opcao == 3) {
			System.out.println("WIP");
		}
	}
	
	public void gerar_grafico_geral(){
		//TODO metodo para gerar os graficos em geral
	}
	
	public void gerar_grafico_mensal() {
		//TODO metodo para gerar os graficos baseados no mes
	}
	public void gerar_grafico_mes_atual() {
		// TODO metodo para gerar os graficos baseados no mes atual
	}
	
	/**
	 * Esse metodo funciona como uma especie de sub-menu, para o cliente selecionar qual tipo
	 * de relatorio ele quer visualizar
	 * 
	 * OBS. Caso ache melhor adcionar mais informações aos relatorios, sinta-se livre para fazer
	 * alterações
	 * 
	 * 
	 */
	public void mostrar_relatorios() {
		System.out.println("1-Relatorio de transações do mes atual");
		System.out.println("2-Relatorio de transações de um mes especifico");
		System.out.println("3-Relatorio geral de transações");
		System.out.println("Selecione uma opção de relatorio: ");
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
	 * Esse metodo gera os relatorios de todas as movimentações do cliente contida na tabela de 
	 * transações
	 * 
	 * OBS. esse codigo pode ser usado pra representar o grafico tambem, não acho q vai ter problema
	 */
	public void gerar_relatorio_geral() {
		String BUSCAR_VALORES = "SELECT sum(t.valor), cm.nome "
				+ "FROM transacoes AS t, carteiras_digitais AS cd, clientes AS c, criptomoedas AS cm, usuarios AS u "
				+ "WHERE t.id_carteira_digital = cd.id AND t.id_criptomoeda = cm.id "
				+ "AND cd.id_cliente = c.id AND c.id_usuario = u.id AND c.id = ? GROUP BY id_criptomoeda";
		String PEGA_ID = "SELECT * FROM usuarios WHERE username = ? AND senha = ? AND codigo = 1";
		String PEGA_ID_CLIENTE = "SELECT * FROM clientes WHERE id_usuario = ?";
		
		try {
			Conexao conect = new Conexao();
			Connection con = conect.conectar();
			PreparedStatement pega_id = con.prepareStatement(PEGA_ID);
			pega_id.setString(1, this.username);
			pega_id.setString(2, this.senha);
			ResultSet res = pega_id.executeQuery();
			res.last();
			int id_usuario = res.getInt(1);
			PreparedStatement pega_id_cliente = con.prepareStatement(PEGA_ID_CLIENTE);
			pega_id_cliente.setInt(1, id_usuario);
			ResultSet res2 = pega_id_cliente.executeQuery();
			res2.last();
			int id_cliente = res2.getInt(1);
			PreparedStatement busca = con.prepareStatement(BUSCAR_VALORES);
			busca.setInt(1, id_cliente);
			ResultSet resultado = busca.executeQuery();
			resultado.last();
			int qtd = resultado.getRow();
			resultado.beforeFirst();
			if(qtd>0) {
				System.out.println("Relatorio Geral de transações");
				System.out.println("--------------------");
				System.out.println("Foram movimentados: ");
				while(resultado.next()) {
					System.out.println(resultado.getFloat(1) + " em " + resultado.getString(2));
				}
				System.out.println("--------------------");
			}else {
				System.out.println("Não há transações");
			}
			busca.close();
			conect.desconectar(con);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um erro na busca");
		}
	}
	
	/**
	 * Este metodo gera os relatorios referente a um mes especifico informado pelo usuario
	 * 
	 * talvez pode gerar erros se for digitado em texto então pode ser explorado outra opção
	 * na interface, mas fica a seu criterio
	 */
	public void gerar_relatorio_mensal() {
		String BUSCAR_VALORES = "SELECT sum(t.valor), cm.nome "
				+ "FROM transacoes AS t, carteiras_digitais AS cd, clientes AS c, criptomoedas AS cm, usuarios AS u "
				+ "WHERE t.id_carteira_digital = cd.id AND t.id_criptomoeda = cm.id "
				+ "AND cd.id_cliente = c.id AND c.id_usuario = u.id AND YEAR(t.data) = YEAR(CURDATE()) AND MONTH(t.data) = ? AND c.id = ? GROUP BY id_criptomoeda";
		String PEGA_ID = "SELECT * FROM usuarios WHERE username = ? AND senha = ? AND codigo = 1";
		String PEGA_ID_CLIENTE = "SELECT * FROM clientes WHERE id_usuario = ?";
		
		System.out.println("Digite o mes que deseja buscar: ");
		int mes = Integer.parseInt(teclado.nextLine());
		
		try {
			Conexao conect = new Conexao();
			Connection con = conect.conectar();
			PreparedStatement pega_id = con.prepareStatement(PEGA_ID);
			pega_id.setString(1, this.username);
			pega_id.setString(2, this.senha);
			ResultSet res = pega_id.executeQuery();
			res.last();
			int id_usuario = res.getInt(1);
			PreparedStatement pega_id_cliente = con.prepareStatement(PEGA_ID_CLIENTE);
			pega_id_cliente.setInt(1, id_usuario);
			ResultSet res2 = pega_id_cliente.executeQuery();
			res2.last();
			int id_cliente = res2.getInt(1);
			PreparedStatement busca = con.prepareStatement(BUSCAR_VALORES);
			busca.setInt(1, mes);
			busca.setInt(2, id_cliente);
			ResultSet resultado = busca.executeQuery();
			resultado.last();
			int qtd = resultado.getRow();
			resultado.beforeFirst();
			if(qtd>0) {
				System.out.println("Relatorio de transações do mês " + mes);
				System.out.println("--------------------");
				System.out.println("Foram movimentados: ");
				while(resultado.next()) {
					System.out.println(resultado.getFloat(1) + " em " + resultado.getString(2));
				}
				System.out.println("--------------------");
			}else {
				System.out.println("Não houve transações neste mes");
				
			}
			busca.close();
			conect.desconectar(con);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um erro na busca");
		}
		
	}
	
	
	/**
	 * Este metodo gera o relatorio referente ao mes atual, provavelmente usa a data do sistema
	 */
	public void gerar_relatorio_mes_atual() {
		String BUSCAR_VALORES = "SELECT sum(t.valor), cm.nome "
				+ "FROM transacoes AS t, carteiras_digitais AS cd, clientes AS c, criptomoedas AS cm, usuarios AS u "
				+ "WHERE t.id_carteira_digital = cd.id AND t.id_criptomoeda = cm.id "
				+ "AND cd.id_cliente = c.id AND c.id_usuario = u.id AND YEAR(t.data) = YEAR(CURDATE()) AND MONTH(t.data) = MONTH(CURDATE()) AND c.id = ? GROUP BY id_criptomoeda";
		String PEGA_ID = "SELECT * FROM usuarios WHERE username = ? AND senha = ? AND codigo = 1";
		String PEGA_ID_CLIENTE = "SELECT * FROM clientes WHERE id_usuario = ?";
		
		try {
			Conexao conect = new Conexao();
			Connection con = conect.conectar();
			PreparedStatement pega_id = con.prepareStatement(PEGA_ID);
			pega_id.setString(1, this.username);
			pega_id.setString(2, this.senha);
			ResultSet res = pega_id.executeQuery();
			res.last();
			int id_usuario = res.getInt(1);
			PreparedStatement pega_id_cliente = con.prepareStatement(PEGA_ID_CLIENTE);
			pega_id_cliente.setInt(1, id_usuario);
			ResultSet res2 = pega_id_cliente.executeQuery();
			res2.last();
			int id_cliente = res2.getInt(1);
			PreparedStatement busca = con.prepareStatement(BUSCAR_VALORES);
			busca.setInt(1, id_cliente);
			ResultSet resultado = busca.executeQuery();
			resultado.last();
			int qtd = resultado.getRow();
			resultado.beforeFirst();
			if(qtd>0) {
				System.out.println("Relatorio Geral de transações");
				System.out.println("--------------------");
				System.out.println("Foram movimentados: ");
				while(resultado.next()) {
					System.out.println(resultado.getFloat(1) + " em " + resultado.getString(2));
				}
				System.out.println("--------------------");
			}else {
				System.out.println("Não houve transações neste mes");
			}
			busca.close();
			conect.desconectar(con);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um erro na busca");
		}
	}

	/**
	 * Este metodo mostra todas as informações de todas as transações que o cliente realizou
	 */
	public void mostrar_historico_transacoes() {
		String BUSCAR_TRANSACOES = "SELECT t.id, t.data, t.valor, cd.nome, c.nome, cm.nome "
				+ "FROM transacoes AS t, carteiras_digitais AS cd, clientes AS c, criptomoedas AS cm, usuarios AS u "
				+ "WHERE t.id_carteira_digital = cd.id AND t.id_criptomoeda = cm.id "
				+ "AND cd.id_cliente = c.id AND c.id_usuario = u.id AND c.id = ?";
		String PEGA_ID = "SELECT * FROM usuarios WHERE username = ? AND senha = ? AND codigo = 1";
		String PEGA_ID_CLIENTE = "SELECT * FROM clientes WHERE id_usuario = ?";
		
		try {
			Conexao conect = new Conexao();
			Connection con = conect.conectar();
			PreparedStatement pega_id = con.prepareStatement(PEGA_ID);
			pega_id.setString(1, this.username);
			pega_id.setString(2, this.senha);
			ResultSet res = pega_id.executeQuery();
			res.last();
			int id_usuario = res.getInt(1);
			PreparedStatement pega_id_cliente = con.prepareStatement(PEGA_ID_CLIENTE);
			pega_id_cliente.setInt(1, id_usuario);
			ResultSet res2 = pega_id_cliente.executeQuery();
			res2.last();
			int id_cliente = res2.getInt(1);
			PreparedStatement busca = con.prepareStatement(BUSCAR_TRANSACOES);
			busca.setInt(1, id_cliente);
			ResultSet resultado = busca.executeQuery();
			resultado.last();
			int qtd = resultado.getRow();
			resultado.beforeFirst();
			if(qtd > 0) {
				System.out.println("Listando transações...");
				System.out.println("--------------------");
				while(resultado.next()) {
					System.out.println("ID: " + resultado.getInt(1));
					System.out.println("Data: " + resultado.getDate(2));
					System.out.println("Valor: " + resultado.getFloat(3));
					System.out.println("Carteira: " + resultado.getString(4));
					System.out.println("Cliente: " + resultado.getString(5));
					System.out.println("Criptomoeda: " + resultado.getString(6));
					System.out.println("--------------------");
				}
			}else {
				System.out.println("Voce não realizou nenhuma transação");
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um erro na busca");
		}
	}

}
