

package codigo.labplc.mx.trackxi.registro;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.dialogos.Dialogos;
import codigo.labplc.mx.trackxi.expresionesregulares.RegularExpressions;
import codigo.labplc.mx.trackxi.fonts.fonts;
import codigo.labplc.mx.trackxi.log.BeanDatosLog;
import codigo.labplc.mx.trackxi.paginador.Paginador;
import codigo.labplc.mx.trackxi.registro.bean.UserBean;
import codigo.labplc.mx.trackxi.registro.validador.EditTextValidator;
import codigo.labplc.mx.trackxi.utils.Utils;

public class MitaxiRegisterManuallyActivity extends Activity {
	
	public final String TAG = this.getClass().getSimpleName();
	
	private int RESULT_LOAD_IMAGE = 100;
	private int RESULT_LOAD_FOTO = 200;
	private int RESULT_LOAD_CONTACT = 300;
	private int RESULT_LOAD_CONTACT_2 = 400;

	private AlertDialog customDialog = null; // Creamos el dialogo generico
	private EditText etInfousername;
	private EditText etInfouseremail;
	private EditText etInfousertelemergency;
	private EditText etInfousermailemergency;
	private EditText etInfousertelemergency_2;
	private EditText etInfousermailemergency_2;
	private ImageView txtparaque;
	private ImageView userfoto;
	private String foto;
	private UserBean user;
	private boolean hasFoto = false;
	String origen;
	String fotoNotFull;
	  private String selectedImagePath;
	
	private boolean[] listHasErrorEditText = { false, false, false, false, false, false };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		    
		setContentView(R.layout.activity_mitaxi_register_manually);
		
		BeanDatosLog.setTagLog(TAG);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			origen = bundle.getString("origen");	

		}
		

		initUI();

	}

	/**
	 * metodo que inicia la vista
	 */
	public void initUI() {
		// creamos la ruta de la foto con codigo unico
		//foto = Environment.getExternalStorageDirectory() + "/imagen"+ getCode() + ".jpg";


		TextView mitaxiregistermanually_tv_label= (TextView)findViewById(R.id.mitaxiregistermanually_tv_label);
		 mitaxiregistermanually_tv_label.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		 mitaxiregistermanually_tv_label.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_ROJO));
		 
		 TextView mitaxiregistermanually_tv_emergencia= (TextView)findViewById(R.id.mitaxiregistermanually_tv_emergencia);
		 mitaxiregistermanually_tv_emergencia.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		 mitaxiregistermanually_tv_emergencia.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_ROJO));
		 
		
		
		etInfousername = (EditText) findViewById(R.id.mitaxiregistermanually_et_infousername);
		etInfousername.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		etInfousername.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		
		etInfouseremail = (EditText) findViewById(R.id.mitaxiregistermanually_et_infouseremail);
		etInfouseremail.setText(Utils.getEmail(getApplicationContext()));
		if(!etInfouseremail.getText().toString().equals("")){
			etInfouseremail.setEnabled(false);
		}
		etInfouseremail.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		etInfouseremail.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));

		
		etInfousertelemergency = (EditText) findViewById(R.id.mitaxiregistermanually_et_telemer);
		etInfousertelemergency.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		etInfousertelemergency.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));

		
		etInfousermailemergency = (EditText) findViewById(R.id.mitaxiregistermanually_et_correoemer);
		etInfousermailemergency.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		etInfousermailemergency.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		
		etInfousertelemergency_2 = (EditText) findViewById(R.id.mitaxiregistermanually_et_telemer_2);
		etInfousertelemergency_2.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		etInfousertelemergency_2.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));

		
		etInfousermailemergency_2 = (EditText) findViewById(R.id.mitaxiregistermanually_et_correoemer_2);
		etInfousermailemergency_2.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		etInfousermailemergency_2.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));


		
		Button contacto_emer = (Button) findViewById(R.id.mitaxiregistermanually_btn_contactos);
		contacto_emer.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		contacto_emer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,Contacts.CONTENT_URI);
				startActivityForResult(intent, RESULT_LOAD_CONTACT);
			}
		});

		Button contacto_emer_2 = (Button) findViewById(R.id.mitaxiregistermanually_btn_contactos_2);
		contacto_emer_2.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		contacto_emer_2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,Contacts.CONTENT_URI);
				startActivityForResult(intent, RESULT_LOAD_CONTACT_2);
			}
		});

		
		userfoto = (ImageView) findViewById(R.id.mitaxiregistermanually_im_fotousuario);
		userfoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				origenDeLaImagen().show();

			}
		});

		txtparaque = (ImageView) findViewById(R.id.mitaxiregistermanually_im_info);
		txtparaque.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				new Dialogos().mostrarParaQue(
						MitaxiRegisterManuallyActivity.this).show();
			}
		});

		etInfousername.setTag(RegularExpressions.KEY_IS_NICKNAME);
		etInfouseremail.setTag(RegularExpressions.KEY_IS_EMAIL);
		etInfousertelemergency.setTag(RegularExpressions.KEY_IS_NUMBER);
		etInfousermailemergency.setTag(RegularExpressions.KEY_IS_EMAIL);
		etInfousertelemergency_2.setTag(RegularExpressions.KEY_IS_NUMBER);
		etInfousermailemergency_2.setTag(RegularExpressions.KEY_IS_EMAIL);

		etInfousername.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
						getBaseContext(), etInfousername, listHasErrorEditText,0));
		etInfouseremail.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
						getBaseContext(), etInfouseremail,listHasErrorEditText, 1));
		etInfousertelemergency.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
						getBaseContext(), etInfousertelemergency,listHasErrorEditText, 2));
		etInfousermailemergency.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
						getBaseContext(), etInfousermailemergency,listHasErrorEditText, 3));
		etInfousertelemergency_2.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
				getBaseContext(), etInfousertelemergency_2,listHasErrorEditText, 4));
		etInfousermailemergency_2.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
				getBaseContext(), etInfousermailemergency_2,listHasErrorEditText, 5));
		
	Button mitaxiregistermanually_btn_ok =(Button)findViewById(R.id.mitaxiregistermanually_btn_ok);
	mitaxiregistermanually_btn_ok.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_AMARILLO));
	mitaxiregistermanually_btn_ok.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (!isAnyEditTextEmpty()) {
				if (!hasErrorEditText()) {
					if (Utils.isNetworkConnectionOk(getBaseContext())) {
						try {
							if(!hasFoto){
								guardarImagenEstatica();
								hasFoto=true;
							}
							saveUserInfo();
						} catch (JSONException e) {
							BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
						}
					} else {
						Log.d("error 302", getString(R.string.no_internet_connection));						
					//	Dialogos.Toast(getApplicationContext(), getString(R.string.no_foto_add), Toast.LENGTH_LONG);
					}
				} else {
					Dialogos.Toast(MitaxiRegisterManuallyActivity.this,getString(R.string.edittext_wrong_info),Toast.LENGTH_LONG);

				}
			} else {
					Dialogos.Toast(MitaxiRegisterManuallyActivity.this,getString(R.string.edittext_emtpy),Toast.LENGTH_LONG);

			}
		}

		
	});
		Button mitaxiregistermanually_btn_cancel=(Button)findViewById(R.id.mitaxiregistermanually_btn_cancel);
		mitaxiregistermanually_btn_cancel.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_AMARILLO));
		mitaxiregistermanually_btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		
		if(origen!=null&&origen.equals("menu")){
			llenarCampos();
			mitaxiregistermanually_btn_ok.setText("Actualizar");
			hasFoto = true;
		}
		
	}

	public void llenarCampos(){
		SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
		etInfousername.setText(prefs.getString("nombre", null));
		etInfousername.setEnabled(false);
		etInfouseremail.setText(prefs.getString("correo", null));
		etInfouseremail.setEnabled(false);
		etInfousertelemergency.setText(prefs.getString("telemer", null));
		etInfousermailemergency.setText(prefs.getString("correoemer", null));
		etInfousertelemergency_2.setText(prefs.getString("telemer2", null));
		etInfousermailemergency_2.setText(prefs.getString("correoemer2", null));
		foto = prefs.getString("foto", null);
		File file = new File(foto);
		if (file.exists()) {
			Bitmap myBitmap = BitmapFactory.decodeFile(file
					.getAbsolutePath());
			Matrix mat = new Matrix();
			Bitmap bMapRotate = Bitmap.createBitmap(myBitmap, 0, 0,
					myBitmap.getWidth(), myBitmap.getHeight(), mat, true);
			userfoto.setImageBitmap(bMapRotate);
			
		}
		
	}

	public boolean hasErrorEditText() {
		for (boolean hasError : listHasErrorEditText)
			if (hasError)
				return true;
		return false;
	}

	public void saveUserInfo() throws JSONException {

		user = new UserBean();
		user.setNombre(etInfousername.getText().toString().replaceAll(" ", "+"));
		user.setCorreo(etInfouseremail.getText().toString());
		user.setTelemergencia(etInfousertelemergency.getText().toString());
		user.setCorreoemergencia(etInfousermailemergency.getText().toString());
		user.setTelemergencia_2(etInfousertelemergency_2.getText().toString());
		user.setCorreoemergencia_2(etInfousermailemergency_2.getText().toString());
		user.setFoto(foto);
		
		Upload nuevaTarea = new Upload();
		nuevaTarea.execute(foto);

	}

	/**
	 * Guarda las preferencias del usuario en la aplicación
	 * 
	 * @param (user) bean que contiene los datos del usuario
	 * @return void
	 */
	public void savePreferences(UserBean user) {

		SharedPreferences prefs = getSharedPreferences(
				"MisPreferenciasTrackxi", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("nombre", user.getNombre());
		editor.putString("correo", user.getCorreo());
		editor.putString("telemer", user.getTelemergencia());
		editor.putString("correoemer", user.getCorreoemergencia());
		editor.putString("telemer2", user.getTelemergencia_2());
		editor.putString("correoemer2", user.getCorreoemergencia_2());
		editor.putString("uuid", user.getUUID());
		editor.putString("foto", user.getFoto());
		editor.commit();

		if(origen!=null&&origen.equals("menu")){
			
		}else{
			Intent mainIntent = new Intent().setClass(MitaxiRegisterManuallyActivity.this, Paginador.class);
			startActivity(mainIntent);
		}
		
		MitaxiRegisterManuallyActivity.this.finish();

	}

	/**
	 * Verifica si alguno de los campos {@link EditText} con la información del
	 * usuario está vacio.
	 * 
	 * @return (boolean) <b>true</b> si esta vacio <b>false</b> si no esta vacio
	 */
	public boolean isAnyEditTextEmpty() {
		boolean empty = false;
		if (EditTextValidator.isEditTextEmpty(etInfousername))
			empty = true;
		if (EditTextValidator.isEditTextEmpty(etInfouseremail))
			empty = true;
		if (EditTextValidator.isEditTextEmpty(etInfousertelemergency))
			empty = true;
		if (EditTextValidator.isEditTextEmpty(etInfousermailemergency))
			empty = true;
		if(EditTextValidator.isEditTextEmpty(etInfousertelemergency_2))
			empty = true;
		if(EditTextValidator.isEditTextEmpty(etInfousermailemergency_2))
			empty = true;
		return empty;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RESULT_LOAD_FOTO) {
			File file = new File(foto);
			if (file.exists()) {
				Bitmap myBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()), 800,480 , false);//Utils.decodeSampledBitmapFromResource(file.getAbsolutePath(),200,200);
				Matrix mat = new Matrix();
				mat.postRotate(-90);
				Bitmap bMapRotate = Bitmap.createBitmap(myBitmap, 0, 0,	myBitmap.getWidth(), myBitmap.getHeight(), mat, true);
				userfoto.setImageBitmap(bMapRotate);
				BitmapDrawable drawable = (BitmapDrawable) userfoto.getDrawable();
  			Bitmap bitmap_prev = drawable.getBitmap();
	  			 try{
	  			       
	  			        FileOutputStream fOut = new FileOutputStream(file);
	  			        bitmap_prev.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
	  			        fOut.flush();
	  			        fOut.close();}
	  			    catch (Exception e) {
	  			    	BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
	  			}
				hasFoto = true;
			}
			          
		}else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK	&& null != data) {
			 Uri selectedImageUri = data.getData();
			 Log.d("imagen uri", selectedImageUri+"");
				 Bitmap myBitmap;
				try {
					myBitmap = Bitmap.createScaledBitmap( MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri),800,480 , false);
				 userfoto.setImageBitmap(myBitmap);
	  			foto = Environment.getExternalStorageDirectory() + "/Traxi/perfil/imagen"+Utils.getCode()+".jpg";
				fotoNotFull = Environment.getExternalStorageDirectory() + "/Traxi/perfil";
				File dir = new File (fotoNotFull); 
				if (!dir.exists())
				{
		           dir.mkdirs();
				}
			    File fn2;  
			    try { // Try to Save #2  
			        fn2 = new File(foto);    
			        FileOutputStream out = new FileOutputStream(fn2);  
			        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);  
			        out.flush();  
			        out.close();  
			      
			    } catch (Exception e) {  
			    	BeanDatosLog.setDescripcion(Utils.getStackTrace(e)); 
			    }
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				 hasFoto = true; 


	
         }else if (requestCode == RESULT_LOAD_CONTACT) {
 			getContactInfo(data,1);
 		}else if (requestCode == RESULT_LOAD_CONTACT_2) {
 			getContactInfo(data,2);
 		}
	}

	@Override
	public void onBackPressed() {
		if(origen!=null&&origen.equals("menu")){
				super.onBackPressed();
		}else{
			new Dialogos().seguroQuiereSalir(MitaxiRegisterManuallyActivity.this).show();
		}
	}

	/**
	 * clase que envia por post los datos del registro
	 * 
	 * @author mikesaurio
	 * 
	 */
	class Upload extends AsyncTask<String, Void, Void> {

		private ProgressDialog pDialog;
		private String miFoto = "";
		private String resultado="";
		public static final int HTTP_TIMEOUT = 30 * 1000;

		@SuppressWarnings("deprecation")
		@Override
		protected Void doInBackground(String... params) {
			miFoto = (String) params[0];
			try

			{

				System.setProperty("http.keepAlive", "false");
				HttpClient httpclient = new DefaultHttpClient();
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				final HttpParams par = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(par, HTTP_TIMEOUT);
				HttpConnectionParams.setSoTimeout(par, HTTP_TIMEOUT);
				ConnManagerParams.setTimeout(par, HTTP_TIMEOUT);
				HttpPost httppost;
				if(origen!=null&&origen.equals("menu")){
				 httppost = new HttpPost("http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=updatepost");
				}else{
				 httppost = new HttpPost("http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=addpost");
				}
				MultipartEntity entity = new MultipartEntity();
				entity.addPart("nombre", new StringBody(user.getNombre() + ""));
				entity.addPart("correo", new StringBody(user.getCorreo() + ""));
				entity.addPart("telemer",new StringBody(user.getTelemergencia() + ""));
				entity.addPart("os", new StringBody(user.getOs() + ""));
				entity.addPart("correoemer",new StringBody(user.getCorreoemergencia() + ""));
				entity.addPart("telemer2",new StringBody(user.getTelemergencia_2() + ""));
				entity.addPart("correoemer2",new StringBody(user.getCorreoemergencia_2() + ""));
				
				File file = new File(miFoto);
				entity.addPart("foto", new FileBody(file));
				System.setProperty("http.keepAlive", "false");
				httppost.setEntity(entity);
				HttpResponse response = httpclient.execute(httppost);
				BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String linea = "";
				String NL = System.getProperty("line.separator");

				while ((linea = in.readLine()) != null) {
					sb.append(linea + NL);
				}
				in.close();
				resultado = sb.toString();
			//	Log.d("******", sb.toString()+"sjkdnds,jn");
				
				httpclient = null;
				response = null;
				if (resultado != null) {
					String errorJson = "";
					String successsJson = "";
					String pk_user = "";
					JSONObject json = (JSONObject) new JSONTokener(resultado).nextValue();
					JSONObject json2 = json.getJSONObject("message");
					try {
						errorJson = (String) json2.get("error");
					} catch (JSONException e) {
						errorJson = null;
					}
					try {
						successsJson = (String) json2.get("success");
						pk_user = (String) json2.get("id");
					} catch (JSONException e) {
						successsJson = null;
						pk_user = null;
					}

					if (pk_user != null) {
						user.setUUID(pk_user);// agregamos el UUID del usuario
						savePreferences(user); // guardamos todo en preferencias

					} else if (errorJson != null) {
						Log.d("error 201", resultado+"");
							resultado="muyLargo";
						
					}
				} else {
					Log.d("error 202", "Null en la respuesta");
					
				}
			} catch (Exception e) {
				BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
			}
			return null;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MitaxiRegisterManuallyActivity.this);
			pDialog.setMessage(getResources().getString(R.string.mitaxiregistermanually_subiendo_la_info));
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(resultado.equals("muyLargo")){
				Dialogos.Toast(MitaxiRegisterManuallyActivity.this, getResources().getString(R.string.imagen_muy_grande), Toast.LENGTH_LONG);
			}
			pDialog.dismiss();
			this.cancel(true);
		}

	}

	/**
	 * Dialogo para que el usuario reporte una anomalia en el taxi o el chofer
	 * 
	 * @param Activity
	 *            (actividad que llama al diálogo)
	 * @return Dialog (regresa el dialogo creado)
	 **/
	public Dialog origenDeLaImagen() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = getLayoutInflater().inflate(
				R.layout.dialogo_tipo_de_imagen, null);
		builder.setView(view);
		builder.setCancelable(true);
		//fuentes
((Button) view.findViewById(R.id.dialogo_tipo_de_imagen_btnCancelar)).setTypeface(new fonts(MitaxiRegisterManuallyActivity.this).getTypeFace(fonts.FLAG_AMARILLO));
((Button) view.findViewById(R.id.dialogo_tipo_de_imagen_btnAceptar)).setTypeface(new fonts(MitaxiRegisterManuallyActivity.this).getTypeFace(fonts.FLAG_AMARILLO));
((TextView) view.findViewById(R.id.dialogo_tipo_de_imagen_tv_texto)).setTypeface(new fonts(MitaxiRegisterManuallyActivity.this).getTypeFace(fonts.FLAG_ROJO));
((TextView) view.findViewById(R.id.dialogo_tipo_de_imagen_tv_texto)).setTextColor(new fonts(MitaxiRegisterManuallyActivity.this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));


		// escucha del boton aceptar
		((Button) view.findViewById(R.id.dialogo_tipo_de_imagen_btnCancelar)).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						foto = Environment.getExternalStorageDirectory() + "/Traxi/perfil/imagen"+Utils.getCode()+".jpg";
						//foto = Environment.getExternalStorageDirectory() + "/imagen"+ NetworkUtils.getCode() + ".jpg";
						File dir = new File (Environment.getExternalStorageDirectory() + "/Traxi/perfil"); 
			    	       if (!dir.exists())
			    	       {
			    	           dir.mkdirs();
			    	       }
						// Camara
						Intent intent = new Intent(	MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
						Uri output = Uri.fromFile(new File(foto));
						intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
						startActivityForResult(intent, RESULT_LOAD_FOTO); 
						customDialog.dismiss(); // cerramos el diálogo
					}
				});

		// escucha del boton cancelar
		((Button) view.findViewById(R.id.dialogo_tipo_de_imagen_btnAceptar)).setOnClickListener(new OnClickListener() {

					

					@Override
					public void onClick(View view) {
					/*	foto = Environment.getExternalStorageDirectory() + "/Traxi/perfil/imagen"+Utils.getCode()+".jpg";
						fotoNotFull = Environment.getExternalStorageDirectory() + "/Traxi/perfil";*/
						Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(i, RESULT_LOAD_IMAGE);

						customDialog.dismiss(); // cerramos el diálogo
					}

				});
		return (customDialog = builder.create());
	}
	
	
	public void getContactInfo(Intent intent, int contacto)
	{
		try{
			  Cursor   cursor =  managedQuery(intent.getData(), null, null, null, null);      
			  if(!cursor.isClosed()&&cursor!=null){
			   while (cursor.moveToNext()) 
			   {           
			       String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			       String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)); 
			       String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
		
			       if ( hasPhone.equalsIgnoreCase("1")){
			           hasPhone = "true";
			           
			       }else{
			           hasPhone = "false" ;
			       }
			       if (Boolean.parseBoolean(hasPhone)) 
			       {
			        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
			        while (phones.moveToNext()) 
			        {
			          String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			          if(contacto==1)
			        	  etInfousertelemergency.setText(phoneNumber.replaceAll(" ", ""));
			          else
			        	  etInfousertelemergency_2.setText(phoneNumber.replaceAll(" ", "")); 
			          break;
			        }
			        phones.close();
			       }
		
			       // Find Email Addresses
			       Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);
			       while (emails.moveToNext()) 
			       {
			        String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			        if(contacto==1)
			        	etInfousermailemergency.setText(emailAddress);
			        else
			        	etInfousermailemergency_2.setText(emailAddress);
			        break;
			       }
			       
			       emails.close();
			       break;
			  }  
			  }
		}catch(Exception e){
			BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
		}
	}

	   /**
	    * cuando el usuario no pone foto de perfil le asignamos una por defecto 
	    */
	    public void guardarImagenEstatica() {

		 	Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_dino);
			foto = Environment.getExternalStorageDirectory() + "/Traxi/perfil/imagen"+Utils.getCode()+".jpg";
			fotoNotFull = Environment.getExternalStorageDirectory() + "/Traxi/perfil";
			File dir = new File (fotoNotFull); 
			if (!dir.exists())
			{
	           dir.mkdirs();
			}
		    File fn2;  
		    try { // Try to Save #2  
		        fn2 = new File(foto);    
		        FileOutputStream out = new FileOutputStream(fn2);  
		        bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);  
		        out.flush();  
		        out.close();  
		      
		    } catch (Exception e) {  
		    	BeanDatosLog.setDescripcion(Utils.getStackTrace(e)); 
		    }  
			
		}
	  
	    
	  
	
}