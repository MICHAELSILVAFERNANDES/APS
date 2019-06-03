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
@ManagedBean(name = "Veiculobean")
@RequestScoped
public class cadastroVeiculo {
	private String marca;
	private String modelo;
	private String cor;
	private String anoFabricacao;
	private String placa;
	private String chassi;
	private int id;
	
	ArrayList usersList;
	private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	Connection connection;
   public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getAnoFabricacao() {
		return anoFabricacao;
	}

	public void setAnoFabricacao(String anoFabricacao) {
		this.anoFabricacao = anoFabricacao;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getChassi() {
		return chassi;
	}

	public void setChassi(String chassi) {
		this.chassi = chassi;
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
			ResultSet rs = stmt.executeQuery("select * from veiculo");
			while (rs.next()) {
				cadastroVeiculo veiculo = new cadastroVeiculo();
				veiculo.setMarca(rs.getString("marca"));
				veiculo.setCor(rs.getString("cor"));
				veiculo.setModelo(rs.getString("modelo"));
				veiculo.setAnoFabricacao(rs.getString("anoFabricacao"));
				veiculo.setPlaca(rs.getString("placa"));
				veiculo.setChassi(rs.getString("chassi"));
				veiculo.setId(rs.getInt("id"));
				usersList.add(veiculo);
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
					"insert into veiculo(modelo,marca,cor,anoFabricacao,placa,chassi) values (?,?,?,?,?,?)");
			stmt.setString(1, modelo);
			stmt.setString(2, marca);
			stmt.setString(3, cor);
			stmt.setString(4, anoFabricacao);
			stmt.setString(5, placa);
			stmt.setString(6, chassi);
			resut = stmt.executeUpdate();
			connection.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		

		return "index.xhtml?faces-redrect=true";

	}

//usado para atualizar os registros no banco
	public String editar(int id) {
		cadastroVeiculo veiculo = null;
		System.out.println(id);
		try {
			connection = getConnection();
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("select * from veiculo where id =" + id);
			rs.next();
			veiculo = new cadastroVeiculo();
			veiculo.setModelo(rs.getString("modelo"));
			veiculo.setMarca(rs.getString("marca"));
			veiculo.setCor(rs.getString("cor"));
			veiculo.setAnoFabricacao(rs.getString("anoFabricacao"));
			veiculo.setPlaca(rs.getString("placa"));
			veiculo.setChassi(rs.getString("chassi"));
			veiculo.setId(rs.getInt("id"));
			sessionMap.put("editUser", veiculo);
			connection.close();
		} catch (Exception e) {
			System.out.println("e");
		}
		return "editaVeiculo.xhtm?faces-redirect = true";
	}

	public String atualizar(cadastroVeiculo u) {
		try {
			connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement(
					"update veiculo set modelo=?, marca=?, anoFabricacao=?, cor =? , placa =? , chassi =? where id = "+u.id);
			stmt.setString(1, u.getModelo());
			stmt.setString(2, u.getMarca());
			stmt.setString(3, u.getAnoFabricacao());
			stmt.setString(4, u.getCor());
			stmt.setString(5, u.getPlaca());
			stmt.setString(6, u.getChassi());
			stmt.executeUpdate();
			connection.close();
		} catch (Exception e) {
			System.out.println();
		}
		return "listaVeiculo.xhtml?faces-redrect = true";
	}

	public void delete(int id) {
		try {
			connection = getConnection();
			PreparedStatement stmt = connection.prepareStatement("delete from veiculo where id =" + id);
			stmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
