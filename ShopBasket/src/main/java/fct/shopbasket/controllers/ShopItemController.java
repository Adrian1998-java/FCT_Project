package fct.shopbasket.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import fct.shopbasket.utils.Lista;
import fct.shopbasket.utils.Producto;
import fct.shopbasket.utils.Unidades;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

/**
 * Controlador de los items. Trabaja con los items dentro de una lista
 * 
 * @author scrag
 *
 */
public class ShopItemController implements Initializable {

	// MODEL
	private ObjectProperty<Producto> productoSelected = new SimpleObjectProperty<>();
	private ListProperty<Producto> productos = new SimpleListProperty<Producto>(
			FXCollections.observableArrayList(new ArrayList<Producto>()));

	private StringProperty nombreLista = new SimpleStringProperty();

	@FXML
	private TextField cantidadTextfield;

	@FXML
	private TextField nombreTextfield;

	@FXML
	private ListView<Producto> productosList;

	@FXML
	private TextField nombreListaTextfield = new TextField();

	@FXML
	private Button cambiarButton;

	@FXML
	private ComboBox<Unidades> unidadCombo = new ComboBox<>();

	@FXML
	private Button plusButton;

	@FXML
	private Button removeButton;

	@FXML
	private BorderPane view;

	public ShopItemController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ShopItem.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		nombreListaTextfield.textProperty().bindBidirectional(nombreLista);

		productosList.itemsProperty().bind(productos);

		productoSelectedProperty().bind(productosList.getSelectionModel().selectedItemProperty());

		productoSelected.addListener((v, ov, nv) -> onProductChanged(v, ov, nv));

		unidadCombo.getItems().setAll(Unidades.values());
		unidadCombo.getSelectionModel().selectFirst();

//		plusButton.setDisable(true);
		removeButton.setDisable(true);
		nombreTextfield.setDisable(true);
		cantidadTextfield.setDisable(true);
		unidadCombo.setDisable(true);
		cambiarButton.setDisable(true);

	}

	/**
	 * Listener para cuando cambia un producto
	 * 
	 * @param v
	 * @param ov
	 * @param nv
	 */
	private void onProductChanged(ObservableValue<? extends Producto> v, Producto ov, Producto nv) {

		if (nv != null) {

			plusButton.setDisable(false);
			removeButton.setDisable(false);
			nombreTextfield.setDisable(false);
			cantidadTextfield.setDisable(false);
			unidadCombo.setDisable(false);
			cambiarButton.setDisable(false);

			nombreTextfield.setText(nv.getNombreProducto());
			cantidadTextfield.setText(String.valueOf(nv.getCantidad()));
			unidadCombo.setValue(Unidades.valueOf(nv.getUnidad()));
			
		} else {
//			plusButton.setDisable(true);
			removeButton.setDisable(true);
			nombreTextfield.setDisable(true);
			cantidadTextfield.setDisable(true);
			unidadCombo.setDisable(true);
			cambiarButton.setDisable(true);
		}
	}

	/**
	 * Cambia los datos que se han modificado del item actual
	 * 
	 * @param event
	 */
	@FXML
	void onCambio(ActionEvent event) {
		productoSelected.get().setNombreProducto(nombreTextfield.getText());
		productoSelected.get().setCantidad(Integer.parseInt(cantidadTextfield.getText()));
		productoSelected.get().setUnidad(unidadCombo.getValue().toString());
		productosList.getSelectionModel().selectPrevious();
		productosList.getSelectionModel().selectNext();
	}

	/**
	 * Añade un nuevo Item
	 * 
	 * @param event
	 */
	@FXML
	void onPlus(ActionEvent event) {
		Producto prod = new Producto();
		prod.setUnidad("g");
		prod.setNombreProducto("food");
		prod.setCantidad(0);

		productos.add(prod);
		
		productosList.getSelectionModel().selectLast();
	}

	/**
	 * Elimina el item actual
	 * 
	 * @param event
	 */
	@FXML
	void onRemove(ActionEvent event) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("¿Seguro?");
		alert.setHeaderText("Va a borrar una lista PERMANENTEMENTE");
		alert.setContentText("Va a borrar el producto " + productoSelected.get().getNombreProducto() + " de esta lista."
				+ "\n ¿Está seguro que quiere hacerlo?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			productos.remove(productoSelected.get());
		} else {
			System.out.println();
		}
	}

	public BorderPane getView() {
		return view;
	}

	public TextField getNombreListaTextfield() {
		return nombreListaTextfield;
	}

	public ListView<Producto> getProductosList() {
		return productosList;
	}

	// PROPERTIES

	public ObjectProperty<Producto> productoSelectedProperty() {
		return this.productoSelected;
	}

	public Producto getProductoSelected() {
		return this.productoSelectedProperty().get();
	}

	public void setProductoSelected(final Producto productoSelected) {
		this.productoSelectedProperty().set(productoSelected);
	}

	public ListProperty<Producto> productosProperty() {
		return this.productos;
	}

	public ObservableList<Producto> getProductos() {
		return this.productosProperty().get();
	}

	public void setProductos(final ObservableList<Producto> productos) {
		this.productosProperty().set(productos);
	}

	public StringProperty nombreListaProperty() {
		return this.nombreLista;
	}

	public String getNombreLista() {
		return this.nombreListaProperty().get();
	}

	public void setNombreLista(final String nombreLista) {
		this.nombreListaProperty().set(nombreLista);
	}

}
