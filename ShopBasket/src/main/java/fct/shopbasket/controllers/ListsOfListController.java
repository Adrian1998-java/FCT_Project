package fct.shopbasket.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;

import com.google.gson.Gson;

import fct.shopbasket.app.App;
import fct.shopbasket.utils.Lista;
import fct.shopbasket.utils.Producto;
import fct.shopbasket.utils.User;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ListsOfListController implements Initializable {

	// MODEL
	private ObjectProperty<Lista> selectedUser = new SimpleObjectProperty<Lista>();
	private static ListProperty<Lista> userList = new SimpleListProperty<Lista>(
			FXCollections.observableArrayList(new ArrayList<Lista>()));
	
	private ShopItemController itemController = new ShopItemController();
	
	static Connection conn = null;
	
	private String usuarioName;
	private String usuarioPassw;
	
	@FXML
	private Button addButton;

	@FXML
	private ListView<Lista> listsListView;

	@FXML
	private VBox noItemButton;

	@FXML
	private Button removeButton;
	

	@FXML
    private Button guardarDatosButton;
	
    @FXML
    private Label usernameLabel;

	@FXML
	private BorderPane view;

	public ListsOfListController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ListOfLists.fxml"));
		loader.setController(this);
		loader.load();
	}

	/**
	 * 
	 */
	public void initialize(URL location, ResourceBundle resources) {

		System.out.println(userList.getValue());
		listsListView.itemsProperty().bind(userList);
		listsListView.getSelectionModel().selectFirst();
		
		selectedUserProperty().bind(listsListView.getSelectionModel().selectedItemProperty());
		
		selectedUser.addListener((v, ov, nv) -> onItemChanged(v, ov, nv));
		
	}


	private void onItemChanged(ObservableValue<? extends Lista> v, Lista ov, Lista nv) {
		if (ov != null) {
			view.setCenter(noItemButton);

			ov.setProductos(ov.getProductos());
			ov.getProductos().clear();
			ov.getProductos().addAll(itemController.getProductos());
			ov.setNombreLista(itemController.getNombreLista());
		}
		if (nv != null) {
			
			itemController.productosProperty().clear();
			itemController.productosProperty().addAll(nv.getProductos());
			itemController.getNombreListaTextfield().setText(nv.getNombreLista());
			view.setCenter(itemController.getView());
			
		}
	}

	public BorderPane getView() {
		return view;
	}
	
	/**
	 * 
	 * Funciones de Conn para trabajar con la BD
	 */
	public static Connection getConn() {
		return conn;
	}
	
	public static void setConn(Connection conn) {
		ListsOfListController.conn = conn;
	}

	@FXML
	void OnAddButton(ActionEvent event) {
		Producto prod = new Producto();
		prod.setUnidad("g");
		prod.setNombreProducto("food");
		prod.setCantidad(0);

		Lista list = new Lista();
		list.setNombreLista("place");
		list.getProductos().add(prod);

		userList.add(list);
	}

	@FXML
	void OnRemoveButton(ActionEvent event) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("¿Seguro?");
		alert.setHeaderText("Va a borrar una lista PERMANENTEMENTE");
		alert.setContentText("Va a borrar la lista "+selectedUser.get().getNombreLista()+" de forma permanente."
				+ "\n ¿Está seguro que quiere hacerlo?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    userList.remove(selectedUser.get());
		} else {
		    System.out.println();
		}
	}
	

    @FXML
    void onGuardarDatos(ActionEvent event) throws SQLException {
    	
    	if(conn.isValid(0)) {
    		System.out.println("valid");
    		
    		PreparedStatement psMySQL = conn.prepareStatement("UPDATE usuarios SET datosuser=? WHERE usuario=? AND contraseña=?;");
    		
    		User user = new User();
    		user.getListasObject().addAll(userList);
    		String json = new Gson().toJson(user);
    		
    		psMySQL.setString(1, json);
    		psMySQL.setString(2, usuarioName);
    		psMySQL.setString(3, usuarioPassw);
    		
    		if(psMySQL.executeUpdate() == 1) {
    			Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("Datos guardados");
    			alert.setHeaderText(null);
    			alert.setContentText("Sus datos se han guardado en la nube :)");

    			alert.showAndWait();
    		}

    	}
    }

	/**
	 * Muestra la ventana de la APP
	 */
	public void showApp() {
		Stage listStage = new Stage();

		listStage.setScene(new Scene(this.getView(), 850, 700));
		listStage.setTitle("ShopBasket");
		listStage.initModality(Modality.WINDOW_MODAL);
		listStage.initOwner(App.getStage());
		usernameLabel.setText("Usuario: "+usuarioName);

		listStage.showAndWait();

	}

	// PROPERTIES

	public ObjectProperty<Lista> selectedUserProperty() {
		return this.selectedUser;
	}

	public Lista getSelectedUser() {
		return this.selectedUserProperty().get();
	}

	public void setSelectedUser(final Lista selectedUser) {
		this.selectedUserProperty().set(selectedUser);
	}

	public ListProperty<Lista> userListProperty() {
		return this.userList;
	}

	public ObservableList<Lista> getUserList() {
		return this.userListProperty().get();
	}

	public void setUserList(final ObservableList<Lista> userList) {
		this.userListProperty().set(userList);
	}

	public String getUsuarioName() {
		return usuarioName;
	}

	public void setUsuarioName(String usuarioName) {
		this.usuarioName = usuarioName;
	}

	public String getUsuarioPassw() {
		return usuarioPassw;
	}

	public void setUsuarioPassw(String usuarioPassw) {
		this.usuarioPassw = usuarioPassw;
	}
}
