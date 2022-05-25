package fct.shopbasket.utils;

public class Producto {

	private String nombreProducto;
	private int cantidad;
	private String unidad;
	
	//Getters 
	public String getNombreProducto() {
		return nombreProducto;
	}
	public int getCantidad() {
		return cantidad;
	}
	public String getUnidad() {
		return unidad;
	}
	
	//Setters
	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	} 
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	
	@Override
	public String toString() {
		return getNombreProducto()+" con cantidad de "+getCantidad()+ " "+getUnidad();
	}
}
