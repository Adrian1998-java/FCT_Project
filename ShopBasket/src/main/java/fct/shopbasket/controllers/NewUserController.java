package fct.shopbasket.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.google.gson.Gson;

import fct.shopbasket.app.App;
import fct.shopbasket.utils.Lista;
import fct.shopbasket.utils.Producto;
import fct.shopbasket.utils.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * Controlador de la clase NewUser. Crea un nuevo usuario en la BD
 * @author scrag
 *
 */
public class NewUserController implements Initializable {

	// MODEL
	private StringProperty verify = new SimpleStringProperty();

	static Connection conn = null;

	@FXML
	private Button crearUserButton;

	@FXML
	private Label muestraLabel;

	@FXML
	private TextField passwrdTextfield;

	@FXML
	private TextField userTextfield;

	@FXML
	private TextField verifyPasswrdTextfield;

	@FXML
	private BorderPane view;

	public NewUserController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewUserView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		verifyPasswrdTextfield.textProperty().bindBidirectional(verify);
		verify.addListener((v, ov, nv) -> onVerifyChanged(v, ov, nv));

	}

	public BorderPane getView() {
		return view;
	}
/**
 * Verifica que ciertas condiciones se han cumplido
 * @param v
 * @param ov
 * @param nv
 */
	private void onVerifyChanged(ObservableValue<? extends String> v, String ov, String nv) {

		if (!(nv.equals(verifyPasswrdTextfield.getText())) || nv.isEmpty()) {

			crearUserButton.setDisable(true);
		} else {
			crearUserButton.setDisable(false);
		}
	}
/**
 * Crea un nuevo usuario si no existe
 * @param event
 * @throws SQLException
 */
	@FXML
	void onCrearUser(ActionEvent event) throws SQLException {

		int ultimaEntrada = 0;
		PreparedStatement visualiza = conn.prepareStatement("SELECT Id FROM `usuarios` ORDER BY id ASC");
		
		ResultSet resultado = visualiza.executeQuery();
		
		while(resultado.next()) {
			ultimaEntrada = resultado.getInt(1);
		}
		if (passwrdTextfield.getText().equals(verifyPasswrdTextfield.getText())) {

			PreparedStatement inserta = conn.prepareStatement("INSERT INTO usuarios VALUES (?,?,?,?)");
			
			inserta.setInt(1, ultimaEntrada+1);
			inserta.setString(2, userTextfield.getText());
			inserta.setString(3, passwrdTextfield.getText());
			
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
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("ERROR");
			alert.setContentText("Procure ver que la verificación es correcta");
			
			alert.showAndWait();
		}

	}

	public void showApp() {
		Stage listStage = new Stage();

		listStage.setScene(new Scene(this.getView(), 600, 300));
		listStage.setTitle("CrearUsuario");
		listStage.initModality(Modality.WINDOW_MODAL);
		listStage.setResizable(false);
		listStage.initOwner(App.getStage());

		listStage.showAndWait();
	}

	public static void setConn(Connection conn) {
		NewUserController.conn = conn;
	}
}
