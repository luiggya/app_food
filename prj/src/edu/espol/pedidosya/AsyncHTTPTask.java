package edu.espol.pedidosya;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class AsyncHTTPTask extends AsyncTask<JSONObject, Void, HttpResponse> {

	private JSONObject jsonRequest;
	private HttpResponse response;
	private static Context context=MainActivity.instance();
	@Override
	protected HttpResponse doInBackground(JSONObject... params) {
		HttpParams parameters = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(parameters, 20 * 1000);
        HttpProtocolParams.setContentCharset(parameters, "UTF-8");

        // Create and initialize scheme registry
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        // Create an HttpClient with the ThreadSafeClientConnManager.
        // This connection manager must be used if more than one thread will
        // be using the HttpClient.
        ClientConnectionManager cm = new ThreadSafeClientConnManager(parameters, schemeRegistry);

        HttpClient client = new DefaultHttpClient(cm, parameters);
		
		Log.d("AsyncHTTPTask - doInBackground", "se ha definido el cliente: " + client);

		try {
		    Log.d("AsyncHTTPTask - doInBackground", "creando un request post");
		    HttpRequestBase request = new HttpPost("http://api.jigl.com");
		    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	        multipartEntity.addPart("data", new StringBody(params[0].toString()));
	        ((HttpPost) request).setEntity(multipartEntity);

		    Log.d("AsyncHTTPTask - doInBackground", "definido request: " +request);

		    synchronized (this) {
		        // store request. It then can be used for terminating this
		        // construction operation.
		    	jsonRequest = params[0];
		    }
		    Log.d("AsyncHTTPTask - doInBackground", "Json enviado: "+params[0].toString());
		    response = client.execute(request);
		    Log.d("AsyncHTTPTask - doInBackground", "recogiendo Response: "+response.getEntity());

		    // check return code
		    StatusLine status = response.getStatusLine();

		    int code = status.getStatusCode();
		    Log.d("AsyncHTTPTask - doInBackground", "code de la respuesta: "+code);
		} catch (Exception e) {
			Log.d("AsyncHTTPTask - doInBackground", "Exception!: "+e.toString());
        }
		
		synchronized (this) {
			return response;
		}
	}
	
	@Override
	protected void onPostExecute(HttpResponse result) {
		try {
			String respStr = EntityUtils.toString(result.getEntity());
			Log.d("AsyncHTTPTask - onPostExecute", "recogiendo respuesta del server: "+respStr);
			JSONObject response = new JSONObject(respStr);
			switch (response.getInt("error")) {
			case 101:
				ResponseManager.instance().error(response.getString("descriptionerror"), context);
				break;
			default:
				if(jsonRequest.getString("request").equals("login")){
					Log.d("AsyncHTTPTask - onPostExecute", "login-JsonObject: "+response.getString("data"));
					ResponseManager.instance().login(response.getJSONObject("data"));
				}else if(jsonRequest.getString("request").equals("login")){
					Log.d("AsyncHTTPTask - onPostExecute", "register-JsonObject: "+response.getString("data"));
					ResponseManager.instance().register(response.getJSONObject("data"));
				}else if(jsonRequest.getString("request").equals("platos")){
					Log.d("AsyncHTTPTask - onPostExecute", "platos-JsonObject: "+response.getString("data"));
					ResponseManager.instance().platos(response.getJSONArray("data"));
				}
				break;
			}
		} catch (JSONException e) {
			Log.d("AsyncHTTPTask - onPostExecute", "JSONException: " + e.toString());
			//e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("static-access")
	public void setContext(Context context){
		this.context=context;
	}
}
