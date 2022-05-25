package fct.shopbasket.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.mysql.cj.protocol.Resultset;

import fct.shopbasket.app.App;
import fct.shopbasket.conn.MsqlConnection;
import fct.shopbasket.utils.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

/**
 * Clase controladora del la ventana del login
 * 
 * @author scrag
 *
 */
public class LoginController implements Initializable {

	// MODEL
	MsqlConnection conexion = new MsqlConnection();
	static Connection conn;

	ListsOfListController listController = new ListsOfListController();
	NewUserController newUserController = new NewUserController();
	User user;

	@FXML
	private Button cancelButton;

	@FXML
	private Button createAccountButton;

	@FXML
	private Button loginButton;

	@FXML
	private PasswordField passwdField;

	@FXML
	private TextField userField;

	@FXML
	private VBox view;

	/**
	 * Constructor
	 * 
	 * @throws IOException
	 */
	public LoginController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
		loader.setController(this);
		loader.load();
	}

	/**
	 * Inicializador del controlador
	 */
	public void initialize(URL location, ResourceBundle resources) {

		try {
			conn = conexion.MySQLConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		userField.setText("Adrian");
		passwdField.setText("Adriancito");

	}

	public VBox getView() {
		return view;
	}

	/**
	 * Evento que ocurre al pulsar el botón cancelar
	 * 
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void onCancel(ActionEvent event) throws SQLException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("ShopBasket");
		alert.setHeaderText("¿Seguro?");
		alert.setContentText("¿Está seguro que quiere salir?");

		ButtonType buttonTypeSi = new ButtonType("Si");
		ButtonType buttonTypeNo = new ButtonType("No", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeSi, buttonTypeNo);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeSi) {
			conn.close();
			System.exit(0);
		} else {
		}
	}

	/**
	 * Evento que ocurre al pulsar el botón Aqui
	 * 
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void onHere(ActionEvent event) throws SQLException {
		if (conn.isValid(0)) {
			newUserController.setConn(conn);
			newUserController.showApp();
		} else {

		}
	}

	/**
	 * Loguea en la aplicación, muestra una ventana de advertencia si los datos no
	 * son correctos
	 * 
	 * @param event
	 * @throws SQLException
	 */
	@FXML
	void onLogin(ActionEvent event) throws SQLException {

		PreparedStatement psMySQL = conn
				.prepareStatement("SELECT COUNT(*) FROM usuarios WHERE usuario=? AND contraseña=?;");

		psMySQL.setString(1, userField.getText());
		psMySQL.setString(2, passwdField.getText());

		ResultSet result = psMySQL.executeQuery();

		if (result.next()) {
			if (result.getByte(1) == 1) {
				System.out.println(result.getByte(1));
				App.getStage().close();
				PassDataToList();
				listController.showApp();
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("ERROR");
				alert.setHeaderText("Error al introducir los datos");
				alert.setContentText("Alguno de los datos introducidos son incorrectos.");

				alert.showAndWait();
			}
		}
	}

	/**
	 * Clase que recoge los datos del usuario y contraseña introducidos
	 * 
	 * @throws SQLException
	 */
	public void PassDataToList() throws SQLException {
		PreparedStatement psMySQL = conn.prepareStatement("SELECT * FROM usuarios WHERE usuario=? AND contraseña=?;");

		psMySQL.setString(1, userField.getText());
		psMySQL.setString(2, passwdField.getText());

		ResultSet resultado = psMySQL.executeQuery();
		while (resultado.next()) {
			String usuario = resultado.getString(2);
			String passwrd = resultado.getString(3);
			String json = resultado.getString(4);

			System.out.println("usuario = " + usuario + "/ password = " + passwrd + "/ json = " + json);
			user = new Gson().fromJson(json, User.class);
			listController.getUserList().addAll(user.getListasObject());
			listController.setConn(conn);
			listController.setUsuarioName(usuario);
			listController.setUsuarioPassw(passwrd);
		}

	}

}
