import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "Clientebean")
@RequestScoped

public class cadastroBean {
	private String nome;
	private String cidade;
	private String email;
	private String cpf;
	private int id;
	private String telefone;
	private String endereco;
	ArrayList usersList;
	private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	Connection connection;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

//conexão com o banco de dados
	public Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/revenda", "root", "");
		} catch (Exception e) {
			System.out.println(e);
		}
		return connection;
	}

//usado para pegas todos os registros
	public ArrayList userList() {
		try {
			usersList = new ArrayList();
			connection = getConnection();
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select*from cliente");
			while (rs.next()) {
				cadastroBean cliente = new cadastroBean();
				cliente.setCidade(rs.getString("cidade"));
				cliente.setCpf(rs.getString("cpf"));
				cliente.setEmail(rs.getString("email"));
				cliente.setEndereco(rs.getString("endereco"));
				cliente.setId(rs.getInt("id"));
				cliente.setNome(rs.getString("nome"));
				cliente.setTelefone(rs.getString("telefone"));
				usersList.add(cliente);
			}
			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		return usersList;

	}

//salva os dados no banco
	public String salvar() {
		int resut = 0;
		try {
			connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement(
					"insert into cliente(nome,email,endereco,cidade,telefone,cpf) values (?,?,?,?,?,?)");
			stmt.setString(1, nome);
			stmt.setString(2, email);
			stmt.setString(3, endereco);
			stmt.setString(4, cidade);
			stmt.setString(5, telefone);
			stmt.setString(6, cpf);
			resut = stmt.executeUpdate();
			connection.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		

		return "telaPrincipal.xhtml?faces-redrect=true";

	}

//usado para atualizar os registros no banco
	public String editar(int id) {
		cadastroBean cliente = null;
		System.out.println(id);
		try {
			connection = getConnection();
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select * from cliente where id =" + id);
			rs.next();
			cliente = new cadastroBean();
			cliente.setCidade(rs.getString("cidade"));
			cliente.setCpf(rs.getString("cpf"));
			cliente.setEmail(rs.getString("email"));
			cliente.setEndereco(rs.getString("endereco"));
			cliente.setId(rs.getInt("id"));
			cliente.setNome(rs.getString("nome"));
			cliente.setTelefone(rs.getString("telefone"));
			sessionMap.put("editUser", cliente);
			connection.close();
		} catch (Exception e) {
			System.out.println("e");
		}
		return "editaCliente.xhtm? faces-redirect=true";
	}

	public String atualizar(cadastroBean u) {
		try {
			connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement(
					"update cliente set nome=?, cidade=?, cpf=?, email =?, endereco = ?, telefone= ? where id = "+u.id);
			stmt.setString(1, u.getNome());
			stmt.setString(2, u.getCidade());
			stmt.setString(3, u.getCpf());
			stmt.setString(4, u.getEmail());
			stmt.setString(5, u.getEndereco());
			stmt.setString(6, u.getTelefone());
			stmt.executeUpdate();
			connection.close();
		} catch (Exception e) {
			System.out.println();
		}
		return "telaPrincipal.xhtml?faces-redrect = true";
	}
  
	public void delete(int id) {
		try {
			connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("delete from cliente where id =" + id);
			stmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
