package edu.espol.pedidosya;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * @author Angel Astudillo
 */
public class ResponseManager {

	private static ResponseManager _sharedInstance;
	
	public static final ResponseManager instance() {
        if (_sharedInstance == null) {
            _sharedInstance = new ResponseManager();
        }
        return _sharedInstance;
    }
	
	public void login(JSONObject response) throws JSONException{
		Log.d("ResponseManager - LoginResponse", "login-AccountManager Init response: "+ response);
		Cliente.instance().initialize(response);
		MainActivity.instance().comeIn();
	}
	
	public void register(JSONObject response) throws JSONException{
		Log.d("ResponseManager - Register", "register-AccountManager Init response: "+ response);
		Cliente.instance().initialize(response);
		Register.instance().comeIn();
	}
	
	public void platos(JSONArray response) throws JSONException{
		Log.d("ResponseManager - Register", "platos-AccountManager Init response: "+ response);
		Cliente.instance().setPlatos(response);
		Main.instance().enlistar();
	}

	public void error(String string, Context context) {
		if(context==MainActivity.instance()){
			MainActivity.instance().error(string);
		}else if(context==Register.instance()){
			Register.instance().error(string);
		}
	}
}
