package teste_desktop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Metodo de conex�o, o maior problema desse codigo, por enquanto est� sendo feito no MySQL pq no
 * SQL server n�o vai de jeito nenhum, se vcs quiserem tentar conectar e conseguirem 
 * felizmente s� vai precisar mudar essa classe, o resto do codigo vai ser o mesmo
 */
public class Conexao {
	
	public Connection conectar() {
		String CLASSE_DRIVER = "com.mysql.jdbc.Driver";
		String USUARIO = "foxtrade";
		String SENHA = "foxtrade";
		String URL_SERVIDOR = "jdbc:mysql://localhost:3306/foxdb?useSSL=false";
		
		try {
			Class.forName(CLASSE_DRIVER);
			return DriverManager.getConnection(URL_SERVIDOR, USUARIO, SENHA);
		}catch(Exception e) {
			if(e instanceof ClassNotFoundException) {
				e.printStackTrace();
				System.out.println("Verifique o driver de conex�o");
			}else {
				e.printStackTrace();
				System.out.println("Verifique se o servidor est� ativo");
			}
			System.exit(-42);
			return null;
		}
	}
	
	public void desconectar(Connection con) {
		if(con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println("N�o foi possivel fechar a conex�o");
				e.printStackTrace();
			}
		}
	}

}
