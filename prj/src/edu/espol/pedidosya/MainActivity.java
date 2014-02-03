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

public class MainActivity extends Activity {
	TextView register;
	EditText user;
	EditText pass;
	Button login;
	static MainActivity _sharedInstance;
	static ProgressDialog pdialog;
	
	//SINGLETON FUNCTION
	public static final MainActivity instance() {
        if (_sharedInstance == null) {
            _sharedInstance = new MainActivity();
        }
        return _sharedInstance;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		_sharedInstance=this;
		user = (EditText) findViewById(R.id.user);
		pass = (EditText) findViewById(R.id.pass);
		register = (TextView) findViewById(R.id.register);
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent register = new Intent(MainActivity.instance(), Register.class);
				MainActivity.this.startActivity(register);
			}
		});
		
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pdialog= ProgressDialog.show(MainActivity.instance(), "", "Cargando...", true);
				JSONObject params = new JSONObject();
				JSONObject data = new JSONObject();
				try {
					data.put("user", user.getText());
					data.put("contrasena", pass.getText());
					params.put("data", data);
					params.put("request", "login");
					new AsyncHTTPTask().execute(new JSONObject[]{params});
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public void error(String error){
		pdialog.dismiss();
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.instance());
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
	
	public void comeIn(){
		Intent main = new Intent(MainActivity.instance(),Main.class);
		startActivity(main);
		pdialog.dismiss();
	}

}
