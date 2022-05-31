package fct.shopbasket.utils;

import java.util.ArrayList;
import java.util.List;

public class Lista {

	private String nombreLista;
	private ArrayList<Producto> productos = new ArrayList<Producto>();


	//GETTERS
	public String getNombreLista() {
		return nombreLista;
	}
	public ArrayList<Producto> getProductos() {
		return productos;
	}
	//SETTERS
	public void setNombreLista(String nombreLista) {
		this.nombreLista = nombreLista;
	}	
	public void setProductos(ArrayList<Producto> productos) {
		this.productos = productos;
	}
	
	@Override
	public String toString() {
		return getNombreLista()+" ("+getProductos().size()+")";
	}
}
