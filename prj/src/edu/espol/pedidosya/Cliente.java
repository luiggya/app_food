package edu.espol.pedidosya;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Cliente {
	String username;
	String Name;
	String Ruc;
	List<Platos> lplatos = new ArrayList<Platos>();
	static Cliente instance;
	
	public Cliente() {
		super();
	}
	
	public static Cliente instance(){
		if(instance==null)
			instance=new Cliente();
		return instance;
	}
	
	public void initialize(JSONObject json){
		try {
			this.setUsername(json.getString("username"));
			this.setRuc(json.getString("ruc"));
			this.setName(json.getString("name"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}
	
	public String getRuc() {
		return Ruc;
	}

	public void setRuc(String ruc) {
		Ruc = ruc;
	}

	public List<Platos> getLplatos() {
		return lplatos;
	}

	public void setLplatos(List<Platos> lplatos) {
		this.lplatos = lplatos;
	}

	public void setPlatos(JSONArray response) {
		for (int i = 0; i < response.length(); i++) {
			try {
				JSONObject plato = (JSONObject) response.get(i);
				this.lplatos.add(new Platos(plato.getString("nombre"),
						plato.getString("descripcion"),plato.getString("precio")));
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
