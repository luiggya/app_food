package edu.espol.pedidosya;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class Register extends Activity {
	TextView login;
	EditText user;
	EditText pass;
	EditText ruc;
	EditText name;
	Button register;
	static Register _sharedInstance;
	static ProgressDialog pdialog;
	
	//SINGLETON FUNCTION
	public static final Register instance() {
        if (_sharedInstance == null) {
            _sharedInstance = new Register();
        }
        return _sharedInstance;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registro);
		_sharedInstance=this;
		AsyncHTTPTask.setContext(Register.instance());
		user = (EditText) findViewById(R.id.user);
		pass = (EditText) findViewById(R.id.pass);
		ruc = (EditText) findViewById(R.id.ruc);
		name = (EditText) findViewById(R.id.name);
		login = (TextView) findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent login = new Intent(Register.instance(), MainActivity.class);
				Register.instance().startActivity(login);
			}
		});
		register = (Button) findViewById(R.id.register);
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pdialog= ProgressDialog.show(Register.instance(), "", "Cargando...", true);
				JSONObject params = new JSONObject();
				JSONObject data = new JSONObject();
				try {
					data.put("user", user.getText());
					data.put("contrasena", pass.getText());
					data.put("name", name.getText());
					data.put("ruc", ruc.getText());
					params.put("data", data);
					params.put("request", "registro");
					new AsyncHTTPTask().execute(new JSONObject[]{params});
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public void comeIn(){
		Intent main = new Intent(Register.instance(),Main.class);
		startActivity(main);
		pdialog.dismiss();
	}
	
	public void error(String error){
		pdialog.dismiss();
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(Register.instance());
		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(error).setTitle("Error");
		// Add the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User clicked OK button
			}
		});
		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}

}
