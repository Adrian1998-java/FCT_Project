package fct.shopbasket.utils;

import java.util.ArrayList;
import java.util.List;

public class User {

	private ArrayList<Lista> ListasObject = new ArrayList<Lista>();

	// GETTERS
	public List<Lista> getListasObject() {
		return ListasObject;
	}

	// SETTERS
	public void setListasObject(ArrayList<Lista> listasObject) {
		ListasObject = listasObject;
	}


	@Override
	public String toString() {
		return getListasObject().toString();
	}
}
