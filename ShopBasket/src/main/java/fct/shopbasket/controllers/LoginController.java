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
import fct.shopbasket.app.App;
import fct.shopbasket.conn.MsqlConnection;
import fct.shopbasket.utils.Lista;
import fct.shopbasket.utils.Producto;
import fct.shopbasket.utils.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

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
//		if (conn.isValid(0)) {
//			newUserController.setConn(conn);
//			newUserController.showApp();
//			App.getStage().initOwner(App.getStage());
//		} 
		
		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("New User");
		dialog.setHeaderText("Crea un nuevo usuario");

		// Set the icon (must be included in the project).
//		dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Crear", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

		// Enable/Disable login button depending on whether a username was entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		username.textProperty().addListener((observable, oldValue, newValue) -> {
		    loginButton.setDisable(newValue.trim().isEmpty() && !password.getText().trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> username.requestFocus());

		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == loginButtonType) {
		        return new Pair<>(username.getText(), password.getText());
		    }
		    return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(usernamePassword -> {
			try {
				int ultimaEntrada = 0;
				PreparedStatement visualiza = conn.prepareStatement("SELECT Id FROM `usuarios` ORDER BY id ASC");
				
				ResultSet resultado = visualiza.executeQuery();
				
				while(resultado.next()) {
					ultimaEntrada = resultado.getInt(1);
				}
				PreparedStatement inserta = conn.prepareStatement("INSERT INTO usuarios VALUES (?,?,?,?)");
				
				inserta.setInt(1, ultimaEntrada+1);
				inserta.setString(2, username.getText());
				inserta.setString(3,password.getText());
				
				Producto prod = new Producto();
				prod.setNombreProducto("Almendras");
				prod.setCantidad(10);
				
				Lista list = new Lista();
				list.setNombreLista("Mercadona");
				list.getProductos().add(prod);
				
				User user = new User();
				user.getListasObject().add(list);
				String json = new Gson().toJson(user);
				
				inserta.setString(4, json);
				
				if (inserta.executeUpdate() == 1) {
					
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Insercción con éxito");
					alert.setHeaderText("¡Insercción con éxito!");
					alert.setContentText("Se ha insertado el usuario correctamente \n No olvides tus datos");
					
					alert.showAndWait();
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("ERROR");
					alert.setContentText("Ha ocurrido un error inesperado. Intentelo más tarde");
					
					alert.showAndWait();
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		});
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

		if(conn != null) {
			
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
		else
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("ERROR");
			alert.setHeaderText("Conexión fallida");
			alert.setContentText("El servicio no está activo en estos momentos");
			
			alert.showAndWait();
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
