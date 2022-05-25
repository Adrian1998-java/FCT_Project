package fct.shopbasket.utils;

import com.google.gson.Gson;

public class UseGson {
	
	public UseGson() {}
	
	public String PassToGson(User usuario) {
		
		return new Gson().toJson(usuario);
	}
	
	public User PassFromGson(String json) {
		
		return new Gson().fromJson(json, User.class);
	}
}
