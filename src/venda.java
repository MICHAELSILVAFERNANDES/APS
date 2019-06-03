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
import javax.swing.JOptionPane;
@ManagedBean(name = "vendaBean")
@RequestScoped
public class venda {
	private String data;
	private int clienteId;
	private int veiculoId;
	private int id;
	ArrayList usersList;
	private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	Connection connection;
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getClienteId() {
		return clienteId;
	}
	public void setClienteId(int clienteId) {
		this.clienteId = clienteId;
	}
	public int getVeiculoId() {
		return veiculoId;
	}
	public void setVeiculoId(int veiculoId) {
		this.veiculoId = veiculoId;
	}
	public Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/revenda", "root", "");
		} catch (Exception e) {
			System.out.println(e);
		}
		return connection;
	}

	public ArrayList userList() {
		try {
			usersList = new ArrayList();
			connection = getConnection();
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select * from venda ");
			while (rs.next()) {
				venda Venda = new venda();
				Venda.setData(rs.getString("data"));
				Venda.setClienteId(rs.getInt("clienteId"));
				Venda.setVeiculoId(rs.getInt("veiculoId"));
				Venda.setId(rs.getInt("id"));
				usersList.add(Venda);
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
					"insert into venda(clienteId,veiculoId,data) values (?,?,?)");
			stmt.setInt(1, clienteId);
			stmt.setInt(2, veiculoId);
			stmt.setString(3, data);
			resut = stmt.executeUpdate();
			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		

		return "index.xhtml?faces-redrect=true";

	}

//usado para atualizar os registros no banco
	public String editar(int id) {
		venda Venda = null;
		System.out.println(id);
		try {
			connection = getConnection();
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select * from venda where id =" + id);
			rs.next();
			Venda = new venda();
			Venda.setData(rs.getString("data"));
			Venda.setVeiculoId(rs.getInt("veiculoId"));
			Venda.setClienteId(rs.getInt("clienteId"));
			Venda.setId(rs.getInt("id"));
			sessionMap.put("editUser", Venda);
			connection.close();
		} catch (Exception e) {
			System.out.println("e");
		}
		return "editaVenda.xhtm?faces-redirect = true";
	}

	public String atualizar(venda u) {
		try {
			connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement(
					"update venda set clienteId=?, veiculoId=?, data=? where id = "+u.getId());
			stmt.setInt(1, u.getClienteId());
			stmt.setInt(2, u.getVeiculoId());
			stmt.setString(3, u.getData());
			stmt.executeUpdate();
			connection.close();
		} catch (Exception e) {
			System.out.println();
		}
		return "listaVendas.xhtml?faces-redrect = true";
	}

	public void delete(int id) {
		try {
			connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("delete from venda where id =" + id);
			stmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}

	

