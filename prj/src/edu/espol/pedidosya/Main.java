package edu.espol.pedidosya;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class Main extends Activity {
	ListView platos;
	Button doPedido;
	static Main _sharedInstance;
	static ProgressDialog pdialog;
	
	//SINGLETON FUNCTION
	public static final Main instance() {
        if (_sharedInstance == null) {
            _sharedInstance = new Main();
        }
        return _sharedInstance;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		platos = (ListView) findViewById(R.id.lista);
//		doPedido = (Button) findViewById(R.id.pedido);
		doPedido.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pdialog= ProgressDialog.show(Main.instance(), "", "Cargando...", true);
//				JSONObject params = new JSONObject();
//				JSONObject data = new JSONObject();
//				try {
//					data.put("user", user.getText());
//					data.put("password", pass.getText());
//					data.put("name", name.getText());
//					data.put("ruc", ruc.getText());
//					params.put("data", data);
//					params.put("request", "register");
//					new AsyncHTTPTask().execute(new JSONObject[]{params});
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		});
		
		pdialog= ProgressDialog.show(Main.instance(), "", "Cargando...", true);
		JSONObject params = new JSONObject();
		try {
			params.put("request", "platos");
			new AsyncHTTPTask().execute(new JSONObject[]{params});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void enlistar(){
		pdialog.dismiss();
		// TODO: poner en el listview platos los textos guardados en el modelo platos
		// TODO: la funcion es Cliente.instance().getLplatos()
	}
	
	public void error(String error){
		pdialog.dismiss();
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(Main.instance());
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
