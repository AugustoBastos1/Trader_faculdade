package teste_desktop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class Administrador extends Funcionario {

	public void cadastrar_func() {
		String CHECA_USUARIO = "SELECT * FROM usuarios WHERE username = ? AND senha = ? AND codigo > 1";
		String INSERE_USUARIO = "INSERT INTO usuarios(username, senha, codigo) VALUES (?, ?, ?)";
		String INSERE_FUNCIONARIO = "INSERT INTO funcionarios"
				+ "(nome, documento, email, funcao, data_admissao, telefone, endereco, cidade, estado, id_usuario)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		System.out.println("Cadastro de Funcionarios");
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
				String PEGA_ID = "SELECT * FROM usuarios WHERE username = ? AND senha = ? AND codigo = ?";
				System.out.println("Digite o codigo do funcionario(2 p/ funcionario comum 3 p/adm)");
				int codigo = Integer.parseInt(teclado.nextLine());
				PreparedStatement salvar = con.prepareStatement(INSERE_USUARIO);
				salvar.setString(1, username);
				salvar.setString(2, senha);
				salvar.setInt(3, codigo);
				System.out.println("Digite o nome do funcionario");
				String nome = teclado.nextLine();
				System.out.println("Digite o cpf/cnpj do funcionario");
				String documento = teclado.nextLine();
				System.out.println("Digite o email do funcionario");
				String email = teclado.nextLine();
				System.out.println("Digite a função do funcionario");
				String funcao = teclado.nextLine();
				System.out.println("Digite a data de admissão do funcionario (yyyy-mm-dd)");
				String data = teclado.nextLine();
				System.out.println("Digite o telefone do funcionario");
				String telefone = teclado.nextLine();
				System.out.println("Digite o endereço do funcionario");
				String endereco = teclado.nextLine();
				System.out.println("Digite a cidade em que o funcionario trabalha: ");
				String cidade = teclado.nextLine();
				System.out.println("Digite a UF em que o funcionario trabalha: ");
				String estado = teclado.nextLine();
				salvar.executeUpdate();
				PreparedStatement pega_id = con.prepareStatement(PEGA_ID);
				pega_id.setString(1, username);
				pega_id.setString(2, senha);
				pega_id.setInt(3, codigo);
				ResultSet res = pega_id.executeQuery();
				res.last();
				int id_usuario = res.getInt(1);
				PreparedStatement funcionario = con.prepareStatement(INSERE_FUNCIONARIO);
				funcionario.setString(1, nome);
				funcionario.setString(2, documento);
				funcionario.setString(3, email);
				funcionario.setString(4, funcao);
				funcionario.setString(5, data);
				funcionario.setString(6, telefone);
				funcionario.setString(7, endereco);
				funcionario.setString(8, cidade);
				funcionario.setString(9, estado);
				funcionario.setInt(10, id_usuario);
				funcionario.executeUpdate();
				System.out.println("Funcionario inserido com sucesso");
				salvar.close();
				funcionario.close();
				pega_id.close();
				conect.desconectar(con);
			}	
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um erro ao cadastrar o funcionario");
		}
	}
	
	public void verificar_func() {
		String BUSCA_FUNCIONARIO = "SELECT * FROM funcionarios WHERE id = ?";
		System.out.println("Digite o ID do funcionario: ");
		int id = Integer.parseInt(teclado.nextLine());
		
		try {
			Conexao conect = new Conexao();
			Connection con = conect.conectar();
			PreparedStatement busca = con.prepareStatement(BUSCA_FUNCIONARIO);
			busca.setInt(1, id);
			ResultSet resultado = busca.executeQuery();
			resultado.last();
			int qtd = resultado.getRow();
			if(qtd > 0) {
				System.out.println("ID: " + resultado.getInt(1));
				System.out.println("Nome: " + resultado.getString(2));
				System.out.println("CPF/CNPJ: " + resultado.getString(3));
				System.out.println("Email: " + resultado.getString(4));
				System.out.println("Funcão: " + resultado.getString(5));
				System.out.println("Data de admissão: " + resultado.getDate(6));
				System.out.println("Telefone: " + resultado.getString(7));
				System.out.println("Endereço: " + resultado.getString(8));
				System.out.println("Cidade: " + resultado.getString(9));
				System.out.println("Estado(UF): " + resultado.getString(10));
				System.out.println("ID de usuario: " + resultado.getInt(11));
			}else {
				System.out.println("Não existe funcionario com o id informado");
			}
			resultado.close();
			conect.desconectar(con);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Ocorreu um erro na busca");
		}
	}
	
	

}
