package codigo.labplc.mx.traxi.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.fonts.fonts;
import codigo.labplc.mx.traxi.log.DatosLogBean;

/**
 * Clase que maneja metodos staticos para ayudar a las demas clases
 * @author mikesaurio
 *
 */
@SuppressLint({"SimpleDateFormat","TrulyRandom","DefaultLocale"})
@SuppressWarnings("unused")
public class Utils {


	/**
	 * metodo que hace la conexion al servidor con una url especifica
	 * 
	 * @param url
	 *            (String) ruta del web service
	 * @return (String) resultado del service
	 */
	public static String doHttpConnection(String url) {
		HttpClient Client = new DefaultHttpClient();
		try {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
			HttpGet httpget = new HttpGet(url);
			HttpResponse hhrpResponse = Client.execute(httpget);
			HttpEntity httpentiti = hhrpResponse.getEntity();
			// Log.d("RETURN HTTPCLIENT", EntityUtils.toString(httpentiti));
			return EntityUtils.toString(httpentiti);
		} catch (ParseException e) {
			DatosLogBean.setDescripcion(Utils.getStackTrace(e));
			return null;
		} catch (IOException e) {
			DatosLogBean.setDescripcion(Utils.getStackTrace(e));
			return null;
		}
	}

	/**
	 * Metodo privado que genera un codigo unico segun la hora y fecha del
	 * sistema
	 * 
	 * @return photoCode
	 * */

	public static String getCode() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = dateFormat.format(new Date());
		String photoCode = "pic_" + date;
		return photoCode;
	}

	/**
	 * 
	 * @param URL
	 * @param jsonObjSend
	 * @return
	 */
	public static JSONObject SendHttpPost(String URL, JSONObject jsonObjSend) {

		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPostRequest = new HttpPost(URL);

			StringEntity se;
			se = new StringEntity(jsonObjSend.toString());

			// Set HTTP parameters
			httpPostRequest.setEntity(se);
			httpPostRequest.setHeader("Accept", "application/json");
			httpPostRequest.setHeader("Content-type", "application/json");
			httpPostRequest.setHeader("Accept-Encoding", "gzip"); // only set this parameter if you would like to use gzip compression

			long t = System.currentTimeMillis();
			HttpResponse response = (HttpResponse) httpclient
					.execute(httpPostRequest);

			// Get hold of the response entity (-> the data):
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// Read the content stream
				InputStream instream = entity.getContent();
				Header contentEncoding = response
						.getFirstHeader("Content-Encoding");
				if (contentEncoding != null
						&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					instream = new GZIPInputStream(instream);
				}

				// convert content stream to a String
				String resultString = convertStreamToString(instream);
				instream.close();
				resultString = resultString.substring(1,
						resultString.length() - 1); // remove wrapping "[" and
													// "]"

				// Transform the String into a JSONObject
				JSONObject jsonObjRecv = new JSONObject(resultString);
				// Raw DEBUG output of our received JSON object:

				return jsonObjRecv;
			}

		} catch (Exception e) {
			// More about HTTP exception handling in another tutorial.
			// For now we just print the stack trace.
			DatosLogBean.setDescripcion(Utils.getStackTrace(e));
		}
		return null;
	}

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 * 
		 * (c) public domain:
		 * http://senior.ceng.metu.edu.tr/2009/praeda/2009/01/
		 * 11/a-simple-restful-client-at-android/
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			DatosLogBean.setDescripcion(Utils.getStackTrace(e));
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				DatosLogBean.setDescripcion(Utils.getStackTrace(e));
			}
		}
		return sb.toString();
	}

	public static String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}

	/**
	 * metodo que convierte una imagen a grises
	 * 
	 * @return bitmap
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	/**
	 * metodo que vaida que el telefono tenga internet
	 * 
	 * @param a
	 * @return
	 */
	public static boolean hasInternet(Activity a) {
		boolean hasConnectedWifi = false;
		boolean hasConnectedMobile = false;
		ConnectivityManager cm = (ConnectivityManager) a
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("wifi"))
				if (ni.isConnected())
					hasConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("mobile"))
				if (ni.isConnected())
					hasConnectedMobile = true;
		}
		return hasConnectedWifi || hasConnectedMobile;
	}

	/**
	 * lavve para enviar correos gmail traxi
	 * @param a
	 * @return
	 */
	public static String getMAilKey(Context a) {

		try {
			final String ALGORITMO = "AES";
			final byte[] valor_clave = "0000000000000000".getBytes();
			Key key = new SecretKeySpec(valor_clave, ALGORITMO);
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decodificar_texto = Base64.decode(a.getResources()
					.getString(R.string.envio_mail).getBytes("UTF-8"),
					Base64.DEFAULT);
			byte[] desencriptado = cipher.doFinal(decodificar_texto);
			return new String(desencriptado, "UTF-8");
		} catch (Exception e) {
			DatosLogBean.setDescripcion(Utils.getStackTrace(e));
			return "falla";
		}
	}

	public static String getEmail(Context context) {
		AccountManager accountManager = AccountManager.get(context);
		Account account = getAccount(accountManager);

		if (account == null) {
			return null;
		} else {
			return account.name;
		}
	}

	private static Account getAccount(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];
		} else {
			account = null;
		}
		return account;
	}

	/**
	 * encripta una cadena dada
	 * 
	 * @param texto_a_encriptar
	 * @return
	 * @throws Exception
	 */


	@SuppressLint("TrulyRandom")
	public static String encriptar(String texto_a_encriptar) throws Exception {
		final byte[] valor_clave = "0000000000000000".getBytes();
		Key key = new SecretKeySpec(valor_clave, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encrypted = cipher.doFinal(texto_a_encriptar.getBytes("UTF-8"));
		String texto_encriptado = Base64.encodeToString(encrypted,
				Base64.DEFAULT);// new String(encrypted, "UTF-8");
		return texto_encriptado;

	}

	/**
	 * valid si es nummero o no
	 * 
	 * @return (true) si es numero (false) si no es numero
	 */
	public static boolean isNumeric(String cadena) {
		try {
			Integer.parseInt(cadena);

			return true;
		} catch (NumberFormatException nfe) {

			return false;
		}
	}

	/**
	 * convierte DP a pixeles
	 * @param context
	 * @param dp
	 * @return
	 */
	public static float convertDpToPixel(Context context, float dp) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	/**
	 * convierte pixeles a DP
	 * @param context
	 * @param px
	 * @return
	 */
	public static float convertPixelsToDp(Context context, float px) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}
	
	
	/**
	 * prepara el Action Bar custom
	 * @param (Activity)activity
	 * @param (int) vista para inflar
	 * @param (String) titulo
	 * @param (float) tamaño del titulo
	 * @return (ActionBar) inflado
	 */

	public static ActionBar crearActionBar(Activity activity, int vista,String nombre,float tamano){
		ActionBar ab	= activity.getActionBar();
		ab.setDisplayShowHomeEnabled(false);
		ab.setDisplayShowTitleEnabled(false);
		
		final LayoutInflater inflater = (LayoutInflater) activity.getSystemService("layout_inflater");
		View view = inflater.inflate(vista, null);
		
		((TextView) view.findViewById(R.id.abs_layout_tv_titulo)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_MAMEY));
		((TextView) view.findViewById(R.id.abs_layout_tv_titulo)).setText(nombre.toUpperCase());
		if(tamano!=0.0f){
			((TextView) view.findViewById(R.id.abs_layout_tv_titulo)).setTextSize(tamano);
		}
		ab.setDisplayShowCustomEnabled(true);
		ab.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
		ab.setCustomView(view);

		return ab;
	}
	
	/**
	 * elimina todos los archivos de un directorio
	 * @param fileOrDirectory
	 */
	public static void DeleteRecursive(File fileOrDirectory) {
	    if (fileOrDirectory.isDirectory())
	        for (File child : fileOrDirectory.listFiles())
	            DeleteRecursive(child);
	}
	
	
	/**
	 * metodo que crea y regresa ek estado de as oreferencias
	 * @param preferencia
	 * @return
	 */
	public static boolean getPreferencia(String preferencia,Context act,boolean defaul) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(act);
		return sharedPrefs.getBoolean(preferencia, defaul);
	}
	
	/**
	  * metodo que regresa la distancia de 2 puntos en metros
	  * @param lat1 (Double) latitud inicial
	  * @param lng1 (Double) longitud inicial
	  * @param lat2 (Double) latitud final
	  * @param lng2 (Double) longitud final
	  * @return (long) diatancia entre puntos
	  */
	 public static long getDistanceMeters(double lat1, double lng1, double lat2, double lng2) {

		    double l1 = Math.toRadians(lat1);
		    double l2 = Math.toRadians(lat2);
		    double g1 = Math.toRadians(lng1);
		    double g2 = Math.toRadians(lng2);

		    double dist = Math.acos(Math.sin(l1) * Math.sin(l2) + Math.cos(l1) * Math.cos(l2) * Math.cos(g1 - g2));
		    if(dist < 0) {
		        dist = dist + Math.PI;
		    }

		    return Math.round(dist * 6378100);
		}
	

}